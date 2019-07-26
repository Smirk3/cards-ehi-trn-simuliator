/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.classifier.bean;

import ehi.gps.classifier.AccountingEntryType;

public class ProcessingCode {

    public String value;

    public String label;

    public AccountingEntryType accountingEntryType;

    public ProcessingCode() {
    }

    public ProcessingCode(String value, String label, AccountingEntryType accountingEntryType) {
        this.value = value;
        this.label = label;
        this.accountingEntryType = accountingEntryType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public AccountingEntryType getAccountingEntryType() {
        return accountingEntryType;
    }

    public void setAccountingEntryType(AccountingEntryType accountingEntryType) {
        this.accountingEntryType = accountingEntryType;
    }
}
