/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.message.service;

import ehi.classifier.bean.ProcessingCode;
import ehi.gps.classifier.AccountingEntryType;
import ehi.gps.classifier.StatusCodeMapper;
import ehi.gps.model.Currency;
import ehi.message.model.Amount;
import ehi.message.model.Message;
import ehi.message.model.MessageMainData;
import ehi.message.model.Response;
import ehi.web.service.client.soap.SOAPConnector;
import ehi.web.service.client.soap.schemas.gps.GetTransaction;
import ehi.web.service.client.soap.schemas.gps.GetTransactionResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ehi.gps.classifier.StatusCodeMapper.STATUS_CODE_SUCCESS;
import static ehi.message.Util.randomNumberInRange;
import static ehi.message.Util.resolveEndMessage;
import static ehi.message.service.XmlObjectUtil.toObject;
import static ehi.message.service.XmlObjectUtil.toXml;

@Component
public class MessageServiceImpl implements MessageService {

    @Autowired
    private SOAPConnector soapConnector;

    private static final Logger logger = LogManager.getLogger(MessageServiceImpl.class);

    @Override
    public String createRequestForNewTransaction(Message message) {
        GetTransaction request = createRequest(message);
        return toXml(request, GetTransaction.class);
    }

    @Override
    public String createRequestForSameTransaction(Message message) {
        GetTransaction getTrn = (GetTransaction) toObject(message.xmlRequest, GetTransaction.class);
        getTrn.setTxnType(message.transactionType.txnType);
        getTrn.setMTID(message.transactionType.mtId);
        getTrn.setTXnID(getNewTxnId());
        getTrn.setTxnAmt(message.amount.value.setScale(4, BigDecimal.ROUND_DOWN).doubleValue());
        getTrn.setBillAmt(resolveBillAmountSign(message));
        getTrn.setPOSTimeDE12(resolvePosTime(message));
        return toXml(getTrn, GetTransaction.class);
    }

    @Override
    public Response doRequest(String url, String request) {
        logger.debug("SOAP Request: " + request);
        GetTransaction requestObj = (GetTransaction) toObject(request, GetTransaction.class);
        GetTransactionResponse soapResponse = (GetTransactionResponse) soapConnector.callWebService(url, requestObj);

        Response response = new Response();
        response.statusCode = soapResponse.getGetTransactionResult().getResponsestatus();
        response.statusMessage = StatusCodeMapper.getText(response.statusCode);
        response.xml = toXml(soapResponse, GetTransactionResponse.class);
        logger.debug("SOAP Response: " + response.xml);

        return response;
    }

    @Override
    public List<MessageMainData> getMessagesMainData(Message message) {
        List<MessageMainData> data = new ArrayList<>();
        Message endMessage = resolveEndMessage(message);

        data.add(getMessageMainData(endMessage));
        while (endMessage.parent != null) {
            endMessage = endMessage.parent;
            data.add(getMessageMainData(endMessage));
        }

        return data;
    }

    private static MessageMainData getMessageMainData(Message message) {
        GetTransaction requestObj = (GetTransaction) toObject(message.xmlRequest, GetTransaction.class);

        MessageMainData mainData = new MessageMainData();
        mainData.referenceNumber = requestObj.getTXnID();
        mainData.traceIdLifecycle = requestObj.getTraceidLifecycle();
        mainData.cardPcId = requestObj.getToken();
        mainData.transactionType = message.transactionType;
        mainData.request = message.xmlRequest;
        mainData.response = message.response.xml;
        mainData.success = STATUS_CODE_SUCCESS.equals(message.response.statusCode);
        mainData.statusMessage = message.response.statusMessage;

        Amount amount = new Amount();
        amount.currency = new Currency();
        amount.value = BigDecimal.valueOf(requestObj.getTxnAmt());
        amount.currency.isoCode = message.country.currency.isoCode;
        mainData.amount = amount;

        Amount billingAmount = new Amount();
        billingAmount.currency = new Currency();
        billingAmount.value = BigDecimal.valueOf(requestObj.getBillAmt());
        billingAmount.currency.isoCode = message.amount.currency.isoCode;
        mainData.billingAmount = billingAmount;

        return mainData;
    }

