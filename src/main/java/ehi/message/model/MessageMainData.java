package ehi.message.model;

import ehi.classifier.bean.TransactionType;

public class MessageMainData {

    public String referenceNumber;

    public Amount amount;

    public Amount billingAmount;

    public String cardPcId;

    public TransactionType transactionType;


    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
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
}
