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
}
