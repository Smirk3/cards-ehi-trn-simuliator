/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.alerts;

import static com.google.common.base.MoreObjects.firstNonNull;

/**
 * Created by igorz on 2017-02-09.
 */
public class AlertWarning extends Alert {

    private static final String TITLE = "Warning";


    public AlertWarning(String text) {
        this(text, null);
    }

    public AlertWarning(String text, String title) {

        super("WARNING", String.format("<b>%s</b><br/> %s", firstNonNull(title, TITLE), text));
    }

}
