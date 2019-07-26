/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.gps.classifier;

public enum PinEntryCapability {

    UNKNOWN("0", "Unknown"),
    TERMINAL_SUPORTS_ONLINE_PIN("1", "Terminal suports Online PIN"),
    TERMINAL_DOES_NOT_SUPPORT_ONLINE_PIN("2", "Terminal does not support Online PIN"),
    TERMINAL_SUPPORTS_ONLINE_PIN_BUT_PIN_PAD_DOES_NOT_WORK_CURRENTLY("8", "Terminal supports Online PIN, but PIN PAD does not work currently");

    private final String label;

    private final String value;

    PinEntryCapability(String value, String label) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }
}
