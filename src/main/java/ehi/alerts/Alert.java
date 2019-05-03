package ehi.alerts;

/**
 * Created by igorz on 2017-02-09.
 */
public class Alert {
    
    private String type;
 
    private String text;
    
    protected Alert(String type, String text) {
        this.type = type;
        this.text = text;
    }
    
    public String getType() {
        return type;
    }
    
    public String getText() {
        return text;
    }

}
