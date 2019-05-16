package ehi.message.controller;

import ehi.BaseController;
import ehi.alerts.AlertError;
import ehi.alerts.AlertSuccess;
import ehi.alerts.AlertUtil;
import ehi.card.Card;
import ehi.card.exception.CardNotFoundException;
import ehi.classifier.ClassifierManager;
import ehi.country.CountryManager;
import ehi.gps.classifier.PinEntryCapability;
import ehi.gps.classifier.PosCapability;
import ehi.gps.classifier.Scheme;
import ehi.merchant.exception.MerchantNotFoundException;
import ehi.message.controller.bean.FormData;
import ehi.message.controller.bean.FormDataBuilder;
import ehi.message.exception.CountryNotFoundException;
import ehi.message.exception.CurrencyNotFoundException;
import ehi.message.exception.MccNotFoundException;
import ehi.message.exception.ProcessingCodeNotFoundException;
import ehi.message.exception.TransactionTypeNotFoundException;
import ehi.message.model.Message;
import ehi.message.service.MessageService;
import ehi.settings.Settings;
import ehi.settings.SettingsUtil;
import ehi.template.Template;
import ehi.template.TemplateNotFoundException;
import ehi.template.TemplateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ehi.message.Util.findCard;
import static ehi.message.Util.findCountryByIsoAlpha3;
import static ehi.message.Util.findCurrency;
import static ehi.message.Util.findMcc;
import static ehi.message.Util.findMerchant;
import static ehi.message.Util.findProcessingCode;
import static ehi.message.Util.findTransactionType;
import static ehi.message.Util.newMessageInstance;

@Controller
@RequestMapping(EhiMessageController.EHI_MESSAGE_URI)
@SessionAttributes({EhiMessageController.MODEL_ATTR_MESSAGE})
public class EhiMessageController extends BaseController {

    private static final Logger logger = LogManager.getLogger(EhiMessageController.class);

    public static final String MODEL_ATTR_MESSAGE = "message";

    private static final String MODEL_ATTR_TEMPLATES = "templates";

    public static final String EHI_MESSAGE_URI = "/ehi/message";
    public static final String EHI_TEMPLATES_LIST_URI = "/templates/list";
    public static final String EHI_MESSAGE_NEW = "/new/fields";
    public static final String EHI_MESSAGE_EDIT = "/edit/fields";

    @Autowired
    private CountryManager countryManager;

    @Autowired
    private ClassifierManager classifierManager;

    @Autowired
    private MessageService messageService;

    @RequestMapping("")
    public RedirectView index() {
        return new RedirectView(EHI_MESSAGE_URI + EHI_TEMPLATES_LIST_URI);
    }

    @RequestMapping("/show")
    public String show(Model model, HttpServletRequest request, Message message) {
        Settings settings = SettingsUtil.getSessionSettings(request.getSession());
        try {
            bindMessageObjects(message, settings);

            messageService.createMessageRequest(message);

            model.addAttribute(VIEW, "ehi/transaction/messageFormPreview");
            return TEMPLATE;

        } catch (InvalidObjectIdentifier e) {
            AlertUtil.addAlert(model, new AlertError(e.getMessage()));
            return showEditMessageForm(model, request);
        }
    }

    @RequestMapping(EHI_MESSAGE_NEW)
    public RedirectView showNewMessageForm(Model model) {
        model.addAttribute(MODEL_ATTR_MESSAGE, newMessageInstance());

        model.addAttribute(VIEW, "ehi/transaction/messageFormEdit");
        return new RedirectView(EHI_MESSAGE_URI + EHI_MESSAGE_EDIT);
    }

    @RequestMapping(EHI_MESSAGE_EDIT)
    public String showEditMessageForm(Model model, HttpServletRequest request) {
        Settings settings = SettingsUtil.getSessionSettings(request.getSession());
        addMessageFormData(model, settings);

        model.addAttribute(VIEW, "ehi/transaction/messageFormEdit");
        return TEMPLATE;
    }

    @RequestMapping(EHI_TEMPLATES_LIST_URI)
    public String showTemplatesList(HttpServletRequest request, Model model) {
        List<Template> templates = SettingsUtil.getSessionSettings(request.getSession()).templates;
        if (templates == null){
            templates = new ArrayList<>();
        }
        model.addAttribute(MODEL_ATTR_TEMPLATES, templates);
        model.addAttribute(VIEW, "ehi/transaction/template/templates");
        return TEMPLATE;
    }

