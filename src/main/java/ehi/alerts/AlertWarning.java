package ehi.alerts;

/**
 * Created by igorz on 2017-02-09.
 */
public class AlertWarning extends Alert {
    
    public AlertWarning(String text) {
        super("WARNING", "<b>Warning</b><br/> " + text);
    }
}
