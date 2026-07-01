package fag.model;

public enum ListType {
    FAVORITES("Favoritos"),
    WATCHED("Ja Assistidas"),
    WANT_TO_WATCH("Quero Assistir");

    private final String label;

    ListType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
