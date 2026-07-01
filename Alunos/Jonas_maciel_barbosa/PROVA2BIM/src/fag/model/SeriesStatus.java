package fag.model;

public enum SeriesStatus {
    RUNNING("Em transmissao"),
    ENDED("Concluida"),
    CANCELED("Cancelada"),
    TO_BE_DETERMINED("A definir"),
    IN_DEVELOPMENT("Em desenvolvimento"),
    UNKNOWN("Desconhecido");

    private final String descricaoPtBr;

    SeriesStatus(String descricaoPtBr) {
        this.descricaoPtBr = descricaoPtBr;
    }

    public String getDescricaoPtBr() {
        return descricaoPtBr;
    }

    public static SeriesStatus fromApiValue(String value) {
        if (value == null) {
            return UNKNOWN;
        }
        switch (value.trim().toLowerCase()) {
            case "running":
                return RUNNING;
            case "ended":
                return ENDED;
            case "canceled":
            case "cancelled":
                return CANCELED;
            case "to be determined":
                return TO_BE_DETERMINED;
            case "in development":
                return IN_DEVELOPMENT;
            default:
                return UNKNOWN;
        }
    }

    @Override
    public String toString() {
        return descricaoPtBr;
    }
}
