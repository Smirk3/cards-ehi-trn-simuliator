/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.message.controller.converter;

import ehi.gps.classifier.PosCapability;
import org.springframework.core.convert.converter.Converter;

public class StringToPosCapabilityConverter implements Converter<String, PosCapability> {

    @Override
    public PosCapability convert(String s) {
        return PosCapability.valueOf(s);
    }

}
