package ehi.gps.model;

public class Country {

    public String name;
    public String capital;
    public String isoCodeAlpha2;
    public String isoCodeAlpha3;
    public String isoCodeNumeric;
    public Currency currency;

    public Country(String name, String capital, String isoCodeAlpha2, String isoCodeAlpha3, String isoCodeNumeric, Currency currency) {
        this.name = name;
        this.capital = capital;
        this.isoCodeAlpha2 = isoCodeAlpha2;
        this.isoCodeAlpha3 = isoCodeAlpha3;
        this.isoCodeNumeric = isoCodeNumeric;
        this.currency = currency;
    }
}
