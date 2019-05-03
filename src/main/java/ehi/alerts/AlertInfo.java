package ehi.alerts;

/**
 * Created by igorz on 2017-02-09.
 */
public class AlertInfo extends Alert {
    
    public AlertInfo(String text) {
        super("INFO", "<b>Info</b><br/> " + text);
    }
}
