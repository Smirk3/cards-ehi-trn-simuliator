package ehi.alerts;

/**
 * Created by igorz on 2017-02-09.
 */
public class AlertError extends Alert {
    
    public AlertError(String text) {
        super("ERROR", "<b>Error</b><br/> " + text);
    }
}
