package ehi.merchant.model;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setNameOther(String nameOther) {
        this.nameOther = nameOther;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
