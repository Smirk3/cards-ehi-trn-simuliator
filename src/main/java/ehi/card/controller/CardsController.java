package ehi.card.controller;

import ehi.BaseController;
import ehi.alerts.AlertSuccess;
import ehi.alerts.AlertUtil;
import ehi.card.Card;
import ehi.message.Util;
import ehi.settings.CardNotFoundException;
import ehi.settings.Settings;
import ehi.settings.SettingsUtil;
import org.apache.http.annotation.Contract;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(CardsController.BASE_URI)
public class CardsController extends BaseController {

    private static final Logger logger = LogManager.getLogger(CardsController.class);

    private static final String MODEL_ATTR_CARDS = "cards";

    public static final String BASE_URI = "/ehi/card";

    @RequestMapping("")
    public String index(HttpServletRequest request, Model model) {
        return showList(request, model);
    }

    @RequestMapping("/list")
    public String showList(HttpServletRequest request, Model model) {
        Settings settings = SettingsUtil.getSessionSettings(request.getSession());
        List<Card> cards;
        if (settings == null || CollectionUtils.isEmpty(settings.cards)){
            cards = new ArrayList<>();
        } else {
            cards = settings.cards;
        }
        model.addAttribute(MODEL_ATTR_CARDS, cards);
        model.addAttribute(VIEW, BASE_URI + "/list");
        return TEMPLATE;
    }

    @RequestMapping("/delete")
    public String deleteCard(HttpServletRequest request, Model model,
                                 @RequestParam("pcId") String cardPcId) {
        Settings settings = SettingsUtil.getSessionSettings(request.getSession());
        List<Card> cards = settings.cards;

        try {
            Card card = Util.findCard(settings.cards, cardPcId);
            cards.remove(card);
            AlertUtil.addAlert(model, new AlertSuccess("Card " + card.number + " deleted."));

        } catch (CardNotFoundException e) {
            AlertUtil.addAlert(model, new AlertSuccess("Card not found."));
        }

        return showList(request, model);
    }

    @RequestMapping("/show/create")
    public String showCreateTemplateName(Model model) {
        model.addAttribute(VIEW, BASE_URI+"/new");
        return TEMPLATE;
    }

    @RequestMapping("/create")
    public String createCard(HttpServletRequest request, Model model,
                                 @RequestParam("contractMnemo") String contractMnemo,
                                 @RequestParam("charset") String charset,
                                 @RequestParam("signatureAlgorithm") String signatureAlgorithm,
                                 @RequestParam("merchantId") String merchantId,
                                 @RequestParam("privateKey") String privateKey) {
        Settings settings = SettingsUtil.getSessionSettings(request.getSession());
        /*Contract contract = new Contract();
        contract.setMnemo(contractMnemo);
        contract.setCharset(charset);
        contract.setSignatureAlgorithm(signatureAlgorithm);
        contract.setMerchantId(merchantId);
        contract.setPrivateKey(privateKey);
        settings.getContracts().put(contract.getMnemo(), contract);
        try {
            SecurityKeyReader.validatePrivateKey(privateKey);

            AlertUtil.addAlert(model, new AlertSuccess("IBPay contract " + contractMnemo + " was created successfully."));
            return showList(request, model);
        } catch (IllegalPrivateKey ipk) {
            logger.error(ipk, ipk);
            AlertUtil.addAlert(model, new AlertError(String.format("Invalid private key. [%s]", ipk.getMessage())));
            return showCreateTemplateName(model);
        }*/return null;
    }

    @RequestMapping("/show/edit")
    public String showCardEditForm(HttpServletRequest request, Model model,
                                       @RequestParam("pcId") String cardPcId) {
        String template;
        Settings settings = SettingsUtil.getSessionSettings(request.getSession());
        List<Card> cards = settings.cards;
        try {
            Card card = Util.findCard(cards, cardPcId);
            model.addAttribute("card", card);

            model.addAttribute(VIEW, BASE_URI+"/edit");
            template = TEMPLATE;

        } catch (CardNotFoundException e) {
            AlertUtil.addAlert(model, new AlertSuccess("Card not found."));
            template = showList(request, model);
        }

        return template;
    }

    @RequestMapping("/edit")
    public String editCard(HttpServletRequest request, Model model,
                               @RequestParam("contractMnemo") String contractMnemo,
                               @RequestParam("charset") String charset,
                               @RequestParam("signatureAlgorithm") String signatureAlgorithm,
                               @RequestParam("merchantId") String merchantId,
                               @RequestParam("privateKey") String privateKey) {
        Settings settings = SettingsUtil.getSessionSettings(request.getSession());

        List<Card> cards = settings.cards;
        try {
            Card card = Util.findCard(cards, contractMnemo);
            //card.setCharset(charset);
            //card.setSignatureAlgorithm(signatureAlgorithm);
            //card.setMerchantId(merchantId);
            //card.setPrivateKey(privateKey);

            AlertUtil.addAlert(model, new AlertSuccess("Card " + card.number + " was updated successfully."));

        } catch (CardNotFoundException e) {
            AlertUtil.addAlert(model, new AlertSuccess("Card not found."));
        }

        return showList(request, model);
    }

}
