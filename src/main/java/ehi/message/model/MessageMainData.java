package ehi.message.model;

import ehi.classifier.bean.TransactionType;

public class MessageMainData {

    public String referenceNumber;

    public String traceIdLifecycle;

    public Amount amount;

    public Amount billingAmount;

    public String cardPcId;

    public TransactionType transactionType;

    public String request;

    public String response;

    public Boolean success;

    public String statusMessage;


    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getTraceIdLifecycle() {
        return traceIdLifecycle;
    }

    public void setTraceIdLifecycle(String traceIdLifecycle) {
        this.traceIdLifecycle = traceIdLifecycle;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public Amount getBillingAmount() {
        return billingAmount;
    }

    public void setBillingAmount(Amount billingAmount) {
        this.billingAmount = billingAmount;
    }

    public String getCardPcId() {
        return cardPcId;
    }

    public void setCardPcId(String cardPcId) {
        this.cardPcId = cardPcId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
