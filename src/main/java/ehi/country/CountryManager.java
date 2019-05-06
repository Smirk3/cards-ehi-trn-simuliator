package ehi.country;

import ehi.gps.model.Currency;
import ehi.jaxb.generated.currency.code.iso4217.generated.ISO4217;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class CountryManager {

    private static final Logger logger = LogManager.getLogger(CountryManager.class);

    @Value(value = "classpath:xml/currency-codes-ISO4217.xml")
    private Resource currencyCodesResource;

    private ISO4217 currencies;

    public CountryManager() {
        this.currencies = parseIsoCurrencies(currencyCodesResource);

    }

    private List<Currency> resolveCurrencies(Resource currencyCodesResource) {

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
