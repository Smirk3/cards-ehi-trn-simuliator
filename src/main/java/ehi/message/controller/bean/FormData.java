/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.message.controller.bean;

import ehi.card.Card;
import ehi.classifier.bean.Mcc;
import ehi.classifier.bean.ProcessingCode;
import ehi.classifier.bean.TransactionType;
import ehi.gps.classifier.PinEntryCapability;
import ehi.gps.classifier.PosCapability;
import ehi.gps.classifier.Scheme;
import ehi.gps.model.Country;
import ehi.gps.model.Currency;
import ehi.merchant.model.Merchant;

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

    public List<Merchant> merchants;

    public FormData(String ehiUrlDefault,
                    List<Country> countries,
                    List<Scheme> schemes,
                    List<Currency> currencies,
                    List<Mcc> mccs,
                    List<PosCapability> posCapabilities,
                    List<PinEntryCapability> pinEntryCapabilities,
                    List<ProcessingCode> processingCodes,
                    List<TransactionType> transactionTypes,
                    List<Card> cards,
                    List<Merchant> merchants) {
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
        this.merchants = merchants;
    }
}