    private static GetTransaction createRequest(Message message) {
        final String id = getNewTraceId();
        final String gpsSequence = randomNumberInRange(1000, 99999);

        GetTransaction getTrn = new GetTransaction();

        getTrn.setAcquirerIdDE32(randomNumberInRange(1111111, 9999999));
        getTrn.setActBal(0.00);
        getTrn.setAuthCodeDE38(randomNumberInRange(111111, 999999));
        getTrn.setAvlBal(0.00);
        getTrn.setBillAmt(resolveBillAmountSign(message));
        getTrn.setBillCcy(message.amount.currency.number);
        getTrn.setBlkAmt(0.00);
        getTrn.setCustRef(message.card.customerReference);
        getTrn.setFXPad(0.00);
        getTrn.setFeeFixed(0.00);
        getTrn.setFeeRate(0.00);
        getTrn.setMCCCode(message.mcc.code);
        getTrn.setMCCDesc(message.mcc.description);
        getTrn.setMCCPad(0.00);
        getTrn.setMerchIDDE42("111222" + randomNumberInRange(111111, 999999));
        getTrn.setMerchNameDE43(message.scheme.getLabel());
        getTrn.setPOSDataDE22(message.posCapability.getValue() + message.pinEntryCapability.getValue() + "0");
        getTrn.setPOSTermnlDE41(randomNumberInRange(11111111, 99999999));
        getTrn.setPOSTimeDE12(resolvePosTime(message));
        getTrn.setProcCode(wrapProcessingCodeValue(message.processingCode));
        getTrn.setRespCodeDE39("00");
        getTrn.setRetRefNoDE37("6036");
        getTrn.setSettleAmt(0.00);
        getTrn.setStatusCode("00");
        getTrn.setToken(message.card.pcId);
        getTrn.setTransLink(id);
        getTrn.setTxnAmt(message.amount.value.setScale(4, BigDecimal.ROUND_DOWN).doubleValue());
        getTrn.setTxnCCy(message.country.currency.number);
        getTrn.setTxnCtry(message.country.isoCodeAlpha3);
        getTrn.setTxnDesc(message.scheme.getLabel());
        getTrn.setTxnGPSDate(message.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        getTrn.setTXnID(getNewTxnId());
        getTrn.setTxnStatCode("A");
        getTrn.setTXNTimeDE07(message.date.format(DateTimeFormatter.ofPattern("MMddHHmmss")));
        getTrn.setTxnType(message.transactionType.txnType);
        getTrn.setAuthorisedByGPS("N");
        getTrn.setCUGroup(message.card.cardUsageGroup);
        getTrn.setInstCode(message.card.institutionCode);
        getTrn.setMTID(message.transactionType.mtId);
        getTrn.setProductID(StringUtils.hasText(message.card.productIdInPc) ? Integer.valueOf(message.card.productIdInPc) : null);
        getTrn.setSubBIN(StringUtils.hasText(message.card.subBin) ? Integer.valueOf(message.card.subBin) : null);
        getTrn.setTLogIDOrg("0");
        getTrn.setVLGroup("MOP-VL-001");
        getTrn.setDomFeeFixed("0.00");
        getTrn.setNonDomFeeFixed("0.00");
        getTrn.setFxFeeFixed("0.00");
        getTrn.setFxFeeRate("0.00");
        getTrn.setDomFeeRate("0.00");
        getTrn.setNonDomFeeRate("0.00");
        getTrn.setCVV2(message.card.cvv2);
        getTrn.setExpiryDate(message.card.expiry);
        getTrn.setSendingAttemptCount("1");
        getTrn.setGPSPOSCapability("1234567890123456789012345678901234567890SDF9POLKA");
        getTrn.setGPSPOSData("2");
        getTrn.setAcquirerReferenceData031("6036");
        getTrn.setTraceidLifecycle(id);
        getTrn.setBalanceSequence(Integer.valueOf(gpsSequence));
        getTrn.setBalanceSequenceExthost(Integer.valueOf(gpsSequenceHost(gpsSequence)));
        getTrn.setMerchName(message.merchant.name);
        getTrn.setMerchStreet(message.merchant.address.street);
        getTrn.setMerchCity(message.merchant.address.city);
        getTrn.setMerchRegion(message.merchant.address.region);
        getTrn.setMerchPostcode(message.merchant.address.postCode);
        getTrn.setMerchCountry(message.merchant.address.country);
        getTrn.setMerchTel(message.merchant.phoneNumber);
        getTrn.setMerchURL(message.merchant.url);
        getTrn.setMerchNameOther(message.merchant.nameOther);
        getTrn.setMerchNetId(message.merchant.netId);
        getTrn.setMerchTaxId(message.merchant.taxId);
        getTrn.setMerchContact(message.merchant.contact);

        return getTrn;
    }

    private static String gpsSequenceHost(String gpsSequence) {
        return ((Integer) (Integer.valueOf(gpsSequence) - 1)).toString();
    }

    private static String getNewTraceId() {
        return "ID" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }

    private static String getNewTxnId() {
        return "TXNID" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }

    private static double resolveBillAmountSign(Message message) {
        BigDecimal amount;
        if (AccountingEntryType.DEBIT.equals(message.processingCode.accountingEntryType)) {
            amount = message.amount.value.negate();
        } else {
            amount = message.amount.value;
        }

        // Change billing amount sign to opposite for all Reversal messages
        if (message.transactionType.description.indexOf("Reversal") > -1) {
            amount = amount.negate();
        }

        return amount.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
    }

    private static String wrapProcessingCodeValue(ProcessingCode processingCode) {
        return processingCode.value + "0000";
    }

    private static String resolvePosTime(Message message) {
        if (message.getTransactionType().txnType.equals("A") || message.getTransactionType().txnType.equals("D")) {
            return message.date.format(DateTimeFormatter.ofPattern("HHmmss"));
        } else {
            return message.date.format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
        }
    }

}
