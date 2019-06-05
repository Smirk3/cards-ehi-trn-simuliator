package ehi.message.controller;

import ehi.BaseController;
import ehi.alerts.AlertError;
import ehi.alerts.AlertSuccess;
import ehi.alerts.AlertUtil;
import ehi.card.exception.CardNotFoundException;
import ehi.classifier.ClassifierManager;
import ehi.country.CountryManager;
import ehi.gps.classifier.PinEntryCapability;
import ehi.gps.classifier.PosCapability;
import ehi.gps.classifier.Scheme;
import ehi.merchant.exception.MerchantNotFoundException;
import ehi.message.controller.bean.ButtonNext;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.ws.client.WebServiceIOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ehi.gps.classifier.StatusCodeMapper.STATUS_CODE_SUCCESS;
import static ehi.message.Util.copyOfMessage;
import static ehi.message.Util.findCard;
import static ehi.message.Util.findCountryByIsoAlpha3;
import static ehi.message.Util.findCurrency;
import static ehi.message.Util.findMcc;
import static ehi.message.Util.findMerchant;
import static ehi.message.Util.findProcessingCode;
import static ehi.message.Util.findTransactionType;
import static ehi.message.Util.findTransactionTypeByDescription;
import static ehi.message.Util.newMessageInstance;

@Controller
@RequestMapping(EhiMessageController.EHI_MESSAGE_URI)
@SessionAttributes({EhiMessageController.MODEL_ATTR_MESSAGE})
public class EhiMessageController extends BaseController {

    private static final Logger logger = LogManager.getLogger(EhiMessageController.class);

    public static final String MODEL_ATTR_MESSAGE = "message";

    public static final String EHI_MESSAGE_URI = "/ehi/message";
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
        return new RedirectView(EHI_MESSAGE_URI + EHI_MESSAGE_NEW);
    }

    @RequestMapping("/show")
    public String show(Model model, HttpServletRequest request, @Valid Message message) {
        Settings settings = SettingsUtil.getSessionSettings(request.getSession());
        try {
            bindMessageObjects(message, settings);
            message.xmlRequest = messageService.createRequestForNewTransaction(message);

            model.addAttribute(VIEW, "ehi/transaction/messageFormPreview");
            return TEMPLATE;

        } catch (InvalidObjectIdentifier e) {
            AlertUtil.addAlert(model, new AlertError(e.getMessage()));
            return showEditMessageForm(model, request);
        }
    }

    @RequestMapping("/do")
    public String doMessage(Model model, HttpServletRequest request, Message message) {
        try {
            doMessageRequest(model, message);
            model.addAttribute(VIEW, "ehi/transaction/messageResult");
            return TEMPLATE;

        } catch (WebServiceIOException e) {
            AlertUtil.addAlert(model, new AlertError("Could not connect to " + message.ehiUrl));
            return showEditMessageForm(model, request);
        }
    }

    @RequestMapping("/do/next")
    public String doNextMessage(Model model, Message message,
                                @RequestParam String transactionTypeId) {
        Message nextMessage = copyOfMessage(message);
        nextMessage.parent = message;
        message.child = nextMessage;

        try {
            nextMessage.transactionType = findTransactionType(classifierManager.getTransactionTypes(), transactionTypeId);
            nextMessage.amount.value = resolveAmountForNextMessage(nextMessage);
            nextMessage.response = null;
            nextMessage.xmlRequest = messageService.createRequestForSameTransaction(nextMessage);
            doMessageRequest(model, nextMessage);
            model.addAttribute(MODEL_ATTR_MESSAGE, nextMessage);

        } catch (TransactionTypeNotFoundException e) {
            AlertUtil.addAlert(model, new AlertError(e.getMessage()));
        } catch (WebServiceIOException wse) {
            AlertUtil.addAlert(model, new AlertError("Could not connect to " + message.ehiUrl));
        }
        model.addAttribute(VIEW, "ehi/transaction/messageResult");
        return TEMPLATE;
    }

    private void doMessageRequest(Model model, Message message) {
        message.response = messageService.doRequest(message.ehiUrl, message.xmlRequest);
        String text = String.format("%s: %s (%s)", message.transactionType.description, message.response.statusMessage, message.response.statusCode);
        if (STATUS_CODE_SUCCESS.equals(message.response.statusCode)) {
            model.addAttribute("notice", new AlertSuccess(text, "Processed"));
        } else {
            model.addAttribute("notice", new AlertError(text, "Refused"));
        }
        model.addAttribute("nextButtons", resolveNextButtons(message));
        model.addAttribute("messagesMainData", messageService.getMessagesMainData(message));
    }

    private List<ButtonNext> resolveNextButtons(Message message) {
        if (message == null) {
            return null;
        }

        List<ButtonNext> buttons = new ArrayList<>();
        try {
            if (message.response != null && STATUS_CODE_SUCCESS.equals(message.response.statusCode)
                && "Authorisation Request".equals(message.transactionType.description)) {
                buttons.add(createButtonNext("Authorisation Request"));
                buttons.add(createButtonNext("Automatic Authorisation Reversal"));
                buttons.add(createButtonNext("Financial Notification (First Presentment)"));
            } else if (message.response != null && STATUS_CODE_SUCCESS.equals(message.response.statusCode)
                && "Financial Notification (First Presentment)".equals(message.transactionType.description)) {
                buttons.add(createButtonNext("Financial Reversal"));
            }
        } catch (TransactionTypeNotFoundException e) {
            logger.error(e);
        }
        return buttons;
    }

    private ButtonNext createButtonNext(String transactionTypeDescription) throws TransactionTypeNotFoundException {
        ButtonNext button = new ButtonNext();
        button.transactionType = findTransactionTypeByDescription(classifierManager.getTransactionTypes(), transactionTypeDescription);
        button.label = "Do " + button.transactionType.description;
        return button;
    }

    @RequestMapping(EHI_MESSAGE_NEW)
    public RedirectView showNewMessageForm(Model model) {
        model.addAttribute(MODEL_ATTR_MESSAGE, newMessageInstance(countryManager));

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
            throw new InvalidObjectIdentifier(e.getMessage() + "<br/> You should create it <a href=\"/ehi/data/card\">here</a>.");
        } catch (MerchantNotFoundException e) {
            throw new InvalidObjectIdentifier(e.getMessage() + "<br/> You should create it <a href=\"/ehi/data/merchant\">here</a>");
        } catch (CountryNotFoundException
            | CurrencyNotFoundException
            | MccNotFoundException
            | ProcessingCodeNotFoundException
            | TransactionTypeNotFoundException e) {
            throw new InvalidObjectIdentifier(e.getMessage());
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


    private BigDecimal resolveAmountForNextMessage(Message message){
        BigDecimal amount = message.amount.value;
        if ("Authorisation Request".equals(message.transactionType.description) && "Authorisation Request".equals(message.parent.transactionType.description)) {
            if (message.getAmount().value.signum() < 0) {
                amount = message.getAmount().value.subtract(BigDecimal.ONE);
            } else {
                amount = message.getAmount().value.add(BigDecimal.ONE);
            }
        } else if (message.transactionType.description.indexOf("Authorisation Reversal") != -1
                || message.transactionType.description.indexOf("Financial Notification") != -1) {
            if (message.parent != null) {
                amount = BigDecimal.ZERO;
                Message msg = message;
                while (msg.parent != null) {
                    amount = amount.add(msg.parent.amount.value);
                    msg = msg.parent;
                }
            }
        }
        return amount;
    }

}
