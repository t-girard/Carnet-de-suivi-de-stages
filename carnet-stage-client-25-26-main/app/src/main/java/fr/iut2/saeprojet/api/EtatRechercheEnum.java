package fr.iut2.saeprojet.api;

public enum EtatRechercheEnum {
    NON_COMMENCE("/api/etat_recherches/1"),
    CONSULTATION("/api/etat_recherches/2"),
    CANDIDATURE("/api/etat_recherches/3"),
    TERMINE("/api/etat_recherches/4");

    private String _id;
    private EtatRechercheEnum(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return this._id;
    }

    public String toString() {
        return this._id;
    }
}
