package ehi.alerts;

import static com.google.common.base.MoreObjects.firstNonNull;

/**
 * Created by igorz on 2017-02-10.
 */
public class AlertSuccess extends Alert {
    private static final String TITLE = "Success";

    public AlertSuccess(String text) {
        this(text, null);
    }

    public AlertSuccess(String text, String title) {

        super("SUCCESS", String.format("<b>%s</b><br/> %s", firstNonNull(title, TITLE), text));
    }
}
