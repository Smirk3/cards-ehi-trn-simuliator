package ehi.message.model;

import ehi.card.Card;
import ehi.data.bean.Mcc;
import ehi.data.bean.ProcessingCode;
import ehi.data.bean.TransactionType;
import ehi.gps.classifier.PinEntryCapability;
import ehi.gps.classifier.PosCapability;
import ehi.gps.classifier.Scheme;
import ehi.gps.model.Country;
import ehi.model.Merchant;

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

}
