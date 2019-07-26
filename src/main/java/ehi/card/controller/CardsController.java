/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.card.controller;

import ehi.BaseController;
import ehi.FormMode;
import ehi.alerts.AlertError;
import ehi.alerts.AlertSuccess;
import ehi.alerts.AlertUtil;
import ehi.card.Card;
import ehi.card.exception.CardNotFoundException;
import ehi.card.exception.IllegalCard;
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
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(CardsController.BASE_URI)
@SessionAttributes({CardsController.MODEL_ATTR_CARD})
public class CardsController extends BaseController {

    private static final Logger logger = LogManager.getLogger(CardsController.class);

    private static final String MODEL_ATTR_CARDS = "cards";
    public static final String MODEL_ATTR_CARD = "card";

    public static final String BASE_URI = "/ehi/data/card";

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
        model.addAttribute(VIEW, "ehi/data/card/list");
        return TEMPLATE;
    }

    @RequestMapping("/delete")
    public String delete(HttpServletRequest request, Model model,
                             @RequestParam("cardPcId") String cardPcId) {
        Settings settings = SettingsUtil.getSessionSettings(request.getSession());
        try {
            Card card = Util.findCard(settings.cards, cardPcId);
            settings.cards.removeIf(c -> c.pcId.equalsIgnoreCase(card.pcId));
            AlertUtil.addAlert(model, new AlertSuccess("Card " + card.number + " deleted."));

        } catch (CardNotFoundException e) {
            AlertUtil.addAlert(model, new AlertSuccess("Card not found."));
        }

        return showList(request, model);
    }

    @RequestMapping("/show/create")
    public String showCreateForm(Model model, Boolean isOnError) {
        model.addAttribute(MODEL_ATTR_FORM_MODE, FormMode.CREATE);
        if (isOnError == null || !isOnError){
            model.addAttribute(MODEL_ATTR_CARD, new Card());
        }
        model.addAttribute(VIEW, "ehi/data/card/form");
        return TEMPLATE;
    }

    @RequestMapping("/create")
    public String create(HttpServletRequest request, Model model, Card card) {
        Settings settings = SettingsUtil.getSessionSettings(request.getSession());
        Optional<Card> cardFound = settings.cards.stream().filter(c -> c.pcId.equalsIgnoreCase(card.pcId)).findAny();
        try {
            if (!StringUtils.hasText(card.pcId) || cardFound.isPresent()) throw new IllegalCard();
            setHardcodedValues(card);
            settings.cards.add(card);

            AlertUtil.addAlert(model, new AlertSuccess("Card " + card.number + " was created successfully."));
            return showList(request, model);
        } catch (IllegalCard ic) {
            AlertUtil.addAlert(model, new AlertError(String.format("Invalid card pc id: %s", card.pcId)));
            return showCreateForm(model, true);
        }
    }

    private void setHardcodedValues(Card card) {
        card.cardUsageGroup = "DEV-CU-001";
        card.customerReference = "1234569F";
        card.cvv2 = "918";
        card.expiry = "2206";
        card.institutionCode = "DEV";
        card.productIdInPc = "2395";
        card.subBin = "55462605";
    }

    @RequestMapping("/show/edit")
    public String showEditForm(HttpServletRequest request, Model model,
                                       @RequestParam("cardPcId") String cardPcId) {
        String template;
        Settings settings = SettingsUtil.getSessionSettings(request.getSession());
        List<Card> cards = settings.cards;
        try {
            Card card = Util.findCard(cards, cardPcId);
            model.addAttribute(MODEL_ATTR_CARD, card);

            model.addAttribute(MODEL_ATTR_FORM_MODE, FormMode.EDIT);
            model.addAttribute(VIEW, "ehi/data/card/form");
            template = TEMPLATE;

        } catch (CardNotFoundException e) {
            AlertUtil.addAlert(model, new AlertSuccess("Card not found."));
            template = showList(request, model);
        }

        return template;
    }

    @RequestMapping("/edit")
    public String editCard(HttpServletRequest request, Model model, Card card) {
        Settings settings = SettingsUtil.getSessionSettings(request.getSession());
        try {
            Card cardFound = Util.findCard(settings.cards, card.pcId);
            cardFound.number = card.number;
            setHardcodedValues(cardFound);

            settings.cards.removeIf(c -> c.pcId.equalsIgnoreCase(cardFound.pcId));
            settings.cards.add(cardFound);

            AlertUtil.addAlert(model, new AlertSuccess("Card " + card.number + " was updated successfully."));

        } catch (CardNotFoundException e) {
            AlertUtil.addAlert(model, new AlertSuccess("Card not found."));
        }

        return showList(request, model);
    }

}
