package ehi.gps.model;

public class Currency {

    public String entity;
    public String name;
    public String isoCode;
    public String number;
    public int minorUnit;

    public Currency(String entity, String name, String isoCode, String number, int minorUnit) {
        this.entity = entity;
        this.name = name;
        this.isoCode = isoCode;
        this.number = number;
        this.minorUnit = minorUnit;
    }
}
