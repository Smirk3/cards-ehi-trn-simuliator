package ehi.alerts;

/**
 * Created by igorz on 2017-02-10.
 */
public class AlertSuccess extends Alert {
    
    public AlertSuccess(String text) {
        super("SUCCESS", "<b>Success</b><br/> " + text);
    }
}
