package ehi.gps.classifier;

public enum Scheme {

    VISA("Visa", "GPS Corporation Vilnius LT"),
    MASTER_CARD("MasterCard", "GPS Corporation        Vilnius       LTU");

    private final String value;

    private final String label;

    Scheme(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }
}
