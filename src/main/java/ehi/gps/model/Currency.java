package ehi.gps.model;

public class Currency {

    public String entity;
    public String name;
    public String isoCode;
    public int minorUnit;

    public Currency(String entity, String name, String isoCode, int minorUnit) {
        this.entity = entity;
        this.name = name;
        this.isoCode = isoCode;
        this.minorUnit = minorUnit;
    }
}
