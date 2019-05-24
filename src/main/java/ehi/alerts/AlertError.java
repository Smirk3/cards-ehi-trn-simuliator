package ehi.alerts;

import static com.google.common.base.MoreObjects.firstNonNull;

/**
 * Created by igorz on 2017-02-09.
 */
public class AlertError extends Alert {

    private static final String TITLE = "Error";

    public AlertError(String text) {
        this(text, null);
    }

    public AlertError(String text, String title) {

        super("ERROR", String.format("<b>%s</b><br/> %s", firstNonNull(title, TITLE), text));
    }

}
