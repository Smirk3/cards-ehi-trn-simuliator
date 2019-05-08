package ehi.settings;

import ehi.card.Card;
import ehi.card.CardBuilder;
import ehi.template.Template;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsUtil {

    public static final String SESSION_ATTR_SETTINGS = "ehiSettings";

    private static final String EHI_URL_DEFAULT = "ehiUrlDefault";
    private static final String CARDS = "cards";
    private static final String CARD_PC_ID = "pcId";
    private static final String CARD_NUMBER = "number";
    private static final String TEMPLATES = "templates";
    private static final String TEMPLATE_NAME = "name";
    private static final String TEMPLATE_DESCRIPTION = "description";
    private static final String TEMPLATE_REQUEST = "request";

    public static String toString(Settings settings){
        JsonObjectBuilder settingsBuilder = Json.createObjectBuilder();
        nullSafe(settingsBuilder, EHI_URL_DEFAULT, settings.ehiUrlDefault);

        JsonArrayBuilder crtsBuilder = Json.createArrayBuilder();
        for (Card card : settings.cards){
            JsonObjectBuilder crtBuilder = Json.createObjectBuilder();
            crtBuilder.add(CARD_PC_ID, card.pcId);
            crtBuilder.add(CARD_NUMBER, card.number);

            crtsBuilder.add(crtBuilder);
        }
        settingsBuilder.add(CARDS, crtsBuilder.build());

        JsonArrayBuilder tmplsBuilder = Json.createArrayBuilder();
        for (Template template : settings.templates){
            JsonObjectBuilder tmplBuilder = Json.createObjectBuilder();
            tmplBuilder.add(TEMPLATE_NAME, template.name);
            nullSafe(tmplBuilder, TEMPLATE_DESCRIPTION, template.description);

            JsonArrayBuilder messageBuilder = Json.createArrayBuilder();
/*            for (Field field : template.getMessage().getFields()){
                JsonObjectBuilder fieldBuilder = Json.createObjectBuilder();
                nullSafe(fieldBuilder, field.getName().name(), field.getValue());
                messageBuilder.add(fieldBuilder);
            }*/
            tmplBuilder.add(TEMPLATE_REQUEST, messageBuilder);

            tmplsBuilder.add(tmplBuilder);
        }
        settingsBuilder.add(TEMPLATES, tmplsBuilder.build());

        return settingsBuilder.build().toString();
    }

    public static Settings toObject(Model model, InputStream value){
        Settings settings = new Settings();
        JsonReader jsonReader = Json.createReader(value);

        JsonObject settingsJson = jsonReader.readObject();
        settings.ehiUrlDefault = settingsJson.getString(EHI_URL_DEFAULT, null);

        JsonArray crtsJson = settingsJson.getJsonArray(CARDS);
        List<Card> cards = new ArrayList<>();
        settings.cards = cards;
        for (int j = 0; j < crtsJson.size(); j++) {
            JsonObject cardJson = crtsJson.getJsonObject(j);

            Card card = new CardBuilder().createCard();
            card.pcId = cardJson.getString(CARD_PC_ID);
            card.number = cardJson.getString(CARD_NUMBER);

            cards.add(card);
        }

        JsonArray templatesJson = settingsJson.getJsonArray(TEMPLATES);
        List<Template> templates = new ArrayList<>();
        settings.templates = templates;
        for (int i = 0; i < templatesJson.size(); i++) {
            JsonObject templateJson = templatesJson.getJsonObject(i);
            Template template = new Template();
            template.id = Integer.toString(i);
            template.name = templateJson.getString(TEMPLATE_NAME);
            template.description = templateJson.getString(TEMPLATE_DESCRIPTION, null);

            JsonArray fieldsJson = templateJson.getJsonArray(TEMPLATE_REQUEST);
            Map<String, String> params = new HashMap<>();
            for (int j = 0; j < fieldsJson.size(); j++) {
                JsonObject fieldJson = fieldsJson.getJsonObject(j);
                String fieldName = fieldJson.keySet().iterator().next();
                if (fieldJson.isNull(fieldName)){
                    params.put(fieldName, null);
                } else {
                    params.put(fieldName, fieldJson.getString(fieldName));
                }
            }

/*            String msgMerchantId = params.get(VK_SND_ID.name());
            try {
                Contract contract = Util.findContract(settings.getContracts(), msgMerchantId);
                MessageRequest message = MessageFactory.getMessageRequest(params, contract, true);
                template.setMessage(message);

                JsonObject testJson = templateJson.getJsonObject(IBPAY_TEST);
                if (testJson != null) {
                    String testSuccessOnStr = testJson.getString(IBPAY_TEST_SUCCESS_ON);
                    if (StringUtils.hasText(testSuccessOnStr)) {
                        TestSuccessOn testSuccessOn = TestSuccessOn.valueOf(testSuccessOnStr);
                        template.setTest(new Test(testSuccessOn, testJson.getString(IBPAY_TEST_ERROR_MSG)));
                    }
                }

                templates.add(template);
            } catch (CardNotFoundException e) {
                AlertUtil.addAlert(model, new AlertWarning(String.format("Message template %s was not loaded because"
                        + "of IBPay contract was not found by message merchant id: %s (VK_SND_ID field)",
                        template.getName(), msgMerchantId)));
            }*/
        }

        return settings;
    }

    public static Settings getSessionSettings(HttpSession session){
        Settings settings = (Settings) session.getAttribute(SESSION_ATTR_SETTINGS);
        if (settings == null){
            settings = new Settings();
            settings.cards = new ArrayList<>();
            settings.templates = new ArrayList<>();

            setSessionSettings(session, settings);
        }
        return settings;
    }

    public static void setSessionSettings(HttpSession session, Settings settings){
        session.setAttribute(SESSION_ATTR_SETTINGS, settings);
    }

    private static void nullSafe(JsonObjectBuilder builder, String fieldName, String value){
        if (StringUtils.hasText(value)){
            builder.add(fieldName, value);
        } else {
            builder.add(fieldName, JsonValue.NULL);
        }
    }
}
