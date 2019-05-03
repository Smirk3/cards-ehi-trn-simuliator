package ehi;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by igorz on 2017-03-24.
 */
@Controller
@RequestMapping("/jtracker/help")
public class HelpController extends BaseController {
    
    @RequestMapping("")
    public String index(HttpServletRequest request, Model model) {
        
        model.addAttribute(VIEW, "ehi/help/welcome");
        return TEMPLATE;
    }
    
}
