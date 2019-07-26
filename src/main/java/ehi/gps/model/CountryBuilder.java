/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.gps.model;

public class CountryBuilder {
    private String name;
    private String capital;
    private String isoCodeAlpha2;
    private String isoCodeAlpha3;
    private String isoCodeNumeric;
    private Currency currency;

    public CountryBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CountryBuilder setCapital(String capital) {
        this.capital = capital;
        return this;
    }

    public CountryBuilder setIsoCodeAlpha2(String isoCodeAlpha2) {
        this.isoCodeAlpha2 = isoCodeAlpha2;
        return this;
    }

    public CountryBuilder setIsoCodeAlpha3(String isoCodeAlpha3) {
        this.isoCodeAlpha3 = isoCodeAlpha3;
        return this;
    }

    public CountryBuilder setIsoCodeNumeric(String isoCodeNumeric) {
        this.isoCodeNumeric = isoCodeNumeric;
        return this;
    }

    public CountryBuilder setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public Country createCountry() {
        return new Country(name, capital, isoCodeAlpha2, isoCodeAlpha3, isoCodeNumeric, currency);
    }
}