package ehi.merchant.controller;

import ehi.BaseController;
import ehi.FormMode;
import ehi.alerts.AlertError;
import ehi.alerts.AlertSuccess;
import ehi.alerts.AlertUtil;
import ehi.merchant.exception.IllegalMerchant;
import ehi.merchant.exception.MerchantNotFoundException;
import ehi.merchant.model.Merchant;
import ehi.message.Util;
import ehi.settings.Settings;
import ehi.settings.SettingsUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(MerchantController.BASE_URI)
public class MerchantController extends BaseController {

    private static final Logger logger = LogManager.getLogger(MerchantController.class);

    private static final String MODEL_ATTR_MERCHANTS = "merchants";
    private static final String MODEL_ATTR_MERCHANT = "merchant";

    public static final String BASE_URI = "/ehi/data/merchant";

    @RequestMapping("")
    public String index(HttpServletRequest request, Model model) {
        return showList(request, model);
    }

    @RequestMapping("/list")
    public String showList(HttpServletRequest request, Model model) {
        Settings settings = SettingsUtil.getSessionSettings(request.getSession());
        List<Merchant> merchants;
        if (settings == null || CollectionUtils.isEmpty(settings.merchants)) {
            merchants = new ArrayList<>();
        } else {
            merchants = settings.merchants;
        }
        model.addAttribute(MODEL_ATTR_MERCHANTS, merchants);
        model.addAttribute(VIEW, "ehi/data/merchant/list");
        return TEMPLATE;
    }

    @RequestMapping("/delete")
    public String deleteMerchant(HttpServletRequest request, Model model,
                                 @RequestParam("merchantName") String merchantName) {
        Settings settings = SettingsUtil.getSessionSettings(request.getSession());
        try {
            Merchant merchant = Util.findMerchant(settings.merchants, merchantName);
            settings.merchants.removeIf(m -> m.name.equalsIgnoreCase(merchant.name));
            AlertUtil.addAlert(model, new AlertSuccess("Merchant " + merchantName + " deleted."));

        } catch (MerchantNotFoundException e) {
            AlertUtil.addAlert(model, new AlertSuccess("Merchant not found."));
        }

        return showList(request, model);
    }

    @RequestMapping("/show/create")
    public String showCreateForm(Model model, Boolean isOnError) {
        model.addAttribute(MODEL_ATTR_FORM_MODE, FormMode.CREATE);
        if (isOnError == null || !isOnError){
            model.addAttribute(MODEL_ATTR_MERCHANT, new Merchant());
        }
        model.addAttribute(VIEW, "ehi/data/merchant/form");
        return TEMPLATE;
    }

    @RequestMapping("/create")
    public String createMerchant(HttpServletRequest request, Model model, Merchant merchant) {
        Settings settings = SettingsUtil.getSessionSettings(request.getSession());
        Optional<Merchant> merchantFound = settings.merchants.stream().filter(m -> m.name.equalsIgnoreCase(merchant.name)).findAny();
        try {
            if (!StringUtils.hasText(merchant.name) || merchantFound.isPresent()) throw new IllegalMerchant();
            settings.merchants.add(merchant);

            AlertUtil.addAlert(model, new AlertSuccess("Merchant " + merchant.name + " was created successfully."));
            return showList(request, model);
        } catch (IllegalMerchant ic) {
            AlertUtil.addAlert(model, new AlertError(String.format("Invalid merchant name: %s", merchant.name)));
            return showCreateForm(model, true);
        }
    }

    @RequestMapping("/show/edit")
    public String showEditForm(HttpServletRequest request, Model model,
                               @RequestParam("merchantName") String merchantName) {
        String template;
        Settings settings = SettingsUtil.getSessionSettings(request.getSession());
        try {
            Merchant merchant = Util.findMerchant(settings.merchants, merchantName);
            model.addAttribute(MODEL_ATTR_MERCHANT, merchant);

            model.addAttribute(VIEW, "ehi/data/merchant/form");
            model.addAttribute(MODEL_ATTR_FORM_MODE, FormMode.EDIT);
            template = TEMPLATE;

        } catch (MerchantNotFoundException e) {
            AlertUtil.addAlert(model, new AlertSuccess("Merchant not found."));
            template = showList(request, model);
        }

        return template;
    }

    @RequestMapping("/edit")
    public String edit(HttpServletRequest request, Model model, Merchant merchant) {
        Settings settings = SettingsUtil.getSessionSettings(request.getSession());
        try {
            Merchant merchantFound = Util.findMerchant(settings.merchants, merchant.name);
            merchantFound.phoneNumber = merchant.phoneNumber;
            merchantFound.url = merchant.url;
            merchantFound.netId = merchant.netId;
            merchantFound.taxId = merchant.taxId;
            merchantFound.nameOther = merchant.nameOther;
            merchantFound.contact = merchant.contact;
            merchantFound.address.street = merchant.address.street;
            merchantFound.address.city = merchant.address.city;
            merchantFound.address.region = merchant.address.region;
            merchantFound.address.postCode = merchant.address.postCode;
            merchantFound.address.country = merchant.address.country;

            settings.merchants.removeIf(m -> m.name.equalsIgnoreCase(merchantFound.name));
            settings.merchants.add(merchantFound);

            AlertUtil.addAlert(model, new AlertSuccess("Merchant " + merchant.name + " was updated successfully."));

        } catch (MerchantNotFoundException e) {
            AlertUtil.addAlert(model, new AlertSuccess("Merchant not found."));
        }

        return showList(request, model);
    }
}
