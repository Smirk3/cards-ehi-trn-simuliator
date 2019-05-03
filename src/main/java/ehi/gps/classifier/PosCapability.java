package ehi.gps.classifier;

public enum PosCapability {

    UNKNOWN_OR_NO_TERMINAL("00", "Unknown or no terminal"),
    MANUAL_KEY_ENTRY("01", "Manual Key Entry"),
    PARTIAL_MAGNETIC_STRIPE_READ("02", "Partial Magnetic Stripe Read"),
    BARCODE("03", "Barcode"),
    OCR("04", "OCR"),
    CONTACT_EMV_ICC("05", "Contact EMV ICC"),
    CONTACT_EMV_ICC_PAN_MAPPING_SERVICE_APPLIED_BY_NETWORK("06", "Contact EMV ICC (PAN mapping service applied by Network)"),
    CONTACTLESS_EMV_ICC("07", "Contactless EMV ICC"),
    CONTACTLESS_EMV_ICC_PAN_MAPPING_SERVICE_APPLIED_BY_NETWORK("08", "Contactless EMV ICC (PAN mapping service applied by Network)"),
    PAN_EXPDATE_KEY_ENTERED_BY_ACQUIRER("79", "PAN+expdate key entered by Acquirer (PAN+expdate read from Magnetic Stripe and communicated verbally to acquirer who keyed in the transaction.  Neither Track1 or Track2 will be present.)"),
    MAGNETIC_STRIPE_FALLBACK_FROM_EMV_ICC("80", "Magnetic Stripe (fallback from EMV ICC)"),
    E_COMMERCE("81", "e-commerce"),
    MAGNETIC_STRIPE_READ("90", "Magnetic Stripe Read"),
    CONTACTLESS_MAGNETIC_STRIPE("91", "Contactless Magnetic Stripe"),
    CONTACTLESS_MAGNETIC_STRIPE_PAN_MAPPING_SERVICE_APPLIED("92", "Contactless Magnetic Stripe (PAN mapping service applied)"),
    CONTACT_EMV_ICC_SOMETHING_UNRELIABLE("95", "Contact EMV ICC (something unreliable)");

    private final String label;

    private final String value;

    PosCapability(String value, String label) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }
}
