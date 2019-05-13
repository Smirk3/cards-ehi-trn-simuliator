package ehi.card;

public class Card {

    public String pcId;

    public String number;

    public String institutionCode;

    public String cardUsageGroup;

    public String subBin;

    public String productIdInPc;

    public String cvv2;

    public String customerReference;

    public String expiry;


    public Card() {
    }

    public Card(String pcId, String number, String institutionCode, String cardUsageGroup, String subBin, String productIdInPc, String cvv2, String customerReference, String expiry) {
        this.pcId = pcId;
        this.number = number;
        this.institutionCode = institutionCode;
        this.cardUsageGroup = cardUsageGroup;
        this.subBin = subBin;
        this.productIdInPc = productIdInPc;
        this.cvv2 = cvv2;
        this.customerReference = customerReference;
        this.expiry = expiry;
    }

    public String getPcId() {
        return pcId;
    }

    public void setPcId(String pcId) {
        this.pcId = pcId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getInstitutionCode() {
        return institutionCode;
    }

    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }

    public String getCardUsageGroup() {
        return cardUsageGroup;
    }

    public void setCardUsageGroup(String cardUsageGroup) {
        this.cardUsageGroup = cardUsageGroup;
    }

    public String getSubBin() {
        return subBin;
    }

    public void setSubBin(String subBin) {
        this.subBin = subBin;
    }

    public String getProductIdInPc() {
        return productIdInPc;
    }

    public void setProductIdInPc(String productIdInPc) {
        this.productIdInPc = productIdInPc;
    }

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }
}
