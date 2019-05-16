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
            throw new CardNotFoundException();
        }

        Optional<Card> card = cards.stream().filter(c -> cardPcId.equals(c.pcId)).findAny();

        if (card == null || !card.isPresent()){
            throw new CardNotFoundException();
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
            throw new MerchantNotFoundException();
        }

        Optional<Merchant> merchant = merchants.stream().filter(m -> merchantName.equals(m.name)).findAny();

        if (merchant == null || !merchant.isPresent()) {
            throw new MerchantNotFoundException();
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
            throw new CountryNotFoundException();
        }

        Optional<Country> country = countries.stream().filter(c -> isoCodeAlpha3.equals(c.isoCodeAlpha3)).findAny();

        if (country == null || !country.isPresent()) {
            throw new CountryNotFoundException();
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
            throw new CurrencyNotFoundException();
        }

        Optional<Currency> currency = currencies.stream().filter(c -> isoCode.equals(c.isoCode)).findAny();

        if (currency == null || !currency.isPresent()) {
            throw new CurrencyNotFoundException();
        }

        return new CurrencyBuilder().setEntity(currency.get().entity)
                                    .setName(currency.get().name)
                                    .setIsoCode(currency.get().isoCode)
                                    .setNumber(currency.get().number)
                                    .setMinorUnit(currency.get().minorUnit).createCurrency();
    }

    public static Mcc findMcc(List<Mcc> mccs, String code) throws MccNotFoundException {
        if (!StringUtils.hasText(code) || CollectionUtils.isEmpty(mccs)) {
            throw new MccNotFoundException();
        }

        Optional<Mcc> mcc = mccs.stream().filter(c -> code.equals(c.code)).findAny();

        if (mcc == null || !mcc.isPresent()) {
            throw new MccNotFoundException();
        }

        return new Mcc(mcc.get().code, mcc.get().description);
    }

    public static ProcessingCode findProcessingCode(List<ProcessingCode> processingCodes, String value) throws ProcessingCodeNotFoundException {
        if (!StringUtils.hasText(value) || CollectionUtils.isEmpty(processingCodes)) {
            throw new ProcessingCodeNotFoundException();
        }

        Optional<ProcessingCode> processingCode = processingCodes.stream().filter(c -> value.equals(c.value)).findAny();

        if (processingCode == null || !processingCode.isPresent()) {
            throw new ProcessingCodeNotFoundException();
        }

        return new ProcessingCode(processingCode.get().value,
                                  processingCode.get().label,
                                  processingCode.get().accountingEntryType);
    }

    public static TransactionType findTransactionType(List<TransactionType> transactionTypes, String id) throws TransactionTypeNotFoundException {
        if (!StringUtils.hasText(id) || CollectionUtils.isEmpty(transactionTypes)) {
            throw new TransactionTypeNotFoundException();
        }

        Optional<TransactionType> transactionType = transactionTypes.stream().filter(c -> id.equals(c.id)).findAny();

        if (transactionType == null || !transactionType.isPresent()) {
            throw new TransactionTypeNotFoundException();
        }

        return new TransactionType(transactionType.get().id,
                                   transactionType.get().mtId,
                                   transactionType.get().txnType,
                                   transactionType.get().description);
    }

    public static String randomNumberInRange(int from, int to) {
        return Integer.valueOf(new SplittableRandom().nextInt(from, to)).toString();
    }
}
