package ehi.card;

public class CardBuilder {
    private String pcId;
    private String number;
    private String institutionCode;
    private String cardUsageGroup;
    private String subBin;
    private String productIdInPc;
    private String cvv2;
    private String customerReference;
    private String expiry;

    public CardBuilder setPcId(String pcId) {
        this.pcId = pcId;
        return this;
    }

    public CardBuilder setNumber(String number) {
        this.number = number;
        return this;
    }

    public CardBuilder setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
        return this;
    }

    public CardBuilder setCardUsageGroup(String cardUsageGroup) {
        this.cardUsageGroup = cardUsageGroup;
        return this;
    }

    public CardBuilder setSubBin(String subBin) {
        this.subBin = subBin;
        return this;
    }

    public CardBuilder setProductIdInPc(String productIdInPc) {
        this.productIdInPc = productIdInPc;
        return this;
    }

    public CardBuilder setCvv2(String cvv2) {
        this.cvv2 = cvv2;
        return this;
    }

    public CardBuilder setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
        return this;
    }

    public CardBuilder setExpiry(String expiry) {
        this.expiry = expiry;
        return this;
    }

    public Card createCard() {
        return new Card(pcId, number, institutionCode, cardUsageGroup, subBin, productIdInPc, cvv2, customerReference, expiry);
    }
}