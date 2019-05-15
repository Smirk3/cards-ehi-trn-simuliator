package ehi.message.model;

import ehi.gps.model.Currency;

import java.math.BigDecimal;

public class Amount {

    public BigDecimal value;

    public Currency currency;


    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
