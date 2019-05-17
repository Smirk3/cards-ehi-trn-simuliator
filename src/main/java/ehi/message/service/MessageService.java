package ehi.message.service;

import ehi.gps.classifier.AccountingEntryType;
import ehi.jaxb.generated.GetTransaction;
import ehi.message.model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ehi.message.Util.randomNumberInRange;

@Component
public class MessageService {

    private static final Logger logger = LogManager.getLogger(MessageService.class);

    public void setNewRequest(Message message) {
        message.xmlRequest = createXmlRequest(message);
    }

    private static String createXmlRequest(Message message) {
        final String id = resolveTraceId();
        final String gpsSequence = randomNumberInRange(1000, 99999);

        GetTransaction getTrn = new GetTransaction();

        getTrn.setAcquirerIdDE32(randomNumberInRange(1111111, 9999999));
        getTrn.setActBal("0.00");
        getTrn.setAuthCodeDE38(randomNumberInRange(111111, 999999));
        getTrn.setAvlBal("0.00");
        getTrn.setBillAmt(resolveBillAmount(message).setScale(2, BigDecimal.ROUND_DOWN).toString());
        getTrn.setBillCcy(message.amount.currency.number);
        getTrn.setBlkAmt("0.00");
        getTrn.setCustRef(message.card.customerReference);
        getTrn.setFXPad("0.00");
        getTrn.setFeeFixed("0.00");
        getTrn.setFeeRate("0.00");
        getTrn.setMCCCode(message.mcc.code);
        getTrn.setMCCDesc(message.mcc.description);
        getTrn.setMCCPad("0.00");
        getTrn.setMerchIDDE42("111222" + randomNumberInRange(111111, 999999));
        getTrn.setMerchNameDE43(message.scheme.getLabel());
        getTrn.setPOSDataDE22(message.posCapability.getValue() + message.pinEntryCapability.getValue() + "0");
        getTrn.setPOSTermnlDE41(randomNumberInRange(11111111, 99999999));
        getTrn.setPOSTimeDE12(message.date.format(DateTimeFormatter.ofPattern("HHmmss")));
        getTrn.setProcCode(message.processingCode.value + "0000");
        getTrn.setRespCodeDE39("00");
        getTrn.setRetRefNoDE37("6036");
        getTrn.setSettleAmt("0.00");
        getTrn.setStatusCode("00");
        getTrn.setToken(message.card.pcId);
        getTrn.setTransLink(id);
        getTrn.setTxnAmt(message.amount.value.setScale(4, BigDecimal.ROUND_DOWN).toString());
        getTrn.setTxnCCy(message.country.currency.number);
        getTrn.setTxnCtry(message.country.isoCodeAlpha3);
        getTrn.setTxnDesc(message.scheme.getLabel());
        getTrn.setTxnGPSDate(message.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        getTrn.setTXnID("TXNID" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
        getTrn.setTxnStatCode("A");
        getTrn.setTXNTimeDE07(message.date.format(DateTimeFormatter.ofPattern("MMddHHmmss")));
        getTrn.setTxnType(message.transactionType.txnType);
        getTrn.setAuthorisedByGPS("N");
        getTrn.setCUGroup(message.card.cardUsageGroup);
        getTrn.setInstCode(message.card.institutionCode);
        getTrn.setMTID(message.transactionType.mtId);
        getTrn.setProductID(message.card.productIdInPc);
        getTrn.setSubBIN(message.card.subBin);
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
        getTrn.setBalanceSequence(gpsSequence);
        getTrn.setBalanceSequenceExthost(gpsSequenceHost(gpsSequence));
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

        return generateXml(getTrn);
    }

    private static String gpsSequenceHost(String gpsSequence) {
        return ((Integer) (Integer.valueOf(gpsSequence) - 1)).toString();
    }

    private static String resolveTraceId() {
        return "ID" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }

    private static BigDecimal resolveBillAmount(Message message) {
        if (AccountingEntryType.DEBIT.equals(message.processingCode.accountingEntryType)) {
            return message.amount.value.negate();
        } else {
            return message.amount.value;
        }
    }

    private static String generateXml(GetTransaction object) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(GetTransaction.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "");

            StringOutputStream output = new StringOutputStream();
            jaxbMarshaller.marshal(object, output);
            String xml = output.toString();

            xml = xml.replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"\\?>\n", "");
            xml = xml.replaceAll(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", "");
            xml = xml.replaceAll(" xsi:schemaLocation=\"\"", "");
            xml = xml.replaceAll(" xsi:nil=\"true\"", "");
            xml = xml.replaceAll("ns2:", "");
            xml = xml.replaceAll(":ns2", "");

            return xml;

        } catch (JAXBException e) {
            logger.error("Could not marshal object: " + e.getMessage(), e);
            throw new IllegalArgumentException();
        }
    }

/*
    public static void main(String[] args) {
        GetTransaction getTrn = new GetTransaction();
        System.out.println(new MessageService().generateXml(getTrn));
        System.out.println(BigDecimal.valueOf(5.19).setScale(4, BigDecimal.ROUND_DOWN));

    }
*/

}
