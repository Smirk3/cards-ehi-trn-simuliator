/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.message.model;

import ehi.gps.model.Currency;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Amount {
    @NotNull
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
