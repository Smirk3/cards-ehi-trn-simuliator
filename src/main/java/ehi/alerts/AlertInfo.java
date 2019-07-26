/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.alerts;

import static com.google.common.base.MoreObjects.firstNonNull;

/**
 * Created by igorz on 2017-02-09.
 */
public class AlertInfo extends Alert {

    private static final String TITLE = "Info";

    public AlertInfo(String text) {
        this(text, null);
    }

    public AlertInfo(String text, String title) {

        super("INFO", String.format("<b>%s</b><br/> %s", firstNonNull(title, TITLE), text));
    }

}
