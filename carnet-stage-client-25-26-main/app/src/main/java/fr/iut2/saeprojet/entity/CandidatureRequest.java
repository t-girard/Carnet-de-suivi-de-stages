package fr.iut2.saeprojet.entity;

import com.google.gson.annotations.SerializedName;

public class CandidatureRequest {
    @SerializedName("compteEtudiant")
    public String compteEtudiant;
    @SerializedName("offre")
    public String offre;
    @SerializedName("typeAction")
    public String typeAction;
    @SerializedName("dateAction")
    public String dateAction;
    @SerializedName("etatCandidature")
    public String etatCandidature;
}
