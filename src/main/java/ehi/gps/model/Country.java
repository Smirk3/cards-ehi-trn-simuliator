package ehi.gps.model;

public class Country {

    public String name;
    public String capital;
    public String isoCodeAlpha2;
    public String isoCodeAlpha3;
    public String isoCodeNumeric;
    public Currency currency;

    public Country() {
    }

    public Country(String name, String capital, String isoCodeAlpha2, String isoCodeAlpha3, String isoCodeNumeric, Currency currency) {
        this.name = name;
        this.capital = capital;
        this.isoCodeAlpha2 = isoCodeAlpha2;
        this.isoCodeAlpha3 = isoCodeAlpha3;
        this.isoCodeNumeric = isoCodeNumeric;
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getIsoCodeAlpha2() {
        return isoCodeAlpha2;
    }

    public void setIsoCodeAlpha2(String isoCodeAlpha2) {
        this.isoCodeAlpha2 = isoCodeAlpha2;
    }

    public String getIsoCodeAlpha3() {
        return isoCodeAlpha3;
    }

    public void setIsoCodeAlpha3(String isoCodeAlpha3) {
        this.isoCodeAlpha3 = isoCodeAlpha3;
    }

    public String getIsoCodeNumeric() {
        return isoCodeNumeric;
    }

    public void setIsoCodeNumeric(String isoCodeNumeric) {
        this.isoCodeNumeric = isoCodeNumeric;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
