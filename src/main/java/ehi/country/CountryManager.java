package ehi.country;

import ehi.gps.model.Country;
import ehi.gps.model.CountryBuilder;
import ehi.gps.model.Currency;
import ehi.jaxb.generated.Countries;
import ehi.jaxb.generated.ISO4217;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CountryManager {

    private static final Logger logger = LogManager.getLogger(CountryManager.class);

    private ISO4217 currenciesRaw;
    private Countries countriesRaw;

    private List<Country> countries;

    public CountryManager() {
        this.currenciesRaw = parseIsoCurrencies(new ClassPathResource("xml/currency-codes-ISO4217.xml"));
        this.countriesRaw = parseIsoCountries(new ClassPathResource("xml/iso_country_codes.xml"));
        this.countries = transform(countriesRaw, currenciesRaw);
    }

    public List<Country> getCountries() {
        return countries;
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

    private static Optional<Currency> resolveCurrency(String countryName, ISO4217 currenciesRaw) {
        Optional<ISO4217.CcyTbl.CcyNtry> ccyNtry = currenciesRaw.getCcyTbl().getCcyNtry().stream()
            .filter(c -> c.getCtryNm().equalsIgnoreCase(countryName)).findAny();

        if (ccyNtry.isPresent()) {
            return Optional.of(new Currency(ccyNtry.get().getCtryNm(),
                ccyNtry.get().getCcyNm().getValue(),
                ccyNtry.get().getCcy(),
                ccyNtry.get().getCcyNbr() != null ? ccyNtry.get().getCcyNbr().toString() : null,
                ccyNtry.get().getCcyMnrUnts() != null ? Integer.valueOf(ccyNtry.get().getCcyMnrUnts()) : null));
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
            logger.error("Unable to resolve countries from resource 'classpath:xml/iso_country_codes.xml'. " + jaxbe);
            throw new RuntimeException(jaxbe);
        }
    }

    private static ISO4217 parseIsoCurrencies(Resource currencyCodesResource) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ISO4217.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (ISO4217) jaxbUnmarshaller.unmarshal(getInputStream(currencyCodesResource));

        } catch (JAXBException jaxbe) {
            logger.error("Unable to resolve currencies from resource 'classpath:xml/currency-codes-ISO4217.xml'. " + jaxbe);
            throw new RuntimeException(jaxbe);
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