package ehi.message;

import ehi.card.Card;
import ehi.card.CardBuilder;
import ehi.card.exception.CardNotFoundException;
import ehi.classifier.bean.Mcc;
import ehi.classifier.bean.ProcessingCode;
import ehi.classifier.bean.TransactionType;
import ehi.gps.model.Country;
import ehi.gps.model.CountryBuilder;
import ehi.gps.model.Currency;
import ehi.gps.model.CurrencyBuilder;
import ehi.merchant.exception.MerchantNotFoundException;
import ehi.merchant.model.AddressBuilder;
import ehi.merchant.model.Merchant;
import ehi.merchant.model.MerchantBuilder;
import ehi.message.exception.CountryNotFoundException;
import ehi.message.exception.CurrencyNotFoundException;
import ehi.message.exception.MccNotFoundException;
import ehi.message.exception.ProcessingCodeNotFoundException;
import ehi.message.exception.TransactionTypeNotFoundException;
import ehi.message.model.Amount;
import ehi.message.model.Message;
import ehi.message.model.Response;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.SplittableRandom;

public class Util {

    public static Message newMessageInstance() {
        Message message = new Message();
        message.date = LocalDateTime.now();
        message.country = new Country();
        message.amount = new Amount();
        message.amount.currency = new CurrencyBuilder().createCurrency();
        message.mcc = new Mcc();
        message.processingCode = new ProcessingCode();
        message.transactionType = new TransactionType();
        message.card = new Card();
        message.merchant = new Merchant();
        return message;
    }

    public static Card findCard(List<Card> cards, String cardPcId) throws CardNotFoundException {
        if (!StringUtils.hasText(cardPcId) || CollectionUtils.isEmpty(cards)) {
            throw new CardNotFoundException("Card '" + cardPcId + "' was not found.");
        }

        Optional<Card> card = cards.stream().filter(c -> cardPcId.equals(c.pcId)).findAny();

        if (card == null || !card.isPresent()){
            throw new CardNotFoundException("Card '" + cardPcId + "' was not found.");
        }

        return new CardBuilder()
            .setPcId(card.get().pcId)
            .setNumber(card.get().number)
            .setInstitutionCode(card.get().institutionCode)
            .setCardUsageGroup(card.get().cardUsageGroup)
            .setSubBin(card.get().subBin)
            .setProductIdInPc(card.get().productIdInPc)
            .setCvv2(card.get().cvv2)
            .setCustomerReference(card.get().customerReference)
            .setExpiry(card.get().expiry)
            .createCard();
    }

    public static Merchant findMerchant(List<Merchant> merchants, String merchantName) throws MerchantNotFoundException {
        if (!StringUtils.hasText(merchantName) || CollectionUtils.isEmpty(merchants)) {
            throw new MerchantNotFoundException("Merchant " + merchantName + " was not found.");
        }

        Optional<Merchant> merchant = merchants.stream().filter(m -> merchantName.equals(m.name)).findAny();

        if (merchant == null || !merchant.isPresent()) {
            throw new MerchantNotFoundException("Merchant " + merchantName + " was not found.");
        }

        return new MerchantBuilder()
            .setName(merchant.get().name)
            .setAddress(new AddressBuilder().setStreet(merchant.get().address.street)
                                            .setCity(merchant.get().address.city)
                                            .setRegion(merchant.get().address.region)
                                            .setPostCode(merchant.get().address.postCode)
                                            .setCountry(merchant.get().address.country).createAddress())
            .setPhoneNumber(merchant.get().phoneNumber)
            .setUrl(merchant.get().url)
            .setNameOther(merchant.get().nameOther)
            .setNetId(merchant.get().netId)
            .setTaxId(merchant.get().taxId)
            .setContact(merchant.get().contact)
            .createMerchant();
    }

