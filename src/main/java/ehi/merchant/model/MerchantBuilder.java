package ehi.merchant.model;

public class MerchantBuilder {
    private String name;
    private Address address;
    private String phoneNumber;
    private String url;
    private String nameOther;
    private String netId;
    private String taxId;
    private String contact;

    public MerchantBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public MerchantBuilder setAddress(Address address) {
        this.address = address;
        return this;
    }

    public MerchantBuilder setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public MerchantBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public MerchantBuilder setNameOther(String nameOther) {
        this.nameOther = nameOther;
        return this;
    }

    public MerchantBuilder setNetId(String netId) {
        this.netId = netId;
        return this;
    }

    public MerchantBuilder setTaxId(String taxId) {
        this.taxId = taxId;
        return this;
    }

    public MerchantBuilder setContact(String contact) {
        this.contact = contact;
        return this;
    }

    public Merchant createMerchant() {
        return new Merchant(name, address, phoneNumber, url, nameOther, netId, taxId, contact);
    }
}