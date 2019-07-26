/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.country;

import ehi.gps.model.Country;
import ehi.gps.model.CountryBuilder;
import ehi.gps.model.Currency;
import ehi.gps.model.CurrencyBuilder;
import ehi.jaxb.generated.Countries;
import ehi.jaxb.generated.ISO4217;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Component
public class CountryManager {

    private static final Logger logger = LogManager.getLogger(CountryManager.class);

    private ISO4217 currenciesRaw;
    private Countries countriesRaw;

    private List<Country> countries;
    private List<Currency> currencies;

    public CountryManager() {
        this.currenciesRaw = parseIsoCurrencies(new ClassPathResource("data/xml/currency-codes-ISO4217.xml"));
        this.countriesRaw = parseIsoCountries(new ClassPathResource("data/xml/iso_country_codes.xml"));
        this.countries = transform(countriesRaw, currenciesRaw);
        this.currencies = resolveCurrencies(currenciesRaw);
    }

    public List<Country> getCountries() {
        return countries;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    private static List<Country> transform(Countries countriesRaw, ISO4217 currenciesRaw) {
        List<Country> result = new ArrayList<>();
        for (Countries.Country countryRaw : countriesRaw.getCountry()) {
            Optional<Currency> currency = resolveCurrency(countryRaw.getName(), currenciesRaw);
            if (!currency.isPresent()) {
                logger.error("Country '{}' was skipped because of currency was not found!", countryRaw.getName());
            } else {
                Country country = new CountryBuilder().setName(countryRaw.getName())
                    .setIsoCodeAlpha2(countryRaw.getAlpha2())
                    .setIsoCodeAlpha3(countryRaw.getAlpha3())
                    .setIsoCodeNumeric(countryRaw.getCountryCode().toString())
                    .setCurrency(currency.get())
                    .createCountry();
                result.add(country);
            }
        }
        return result;
    }

    private List<Currency> resolveCurrencies(ISO4217 currenciesRaw) {
        return currenciesRaw.getCcyTbl().getCcyNtry().stream()
            .map(c -> new CurrencyBuilder().setEntity(null).setName(c.getCcyNm().getValue()).setIsoCode(c.getCcy()).setNumber(c.getCcyNbr() != null ? c.getCcyNbr().toString() : null).setMinorUnit(parseMinorUnit(c.getCcyMnrUnts())).createCurrency())
            .filter(c -> StringUtils.hasText(c.isoCode))
            //distinct
            .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Currency::getIsoCode))))
            .stream()
            .sorted()
            .collect(Collectors.toList());
    }

    private static Optional<Currency> resolveCurrency(String countryName, ISO4217 currenciesRaw) {
        Optional<ISO4217.CcyTbl.CcyNtry> ccyNtry = currenciesRaw.getCcyTbl().getCcyNtry().stream()
            .filter(c -> c.getCtryNm().equalsIgnoreCase(countryName)).findAny();

        if (ccyNtry.isPresent()) {
            return Optional.of(new CurrencyBuilder().setEntity(ccyNtry.get().getCtryNm()).setName(ccyNtry.get().getCcyNm().getValue()).setIsoCode(ccyNtry.get().getCcy()).setNumber(ccyNtry.get().getCcyNbr() != null ? ccyNtry.get().getCcyNbr().toString() : null).setMinorUnit(parseMinorUnit(ccyNtry.get().getCcyMnrUnts())).createCurrency());
        } else {
            return Optional.empty();
        }
    }

    private static Countries parseIsoCountries(Resource countriesResource) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Countries.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (Countries) jaxbUnmarshaller.unmarshal(getInputStream(countriesResource));

        } catch (JAXBException jaxbe) {
            logger.error("Unable to resolve countries from resource 'classpath:classifier/xml/iso_country_codes.xml'. " + jaxbe);
            throw new RuntimeException(jaxbe);
        }
    }

    private static ISO4217 parseIsoCurrencies(Resource currencyCodesResource) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ISO4217.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (ISO4217) jaxbUnmarshaller.unmarshal(getInputStream(currencyCodesResource));

        } catch (JAXBException jaxbe) {
            logger.error("Unable to resolve currencies from resource 'classpath:classifier/xml/currency-codes-ISO4217.xml'. " + jaxbe);
            throw new RuntimeException(jaxbe);
        }
    }

    private static Integer parseMinorUnit(String value) {
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }

    private static InputStream getInputStream(Resource file) {
        try {
            return file.getInputStream();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
