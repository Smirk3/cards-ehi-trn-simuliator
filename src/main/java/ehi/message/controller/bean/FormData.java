package ehi.message.controller.bean;

import ehi.card.Card;
import ehi.data.bean.Mcc;
import ehi.data.bean.ProcessingCode;
import ehi.data.bean.TransactionType;
import ehi.gps.classifier.PinEntryCapability;
import ehi.gps.classifier.PosCapability;
import ehi.gps.classifier.Scheme;
import ehi.gps.model.Country;
import ehi.gps.model.Currency;

import java.util.List;

public class FormData {

    public String ehiUrlDefault;

    public List<Country> countries;

    public List<Scheme> schemes;

    public List<Currency> currencies;

    public List<Mcc> mccs;

    public List<PosCapability> posCapabilities;

    public List<PinEntryCapability> pinEntryCapabilities;

    public List<ProcessingCode> processingCodes;

    public List<TransactionType> transactionTypes;

    public List<Card> cards;

    public FormData(String ehiUrlDefault,
                    List<Country> countries,
                    List<Scheme> schemes,
                    List<Currency> currencies,
                    List<Mcc> mccs,
                    List<PosCapability> posCapabilities,
                    List<PinEntryCapability> pinEntryCapabilities,
                    List<ProcessingCode> processingCodes,
                    List<TransactionType> transactionTypes,
                    List<Card> cards) {
        this.ehiUrlDefault = ehiUrlDefault;
        this.countries = countries;
        this.schemes = schemes;
        this.currencies = currencies;
        this.mccs = mccs;
        this.posCapabilities = posCapabilities;
        this.pinEntryCapabilities = pinEntryCapabilities;
        this.processingCodes = processingCodes;
        this.transactionTypes = transactionTypes;
        this.cards = cards;
    }
}
