/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.merchant.model;

public class AddressBuilder {
    private String street;
    private String city;
    private String region;
    private String postCode;
    private String country;

    public AddressBuilder setStreet(String street) {
        this.street = street;
        return this;
    }

    public AddressBuilder setCity(String city) {
        this.city = city;
        return this;
    }

    public AddressBuilder setRegion(String region) {
        this.region = region;
        return this;
    }

    public AddressBuilder setPostCode(String postCode) {
        this.postCode = postCode;
        return this;
    }

    public AddressBuilder setCountry(String country) {
        this.country = country;
        return this;
    }

    public Address createAddress() {
        return new Address(street, city, region, postCode, country);
    }
}