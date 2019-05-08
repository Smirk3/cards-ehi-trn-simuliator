package ehi.model;

public class Merchant {

    public String name;

    public Address address;

    public String phoneNumber;

    public String url;

    public String nameOther;

    public String netId;

    public String taxId;

    public String contact;

    public Merchant() {
        this.address = new Address();
    }

    public Merchant(String name, Address address, String phoneNumber, String url, String nameOther, String netId, String taxId, String contact) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.url = url;
        this.nameOther = nameOther;
        this.netId = netId;
        this.taxId = taxId;
        this.contact = contact;
    }
}
