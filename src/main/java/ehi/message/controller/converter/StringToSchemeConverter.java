/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.message.controller.converter;

import ehi.gps.classifier.Scheme;
import org.springframework.core.convert.converter.Converter;


public class StringToSchemeConverter implements Converter<String, Scheme> {

    @Override
    public Scheme convert(String from) {
        return Scheme.valueOf(from);
    }

}
