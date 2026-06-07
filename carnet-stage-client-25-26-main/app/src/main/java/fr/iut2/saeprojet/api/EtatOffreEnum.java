package fr.iut2.saeprojet.api;

public enum EtatOffreEnum {
    DISPO("/api/etat_offres/1"),
    INDISPO("/api/etat_offres/2"),
    EXPIREE("/api/etat_offres/3");

    private String _id;
    private EtatOffreEnum(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return this._id;
    }

    public String toString() {
        return this._id;
    }
}
