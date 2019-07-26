/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.gps.model;

import org.springframework.util.StringUtils;

public class Currency implements Comparable {

    public String entity;
    public String name;
    public String isoCode;
    public String number;
    public Integer minorUnit;

    public Currency() {
    }

    public Currency(String entity, String name, String isoCode, String number, Integer minorUnit) {
        this.entity = entity;
        this.name = name;
        this.isoCode = isoCode;
        this.number = number;
        this.minorUnit = minorUnit;
    }

    public String getIsoCode() {
        return isoCode;
    }

    @Override
    public int compareTo(Object o) {
        String value = ((Currency) o).isoCode;
        if (!StringUtils.hasText(this.isoCode) && !StringUtils.hasText(value)) {
            return 0;
        } else if (!StringUtils.hasText(this.isoCode) && StringUtils.hasText(value)) {
            return -1;
        } else if (StringUtils.hasText(this.isoCode) && !StringUtils.hasText(value)) {
            return 1;
        } else {
            return this.isoCode.compareTo(((Currency) o).isoCode);
        }
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getMinorUnit() {
        return minorUnit;
    }

    public void setMinorUnit(Integer minorUnit) {
        this.minorUnit = minorUnit;
    }
}
