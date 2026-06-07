package fr.iut2.saeprojet.entity;

import com.google.gson.annotations.SerializedName;

public class OffreRetenue {
    @SerializedName("@id")
    public String _id;
    @SerializedName("@type")
    public String _type;

    @SerializedName("id")
    public long id;
    @SerializedName("compteEtudiant")
    public String compteEtudiant;
    @SerializedName("offre")
    public String offre;
}
