package ehi.message.controller.converter;

import ehi.gps.classifier.Scheme;
import org.springframework.core.convert.converter.Converter;


public class StringToSchemeConverter implements Converter<String, Scheme> {

    @Override
    public Scheme convert(String from) {
        return Scheme.valueOf(from);
    }

}
