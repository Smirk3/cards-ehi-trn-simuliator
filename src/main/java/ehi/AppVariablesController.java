package ehi;

import ehi.alerts.AlertError;
import ehi.alerts.AlertSuccess;
import ehi.alerts.AlertUtil;
import ehi.environment.EnvProviderException;
import ehi.environment.EnvironmentProvider;
import ehi.settings.Settings;
import ehi.settings.SettingsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/ehi/env")
public class AppVariablesController extends BaseController {

    private static final String SETTINGS = "settings";
    private static final String EHI_URL_DEFAULT_INVALID = "ehiUrlDefaultInvalid";

    @Autowired
    private EnvironmentProvider envProvider;

    @RequestMapping("")
    public String index(HttpServletRequest request, Model model) {
        Settings settings = SettingsUtil.getSessionSettings(request.getSession());
        model.addAttribute(SETTINGS, settings);
        model.addAttribute(VIEW, "ehi/env/view");
        return TEMPLATE;
    }

    @RequestMapping("/show/edit")
    public String showEditForm(HttpServletRequest request, Model model) {
        Settings settings = SettingsUtil.getSessionSettings(request.getSession());
        model.addAttribute(SETTINGS, settings);

        if (envProvider.isProviderAccessible()) {
            try {
                envProvider.authProvider("igor.zubanov", "veronika");
                model.addAttribute("environments", envProvider.getEnvironments());

            } catch (EnvProviderException e) {
                AlertUtil.addAlert(model, new AlertSuccess(e.getMessage()));
            }
        }

        model.addAttribute(VIEW, "ehi/env/edit");
        return TEMPLATE;
    }

    @RequestMapping("/edit")
    public String editContract(HttpServletRequest request, Model model,
                               @RequestParam("ehiUrlDefault") String ehiUrlDefault) {
        String template;
        Settings settings = SettingsUtil.getSessionSettings(request.getSession());

        try {
            settings.ehiUrlDefault = ehiUrlDefault;

            AlertUtil.addAlert(model, new AlertSuccess("Application variables where updated successfully."));
            template = index(request, model);

        } catch (Exception e) {
            model.addAttribute(EHI_URL_DEFAULT_INVALID, ehiUrlDefault);
            AlertUtil.addAlert(model, new AlertError("Invalid EHI url specified."));
            template = showEditForm(request, model);
        }

        return template;
    }

/*    @RequestMapping("/getEnvironments")
    public void getEnvironments(HttpServletRequest request, HttpServletResponse response) {
        List<Environment> envs = envProvider.getEnvironments();
        List<Object> list = new ArrayList<>();
        for (Environment env : envs) {
            list.add(env.name);
        }

        Gson gson = new Gson();
        String result = gson.toJson(list, LinkedList.class);

        jsonResponse(result, response);
    }*/

}
