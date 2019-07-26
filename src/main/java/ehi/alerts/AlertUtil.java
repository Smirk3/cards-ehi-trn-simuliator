/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.alerts;

import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igorz on 2017-02-10.
 */
public class AlertUtil {
    
    private static final String ALERTS = "alerts";
    
    public static void addAlert(Model model, Alert alert){
        List<Alert> alerts;
        if (model.containsAttribute(ALERTS)){
            alerts = (List<Alert>) model.asMap().get(ALERTS);
        } else {
            alerts = new ArrayList<>();
            model.addAttribute(ALERTS, alerts);
        }
        
        alerts.add(alert);
    }
    
}
