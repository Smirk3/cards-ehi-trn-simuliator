package ehi.model;

public class Address {

    public String street;

    public String city;

    public String region;

    public String postCode;

    public String country;

    public Address() {
    }

    public Address(String street, String city, String region, String postCode, String country) {
        this.street = street;
        this.city = city;
        this.region = region;
        this.postCode = postCode;
        this.country = country;
    }
}
