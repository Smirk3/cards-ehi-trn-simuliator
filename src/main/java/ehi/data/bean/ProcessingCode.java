package ehi.data.bean;

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
}
