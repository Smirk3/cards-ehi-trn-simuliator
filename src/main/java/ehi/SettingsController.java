package ehi;

import ehi.alerts.AlertSuccess;
import ehi.alerts.AlertUtil;
import ehi.settings.Settings;
import ehi.settings.SettingsUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

import static ehi.settings.SettingsUtil.getSessionSettings;
import static ehi.settings.SettingsUtil.setSessionSettings;

@Controller
@RequestMapping("/ehi/settings")
public class SettingsController extends BaseController {

    private static final Logger logger = LogManager.getLogger(SettingsController.class);

    @Value(value = "classpath:static/card-ehi-simulator-settings.json")
    private Resource settingsDefault;

    @RequestMapping("")
    public String index(HttpServletRequest request, Model model) {
        setSettingsDownloadEnabled(request, model);
        model.addAttribute(VIEW, "ehi/settings/upDown");
        return TEMPLATE;
    }

    @PostMapping("/upload")
    public String uploadSettings(HttpServletRequest request, Model model, @RequestParam("file") MultipartFile file) {
        useSettings(request, model, getInputStream(file));
        AlertUtil.addAlert(model, new AlertSuccess("Settings uploaded and are in use."));
        return TEMPLATE;
    }

    @RequestMapping("/download")
    public void downloadTemplates(HttpServletRequest request, HttpServletResponse response) {
        Settings settings = getSessionSettings(request.getSession());
        FileDownloadUtil.download(response, "card-ehi-simulator-settings.json", SettingsUtil.toString(settings));
    }

    @RequestMapping("/useDefaults")
    public String useDefaultSettings(HttpServletRequest request, Model model){
        useSettings(request, model, getInputStream(settingsDefault));
        AlertUtil.addAlert(model, new AlertSuccess("Default settings in use."));
        return TEMPLATE;
    }

    private void useSettings(HttpServletRequest request, Model model, InputStream settingsIs){
        Settings settings = getSettings(model, settingsIs);
        setSessionSettings(request.getSession(), settings);

        model.addAttribute(VIEW, "ehi/settings/upDown");
        setSettingsDownloadEnabled(request, model);
    }

    private void setSettingsDownloadEnabled(HttpServletRequest request, Model model) {
        boolean isSettingsDownloadEnabled = false;
        Settings settings = getSessionSettings(request.getSession());
        if (!CollectionUtils.isEmpty(settings.cards)
            || !CollectionUtils.isEmpty(settings.templates)
            || StringUtils.hasText(settings.ehiUrlDefault)) {
            isSettingsDownloadEnabled = true;
        }
        model.addAttribute("isSettingsDownloadEnabled", isSettingsDownloadEnabled);
    }

    private InputStream getInputStream(MultipartFile file){
        try {
            return file.getInputStream();
        } catch (IOException e){
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private InputStream getInputStream(Resource file){
        try {
            return file.getInputStream();
        } catch (IOException e){
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private Settings getSettings(Model model, InputStream value){
        return SettingsUtil.toObject(model, value);
    }
}