    @RequestMapping("/templates/do")
    public String payByTemplate(HttpServletRequest request, Model model,
                                @RequestParam("templateId") String templateId) {

        return null;
        /*        Settings settings = SettingsUtil.getSessionSettings(request.getSession());
        List<Template> templates = settings.templates;
        addIbUrlDefault(model, settings);

        try {
            Template template = findTemplate(templates, templateId);
            try {
                Contract contract = getCard(request, template.getMessage().getMerchantId());
                MessageRequest message = MessageUtil.createMessageByTemplate(template, contract);

                String contractMnemo = message.getMerchantId();
                try {
                    getCard(request, contractMnemo);
                } catch (CardNotFoundException e) {
                    AlertUtil.addAlert(model, new AlertWarning("IBPay contract " + contractMnemo + " was not found."));
                }
                message.setSystemGeneratedDate();

                model.addAttribute(MODEL_ATTR_MESSAGE, message);
                request.getSession().setAttribute(SESSION_ATTR_MESSAGE, message);

                return showEditMessageFields(model);
            } catch (CardNotFoundException cnfe) {
                AlertUtil.addAlert(model, new AlertError(String.format(
                    "Could not create message by template %s because of IBPay contract not found by merchant id: %s",
                    template.getName(), template.getMessage().getMerchantId())));
                return showTemplatesList(request, model);
            }
        } catch (TemplateNotFoundException e) {
            AlertUtil.addAlert(model, new AlertWarning("IBPay template was not found."));
            return showTemplatesList(request, model);
        }*/
    }

    private void addIEhiUrlDefault(Model model, Settings settings){
        if (StringUtils.hasText(settings.ehiUrlDefault)){
            model.addAttribute("ehiUrlDefault", settings.ehiUrlDefault);
        }
    }

    @RequestMapping("/templates/delete")
    public String deleteTemplate(HttpServletRequest request, Model model,
                                 @RequestParam("templateId") String templateId) {
        if (StringUtils.hasText(templateId)) {
            List<Template> templates = SettingsUtil.getSessionSettings(request.getSession()).templates;
            for (int i = 0; i < templates.size(); i++) {
                Template template = templates.get(i);
                if (templateId.equalsIgnoreCase(template.id)) {
                    templates.remove(i);
                    break;
                }
            }
        }
        AlertUtil.addAlert(model, new AlertSuccess("Template deleted."));
        return showTemplatesList(request, model);
    }

    @RequestMapping("/templates/create/name")
    public String showCreateTemplateName(Model model) {
        model.addAttribute(VIEW, "ehi/transaction/template/newTemplateName");
        return TEMPLATE;
    }

    @RequestMapping("/templates/create")
    public String createTemplate(HttpServletRequest request, Model model,
                                 @RequestParam("templateName") String templateName,
                                 @RequestParam("templateDescription") String templateDescription) {

        List<Template> templates = SettingsUtil.getSessionSettings(request.getSession()).templates;

        String id = getNewTemplateId(templates);
        Template template = new Template();
        template.id = id;
        template.name = templateName;
        template.description = templateDescription;
        //template.setMessage((MessageRequest) request.getSession().getAttribute(SESSION_ATTR_MESSAGE));
        templates.add(template);

        AlertUtil.addAlert(model, new AlertSuccess("Template created successfully."));
        return showTemplatesList(request, model);
    }

    @RequestMapping("/templates/show/edit")
    public String showTemplateEditForm(HttpServletRequest request, Model model,
                                       @RequestParam("templateId") String templateId) {
        List<Template> templates = SettingsUtil.getSessionSettings(request.getSession()).templates;

        try {
            Template template = TemplateUtil.findTemplate(templates, templateId);
            model.addAttribute("template", template);
            model.addAttribute(VIEW, "ehi/transaction/template/templateEditForm");
            return TEMPLATE;

        } catch (TemplateNotFoundException e) {
            AlertUtil.addAlert(model, new AlertError("Template not found."));
            return showTemplatesList(request, model);
        }
    }

    @RequestMapping("/templates/edit")
    public String editTemplate(HttpServletRequest request, Model model,
                               @RequestParam("templateId") String templateId,
                               @RequestParam("description") String templateDescription) {
        List<Template> templates = SettingsUtil.getSessionSettings(request.getSession()).templates;

        try {
            Map<String, String> params = getMessageParams(request);
            //Card ibPayContract = getCard(request, params.get(FieldName.VK_SND_ID.name()));
            Template template = TemplateUtil.findTemplate(templates, templateId);
            template.description = templateDescription;

            //MessageRequest message = template.getMessage();

            //MessageUtil.setFieldValues(message, params);

            AlertUtil.addAlert(model, new AlertSuccess("Template successfully updated."));
            return showTemplatesList(request, model);

        } catch (TemplateNotFoundException e) {
            AlertUtil.addAlert(model, new AlertError("Template not found."));
            return showTemplatesList(request, model);
        /*} catch (CardNotFoundException cnfe) {
            AlertUtil.addAlert(model, new AlertError("Template not found."));
            return showTemplateEditForm(request, model, templateId);*/
        }
    }

    @RequestMapping("/templates/download")
    public void downloadTemplateTest(HttpServletRequest request, HttpServletResponse response) {
        /*MessageRequest message = (MessageRequest) request.getSession().getAttribute(SESSION_ATTR_MESSAGE);
        TestFile file = TemplateUtil.getTemplateTestFile(message);
        FileDownloadUtil.download(response, file.getName(), file.getContent());*/
    }

