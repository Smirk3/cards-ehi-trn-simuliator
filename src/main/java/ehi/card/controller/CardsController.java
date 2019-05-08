package ehi.card.controller;

import ehi.BaseController;
import ehi.alerts.AlertError;
import ehi.alerts.AlertSuccess;
import ehi.alerts.AlertUtil;
import ehi.card.Card;
import ehi.card.CardBuilder;
import ehi.card.exception.IllegalCard;
import ehi.message.Util;
import ehi.card.exception.CardNotFoundException;
import ehi.settings.Settings;
import ehi.settings.SettingsUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                                 @RequestParam("pcId") String cardPcId,
                                 @RequestParam("number") String cardNumber) {
        Settings settings = SettingsUtil.getSessionSettings(request.getSession());
        Optional<Card> cardFound = settings.cards.stream().filter(c -> c.pcId.equalsIgnoreCase(cardPcId)).findAny();
        try {
            if (cardFound.isPresent()) throw new IllegalCard();
            Card card = new CardBuilder()
                            .setPcId(cardPcId)
                            .setNumber(cardNumber)
                            .createCard();
            settings.cards.add(card);

            AlertUtil.addAlert(model, new AlertSuccess("Card " + card.number + " was created successfully."));
            return showList(request, model);
        } catch (IllegalCard ic) {
            AlertUtil.addAlert(model, new AlertError(String.format("Invalid card pc id: [%s]", cardPcId)));
            return showCreateTemplateName(model);
        }
    }

    @RequestMapping("/show/edit")
    public String showCardEditForm(HttpServletRequest request, Model model,
                                       @RequestParam("cardPcId") String cardPcId) {
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
                               @RequestParam("pcId") String cardPcId,
                               @RequestParam("number") String cardNumber) {
        Settings settings = SettingsUtil.getSessionSettings(request.getSession());
        try {
            Card card = Util.findCard(settings.cards, cardPcId);
            card.number = cardNumber;

            AlertUtil.addAlert(model, new AlertSuccess("Card " + card.number + " was updated successfully."));

        } catch (CardNotFoundException e) {
            AlertUtil.addAlert(model, new AlertSuccess("Card not found."));
        }

        return showList(request, model);
    }

}