    public static Country findCountryByIsoAlpha3(List<Country> countries, String isoCodeAlpha3) throws CountryNotFoundException {
        if (!StringUtils.hasText(isoCodeAlpha3) || CollectionUtils.isEmpty(countries)) {
            throw new CountryNotFoundException("Country " + isoCodeAlpha3 + " was not found.");
        }

        Optional<Country> country = countries.stream().filter(c -> isoCodeAlpha3.equals(c.isoCodeAlpha3)).findAny();

        if (country == null || !country.isPresent()) {
            throw new CountryNotFoundException("Country " + isoCodeAlpha3 + " was not found.");
        }

        return new CountryBuilder()
            .setName(country.get().name)
            .setCapital(country.get().capital)
            .setIsoCodeAlpha2(country.get().isoCodeAlpha2)
            .setIsoCodeAlpha3(country.get().isoCodeAlpha3)
            .setIsoCodeNumeric(country.get().isoCodeNumeric)
            .setCurrency(new CurrencyBuilder().setEntity(country.get().currency.entity)
                                              .setName(country.get().currency.name)
                                              .setIsoCode(country.get().currency.isoCode)
                                              .setNumber(country.get().currency.number)
                                              .setMinorUnit(country.get().currency.minorUnit).createCurrency())
            .createCountry();
    }

    public static Currency findCurrency(List<Currency> currencies, String isoCode) throws CurrencyNotFoundException {
        if (!StringUtils.hasText(isoCode) || CollectionUtils.isEmpty(currencies)) {
            throw new CurrencyNotFoundException("Currency " + isoCode + " was not found.");
        }

        Optional<Currency> currency = currencies.stream().filter(c -> isoCode.equals(c.isoCode)).findAny();

        if (currency == null || !currency.isPresent()) {
            throw new CurrencyNotFoundException("Currency " + isoCode + " was not found.");
        }

        return new CurrencyBuilder().setEntity(currency.get().entity)
                                    .setName(currency.get().name)
                                    .setIsoCode(currency.get().isoCode)
                                    .setNumber(currency.get().number)
                                    .setMinorUnit(currency.get().minorUnit).createCurrency();
    }

    public static Mcc findMcc(List<Mcc> mccs, String code) throws MccNotFoundException {
        if (!StringUtils.hasText(code) || CollectionUtils.isEmpty(mccs)) {
            throw new MccNotFoundException("Mcc " + code + " was not found.");
        }

        Optional<Mcc> mcc = mccs.stream().filter(c -> code.equals(c.code)).findAny();

        if (mcc == null || !mcc.isPresent()) {
            throw new MccNotFoundException("Mcc " + code + " was not found.");
        }

        return new Mcc(mcc.get().code, mcc.get().description);
    }

    public static ProcessingCode findProcessingCode(List<ProcessingCode> processingCodes, String value) throws ProcessingCodeNotFoundException {
        if (!StringUtils.hasText(value) || CollectionUtils.isEmpty(processingCodes)) {
            throw new ProcessingCodeNotFoundException("Processing code " + value + " was not found.");
        }

        Optional<ProcessingCode> processingCode = processingCodes.stream().filter(c -> value.equals(c.value)).findAny();

        if (processingCode == null || !processingCode.isPresent()) {
            throw new ProcessingCodeNotFoundException("Processing code " + value + " was not found.");
        }

        return new ProcessingCode(processingCode.get().value,
                                  processingCode.get().label,
                                  processingCode.get().accountingEntryType);
    }

    public static TransactionType findTransactionType(List<TransactionType> transactionTypes, String id) throws TransactionTypeNotFoundException {
        if (!StringUtils.hasText(id) || CollectionUtils.isEmpty(transactionTypes)) {
            throw new TransactionTypeNotFoundException("Transaction type " + id + " was not found.");
        }

        Optional<TransactionType> transactionType = transactionTypes.stream().filter(c -> id.equals(c.id)).findAny();

        if (transactionType == null || !transactionType.isPresent()) {
            throw new TransactionTypeNotFoundException("Transaction type " + id + " was not found.");
        }

        return new TransactionType(transactionType.get().id,
                                   transactionType.get().mtId,
                                   transactionType.get().txnType,
                                   transactionType.get().description);
    }

