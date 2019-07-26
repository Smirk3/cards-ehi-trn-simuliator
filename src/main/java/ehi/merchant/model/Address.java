/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.merchant.model;

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

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return street + ", " + city + ", " + region + ", " + postCode + ", " + country;
    }
}
