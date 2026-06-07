package fr.iut2.saeprojet.api;

public enum EtatCandidatureEnum {
    OFFRE_RETENUE("/api/etat_candidatures/1"),
    ATTENTE_REPONSE("/api/etat_candidatures/2"),
    REFUSEE("/api/etat_candidatures/3"),
    ATTENTE_RDV("/api/etat_candidatures/4"),
    ATTENTE_ENTRETIEN("/api/etat_candidatures/5"),
    ACCEPTEE("/api/etat_candidatures/6");

    private String _id;
    private EtatCandidatureEnum(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return this._id;
    }

    public String toString() {
        return this._id;
    }
}
