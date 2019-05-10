package ehi.settings;

import ehi.card.Card;
import ehi.card.CardBuilder;
import ehi.merchant.model.Address;
import ehi.merchant.model.AddressBuilder;
import ehi.merchant.model.Merchant;
import ehi.merchant.model.MerchantBuilder;
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

    private static final String MERCHANTS = "merchants";
    private static final String MERCHANT_NAME = "name";
    private static final String MERCHANT_PHONE_NUMBER = "phoneNumber";
    private static final String MERCHANT_URL = "url";
    private static final String MERCHANT_NAME_OTHER = "nameOther";
    private static final String MERCHANT_NET_ID = "netId";
    private static final String MERCHANT_TAX_ID = "taxId";
    private static final String MERCHANT_CONTACT = "contact";
    private static final String MERCHANT_ADDR = "address";
    private static final String MERCHANT_ADDR_STREET = "street";
    private static final String MERCHANT_ADDR_CITY = "city";
    private static final String MERCHANT_ADDR_REGION = "region";
    private static final String MERCHANT_ADDR_POSTCODE = "postCode";
    private static final String MERCHANT_ADDR_COUNTRY = "country";


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

        JsonArrayBuilder merchantsBuilder = Json.createArrayBuilder();
        for (Merchant merchant : settings.merchants){
            JsonObjectBuilder merchantBuilder = Json.createObjectBuilder();
            merchantBuilder.add(MERCHANT_NAME, merchant.name);
            merchantBuilder.add(MERCHANT_PHONE_NUMBER, merchant.phoneNumber);
            merchantBuilder.add(MERCHANT_URL, merchant.url);
            merchantBuilder.add(MERCHANT_NAME_OTHER, merchant.nameOther);
            merchantBuilder.add(MERCHANT_NET_ID, merchant.netId);
            merchantBuilder.add(MERCHANT_TAX_ID, merchant.taxId);
            merchantBuilder.add(MERCHANT_CONTACT, merchant.contact);

            JsonObjectBuilder merchAddrBuilder = Json.createObjectBuilder();
            merchAddrBuilder.add(MERCHANT_ADDR_STREET, merchant.address.street);
            merchAddrBuilder.add(MERCHANT_ADDR_CITY, merchant.address.city);
            merchAddrBuilder.add(MERCHANT_ADDR_COUNTRY, merchant.address.country);
            merchAddrBuilder.add(MERCHANT_ADDR_REGION, merchant.address.region);
            merchAddrBuilder.add(MERCHANT_ADDR_POSTCODE, merchant.address.postCode);
            merchantBuilder.add(MERCHANT_ADDR, merchAddrBuilder.build());

            merchantsBuilder.add(merchantBuilder);
        }
        settingsBuilder.add(MERCHANTS, merchantsBuilder.build());

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

        JsonArray cardsJson = settingsJson.getJsonArray(CARDS);
        List<Card> cards = new ArrayList<>();
        settings.cards = cards;
        if (cardsJson.size() > 0) {
            for (int j = 0; j < cardsJson.size(); j++) {
                JsonObject cardJson = cardsJson.getJsonObject(j);

                Card card = new CardBuilder().createCard();
                card.pcId = cardJson.getString(CARD_PC_ID);
                card.number = cardJson.getString(CARD_NUMBER);

                cards.add(card);
            }
        }

        JsonArray merchantsJson = settingsJson.getJsonArray(MERCHANTS);
        List<Merchant> merchants = new ArrayList<>();
        settings.merchants = merchants;
        if (merchantsJson.size() > 0) {
            for (int j = 0; j < merchantsJson.size(); j++) {
                JsonObject merchantJson = merchantsJson.getJsonObject(j);
                JsonObject merchAddrJson = merchantJson.getJsonObject(MERCHANT_ADDR);
                Address address = new AddressBuilder()
                    .setStreet(merchAddrJson.getString(MERCHANT_ADDR_STREET))
                    .setCity(merchAddrJson.getString(MERCHANT_ADDR_CITY))
                    .setRegion(merchAddrJson.getString(MERCHANT_ADDR_REGION))
                    .setCountry(merchAddrJson.getString(MERCHANT_ADDR_COUNTRY))
                    .setPostCode(merchAddrJson.getString(MERCHANT_ADDR_POSTCODE))
                    .createAddress();

                Merchant merchant = new MerchantBuilder()
                    .setName(merchantJson.getString(MERCHANT_NAME))
                    .setPhoneNumber(merchantJson.getString(MERCHANT_PHONE_NUMBER))
                    .setUrl(merchantJson.getString(MERCHANT_URL))
                    .setNameOther(merchantJson.getString(MERCHANT_NAME_OTHER))
                    .setNetId(merchantJson.getString(MERCHANT_NET_ID))
                    .setTaxId(merchantJson.getString(MERCHANT_TAX_ID))
                    .setContact(merchantJson.getString(MERCHANT_CONTACT))
                    .setAddress(address)
                    .createMerchant();

                merchants.add(merchant);
            }
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
            settings.merchants = new ArrayList<>();

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
