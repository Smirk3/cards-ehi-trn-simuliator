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
}
