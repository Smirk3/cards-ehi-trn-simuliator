/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.gps.model;

public class CurrencyBuilder {
    private String entity;
    private String name;
    private String isoCode;
    private String number;
    private Integer minorUnit;

    public CurrencyBuilder setEntity(String entity) {
        this.entity = entity;
        return this;
    }

    public CurrencyBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CurrencyBuilder setIsoCode(String isoCode) {
        this.isoCode = isoCode;
        return this;
    }

    public CurrencyBuilder setNumber(String number) {
        this.number = number;
        return this;
    }

    public CurrencyBuilder setMinorUnit(Integer minorUnit) {
        this.minorUnit = minorUnit;
        return this;
    }

    public Currency createCurrency() {
        return new Currency(entity, name, isoCode, number, minorUnit);
    }
}