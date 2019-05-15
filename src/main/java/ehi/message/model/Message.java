package ehi.message.model;

import ehi.card.Card;
import ehi.classifier.bean.Mcc;
import ehi.classifier.bean.ProcessingCode;
import ehi.classifier.bean.TransactionType;
import ehi.gps.classifier.PinEntryCapability;
import ehi.gps.classifier.PosCapability;
import ehi.gps.classifier.Scheme;
import ehi.gps.model.Country;
import ehi.merchant.model.Merchant;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {

    public String ehiUrl;

    public Scheme scheme;

    public Country country;

    public LocalDateTime date;

    public Amount amount;

    public Mcc mcc;

    public PosCapability posCapability;

    public PinEntryCapability pinEntryCapability;

    public ProcessingCode processingCode;

    public TransactionType transactionType;

    public Card card;

    public Merchant merchant;

    public String getEhiUrl() {
        return ehiUrl;
    }

    public void setEhiUrl(String ehiUrl) {
        this.ehiUrl = ehiUrl;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
