/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.message.controller.converter;

import ehi.gps.classifier.PinEntryCapability;
import org.springframework.core.convert.converter.Converter;

public class StringToPinEntryCapabilityConverter implements Converter<String, PinEntryCapability> {

    @Override
    public PinEntryCapability convert(String s) {
        return PinEntryCapability.valueOf(s);
    }
}