    private String getNewTemplateId(List<Template> templates){
        int id = 0;
        for (int i = 0; i < 50; i++) {
            id = i;
            try {
                TemplateUtil.findTemplate(templates, Integer.toString(id));
            } catch (TemplateNotFoundException e) {
                break;
            }
        }
        return Integer.toString(id);
    }

    private Map<String, String> getMessageParams(HttpServletRequest req) {
        /*String prefix = "VK_";
        Map<String, Object> paramsObj = WebUtils.getParametersStartingWith(req, prefix);
        if (req.getParameterMap().containsKey(REQ_PARAM_DEBUG)){
            paramsObj.put(REQ_PARAM_DEBUG, req.getParameterMap().get(REQ_PARAM_DEBUG));
        }*/

        Map<String, String> params = new HashMap<>();
        /*for (String key : paramsObj.keySet()) {
            String param = (paramsObj.get(key) instanceof String[]) ?
                ((String[]) paramsObj.get(key))[0] :
                ((String) paramsObj.get(key));
            params.put((REQ_PARAM_DEBUG.equals(key) ? key : prefix + key), param);
        }*/

        return params;
    }

    private void setTemplatesEhiUrl(List<Template> templates, String ehiUrl) {
        if (!CollectionUtils.isEmpty(templates)) {
            for (Template template : templates) {
                //template.getMessage().setIbUrl(ehiUrl);
            }
        }
    }

    private void bindMessageObjects(Message message, Settings settings) throws InvalidObjectIdentifier {
        try {
            message.card = findCard(settings.cards, message.card.pcId);
            message.merchant = findMerchant(settings.merchants, message.merchant.name);
            message.country = findCountryByIsoAlpha3(countryManager.getCountries(), message.country.isoCodeAlpha3);
            message.amount.currency = findCurrency(countryManager.getCurrencies(), message.amount.currency.isoCode);
            message.mcc = findMcc(classifierManager.getMccs(), message.mcc.code);
            message.processingCode = findProcessingCode(classifierManager.getProcessingCodes(), message.processingCode.value);
            message.transactionType = findTransactionType(classifierManager.getTransactionTypes(), message.transactionType.id);

        } catch (CardNotFoundException e) {
            throw new InvalidObjectIdentifier("Card '" + message.card.pcId + "' was not found.<br/> You should create it <a href=\"/ehi/data/card\">here</a>.");
        } catch (MerchantNotFoundException e) {
            throw new InvalidObjectIdentifier("Merchant " + message.merchant.name + " was not found.<br/> You should create it <a href=\"/ehi/data/merchant\">here</a>");
        } catch (CountryNotFoundException e) {
            throw new InvalidObjectIdentifier("Country " + message.country.isoCodeAlpha3 + " was not found.");
        } catch (CurrencyNotFoundException e) {
            throw new InvalidObjectIdentifier("Currency " + message.amount.currency.isoCode + " was not found.");
        } catch (MccNotFoundException e) {
            throw new InvalidObjectIdentifier("Mcc " + message.mcc.code + " was not found.");
        } catch (ProcessingCodeNotFoundException e) {
            throw new InvalidObjectIdentifier("Processing code " + message.processingCode.value + " was not found.");
        } catch (TransactionTypeNotFoundException e) {
            throw new InvalidObjectIdentifier("Transaction type " + message.transactionType.id + " was not found.");
        }
    }

    private class InvalidObjectIdentifier extends Exception {
        public InvalidObjectIdentifier(String message) {
            super(message);
        }
    }

    private void addMessageFormData(Model model, Settings settings) {
        FormData data = new FormDataBuilder()
            .setEhiUrlDefault(settings.ehiUrlDefault)
            .setSchemes(Arrays.asList(Scheme.values()))
            .setCountries(countryManager.getCountries())
            .setCurrencies(countryManager.getCurrencies())
            .setMccs(classifierManager.getMccs())
            .setPosCapabilities(Arrays.asList(PosCapability.values()))
            .setPinEntryCapabilities(Arrays.asList(PinEntryCapability.values()))
            .setProcessingCodes(classifierManager.getProcessingCodes())
            .setTransactionTypes(classifierManager.getTransactionTypes())
            .setCards(settings.cards)
            .setMerchants(settings.merchants)
            .createFormData();
        model.addAttribute("data", data);
    }

    private Collection<Card> getCards(HttpServletRequest request) {
        List<Card> cards = SettingsUtil.getSessionSettings(request.getSession()).cards;
        if (CollectionUtils.isEmpty(cards)) {
            return new ArrayList<>();
        } else {
            return cards;
        }
    }

    private Card getCard(HttpServletRequest request, String cardPcId) throws CardNotFoundException {
        List<Card> cards = SettingsUtil.getSessionSettings(request.getSession()).cards;
        return findCard(cards, cardPcId);
    }

}