    public static TransactionType findTransactionTypeByDescription(List<TransactionType> transactionTypes, String description) throws TransactionTypeNotFoundException {
        if (!StringUtils.hasText(description) || CollectionUtils.isEmpty(transactionTypes)) {
            throw new TransactionTypeNotFoundException("Could not find transaction type by description: '" + description + "'");
        }

        Optional<TransactionType> transactionType = transactionTypes.stream().filter(c -> description.equals(c.description)).findAny();

        if (transactionType == null || !transactionType.isPresent()) {
            throw new TransactionTypeNotFoundException("Could not find transaction type by description: '" + description + "', available transaction types: " + transactionTypes.toArray());
        }

        return new TransactionType(transactionType.get().id,
            transactionType.get().mtId,
            transactionType.get().txnType,
            transactionType.get().description);
    }

    public static String randomNumberInRange(int from, int to) {
        return Integer.valueOf(new SplittableRandom().nextInt(from, to)).toString();
    }

    public static Message copyOfMessage(Message message) {
        Message copy = new Message();
        copy.ehiUrl = message.ehiUrl;
        copy.scheme = message.scheme;
        copy.country = new CountryBuilder()
            .setName(message.country.name)
            .setCapital(message.country.capital)
            .setIsoCodeAlpha2(message.country.isoCodeAlpha2)
            .setIsoCodeAlpha3(message.country.isoCodeAlpha3)
            .setIsoCodeNumeric(message.country.isoCodeNumeric)
            .setCurrency(new CurrencyBuilder().setEntity(message.country.currency.entity)
                .setName(message.country.currency.name)
                .setIsoCode(message.country.currency.isoCode)
                .setNumber(message.country.currency.number)
                .setMinorUnit(message.country.currency.minorUnit).createCurrency()).createCountry();

        copy.date = message.date;

        copy.amount = new Amount();
        copy.amount.value = message.amount.value;
        copy.amount.currency = new Currency();
        copy.amount.currency.entity = message.amount.currency.entity;
        copy.amount.currency.name = message.amount.currency.name;
        copy.amount.currency.isoCode = message.amount.currency.isoCode;
        copy.amount.currency.number = message.amount.currency.number;
        copy.amount.currency.minorUnit = message.amount.currency.minorUnit;

        copy.mcc = new Mcc(message.mcc.code, message.mcc.description);

        copy.posCapability = message.posCapability;
        copy.pinEntryCapability = message.pinEntryCapability;

        copy.processingCode = new ProcessingCode(message.processingCode.value,
            message.processingCode.label,
            message.processingCode.accountingEntryType);

        copy.transactionType = new TransactionType(message.transactionType.id,
            message.transactionType.mtId,
            message.transactionType.txnType,
            message.transactionType.description);

        copy.card = new CardBuilder()
            .setPcId(message.card.pcId)
            .setNumber(message.card.number)
            .setInstitutionCode(message.card.institutionCode)
            .setCardUsageGroup(message.card.cardUsageGroup)
            .setSubBin(message.card.subBin)
            .setProductIdInPc(message.card.productIdInPc)
            .setCvv2(message.card.cvv2)
            .setCustomerReference(message.card.customerReference)
            .setExpiry(message.card.expiry)
            .createCard();

        copy.merchant = new MerchantBuilder()
            .setName(message.merchant.name)
            .setAddress(new AddressBuilder().setStreet(message.merchant.address.street)
                .setCity(message.merchant.address.city)
                .setRegion(message.merchant.address.region)
                .setPostCode(message.merchant.address.postCode)
                .setCountry(message.merchant.address.country).createAddress())
            .setPhoneNumber(message.merchant.phoneNumber)
            .setUrl(message.merchant.url)
            .setNameOther(message.merchant.nameOther)
            .setNetId(message.merchant.netId)
            .setTaxId(message.merchant.taxId)
            .setContact(message.merchant.contact)
            .createMerchant();

        copy.xmlRequest = message.xmlRequest;

        copy.response = new Response();
        copy.response.statusCode = message.response.statusCode;
        copy.response.statusMessage = message.response.statusMessage;
        copy.response.xml = message.response.xml;

        return copy;
    }

    public static Message resolveStartMessage(Message message) {
        Message startMessage = message;
        while (startMessage.parent != null) {
            startMessage = startMessage.parent;
        }
        return startMessage;
    }
}
