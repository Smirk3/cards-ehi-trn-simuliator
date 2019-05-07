package ehi;

import ehi.alerts.AlertError;
import ehi.alerts.AlertSuccess;
import ehi.alerts.AlertUtil;
import ehi.card.Card;
import ehi.message.Message;
import ehi.message.Util;
import ehi.settings.CardNotFoundException;
import ehi.settings.Settings;
import ehi.settings.SettingsUtil;
import ehi.template.Template;
import ehi.template.TemplateNotFoundException;
import ehi.template.TemplateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(EhiMessageController.EHI_MESSAGE_URI)
@SessionAttributes({"message", "messageNumberSelected"})
public class EhiMessageController extends BaseController {

    private static final Logger logger = LogManager.getLogger(EhiMessageController.class);

    private static final String SESSION_ATTR_MESSAGE = "message";
    private static final String MODEL_ATTR_MESSAGE = "message";
    private static final String MODEL_ATTR_CARD = "card";
    private static final String MODEL_ATTR_MESSAGE_RESPONSE = "messageResponse";

    private static final String MODEL_ATTR_TEMPLATES = "templates";

    public static final String EHI_MESSAGE_URI = "/ehi/message";

    @RequestMapping("")
    public String index(HttpServletRequest request, Model model) {
        return showTemplatesList(request, model);
    }

    @RequestMapping("/show")
    public String show(Model model, HttpServletRequest request,
                       @RequestParam(value = "ehiUrl", required = false) String ehiUrl) {
/*
        Map<String, String> params = getMessageParams(request);
        MessageRequest message = (MessageRequest) request.getSession().getAttribute(SESSION_ATTR_MESSAGE);
        message.setIbUrl(ehiUrl);

        MessageUtil.setFieldValues(message, params);

        Settings settings = SettingsUtil.getSessionSettings(request.getSession());
        try {
            Card ibPayContract = getCard(request, message.getMerchantId());
            message.setCharset(ibPayContract.getCharset());
            message.setSignatureAlgorithm(ibPayContract.getSignatureAlgorithm());
            message.setSendEmptyFields(settings.isSendEmptyFields());

            Util.signData(message, settings.getContracts());
            model.addAttribute(VIEW, "ibpay/payment/messageFormPreview");
            return TEMPLATE;

        } catch (CardNotFoundException e) {
            AlertUtil.addAlert(model, new AlertError("IBPay contract '" + message.getMerchantId()
                + "' at field VK_SND_ID was not found. You should create it at \"Contracts\" first."));
            return showEditMessageFields(model);

        } catch (PrivateKeyNotFoundException e) {
            AlertUtil.addAlert(model, new AlertError("Private key for contract " + message.getMerchantId()
                + " was not found.<br/> You can upload private keys from file or add a new one <a href=\"/ibpay/security/list\">here</a>"));
            return showEditMessageFields(model);
        }
*/
return null;
    }

/*
    @RequestMapping("/new/number")
    public String showNewMessageTypeSelector(HttpServletRequest request, Model model) {
        List<MessageTypeSelector> messageTypes = new ArrayList<>();
        messageTypes.add(new MessageTypeSelector(MessageId.MSG_1001, MessageType.PAY));
        messageTypes.add(new MessageTypeSelector(MessageId.MSG_1011, MessageType.PAY));
        messageTypes.add(new MessageTypeSelector(MessageId.MSG_1012, MessageType.PAY));
        messageTypes.add(new MessageTypeSelector(MessageId.MSG_4011, MessageType.AUTH));
        messageTypes.add(new MessageTypeSelector(MessageId.MSG_4012, MessageType.AUTH));

        model.addAttribute("messageTypes", messageTypes);
        model.addAttribute("contracts", getCards(request));

        model.addAttribute(VIEW, "ibpay/payment/messageFormTypeSelector");
        return TEMPLATE;
    }
*/

    private Collection<Card> getCards(HttpServletRequest request){
        List<Card> cards = SettingsUtil.getSessionSettings(request.getSession()).cards;
        if (CollectionUtils.isEmpty(cards)){
            return new ArrayList<>();
        } else {
            return cards;
        }
    }

    private Card getCard(HttpServletRequest request, String cardPcId) throws CardNotFoundException {
        List<Card> cards = SettingsUtil.getSessionSettings(request.getSession()).cards;
        return Util.findCard(cards, cardPcId);
    }

    @RequestMapping("/new/fields")
    public String showNewMessageFields(Model model, HttpServletRequest request) {
        String template = null;
        try {
            Card card = getCard(request, cardSelected);
            MessageId messageId = MessageId.getById(messageNumberSelected);
            MessageRequest message = MessageFactory.getMessageRequest(messageId, card);

            request.getSession().setAttribute(SESSION_ATTR_MESSAGE, message);
            model.addAttribute(MODEL_ATTR_MESSAGE, message);
            model.addAttribute(MODEL_ATTR_CARD, card);

            model.addAttribute(VIEW, "ehi/transaction/messageFormEdit");
            template = TEMPLATE;

        } catch (CardNotFoundException e) {
            AlertUtil.addAlert(model, new AlertError("IBPay contract " + cardSelected + " was not found."));
            template = showNewMessageTypeSelector(request, model);
        }

        return template;
    }

    private Message newMessage() {

    }

    @RequestMapping("/edit/fields")
    public String showEditMessageFields(Model model) {
        model.addAttribute(VIEW, "ibpay/payment/messageFormEdit");
        return TEMPLATE;
    }

    @RequestMapping("/templates/list")
    public String showTemplatesList(HttpServletRequest request, Model model) {
        List<Template> templates = SettingsUtil.getSessionSettings(request.getSession()).templates;
        if (templates == null){
            templates = new ArrayList<>();
        }
        model.addAttribute(MODEL_ATTR_TEMPLATES, templates);
        model.addAttribute(VIEW, "ehi/payment/template/templates");
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
        model.addAttribute(VIEW, "ehi/payment/template/newTemplateName");
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
            model.addAttribute(VIEW, "ehi/payment/template/templateEditForm");
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
}
