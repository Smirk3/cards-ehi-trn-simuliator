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

public class FormDataBuilder {
    private String ehiUrlDefault;
    private List<Country> countries;
    private List<Scheme> schemes;
    private List<Currency> currencies;
    private List<Mcc> mccs;
    private List<PosCapability> posCapabilities;
    private List<PinEntryCapability> pinEntryCapabilities;
    private List<ProcessingCode> processingCodes;
    private List<TransactionType> transactionTypes;
    private List<Card> cards;

    public FormDataBuilder setEhiUrlDefault(String ehiUrlDefault) {
        this.ehiUrlDefault = ehiUrlDefault;
        return this;
    }

    public FormDataBuilder setCountries(List<Country> countries) {
        this.countries = countries;
        return this;
    }

    public FormDataBuilder setSchemes(List<Scheme> schemes) {
        this.schemes = schemes;
        return this;
    }

    public FormDataBuilder setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
        return this;
    }

    public FormDataBuilder setMccs(List<Mcc> mccs) {
        this.mccs = mccs;
        return this;
    }

    public FormDataBuilder setPosCapabilities(List<PosCapability> posCapabilities) {
        this.posCapabilities = posCapabilities;
        return this;
    }

    public FormDataBuilder setPinEntryCapabilities(List<PinEntryCapability> pinEntryCapabilities) {
        this.pinEntryCapabilities = pinEntryCapabilities;
        return this;
    }

    public FormDataBuilder setProcessingCodes(List<ProcessingCode> processingCodes) {
        this.processingCodes = processingCodes;
        return this;
    }

    public FormDataBuilder setTransactionTypes(List<TransactionType> transactionTypes) {
        this.transactionTypes = transactionTypes;
        return this;
    }

    public FormDataBuilder setCards(List<Card> cards) {
        this.cards = cards;
        return this;
    }

    public FormData createFormData() {
        return new FormData(ehiUrlDefault, countries, schemes, currencies, mccs, posCapabilities, pinEntryCapabilities, processingCodes, transactionTypes, cards);
    }
}