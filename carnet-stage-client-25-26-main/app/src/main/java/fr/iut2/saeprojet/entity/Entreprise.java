package fr.iut2.saeprojet.entity;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Entreprise {

    @SerializedName("@id")
    public String _id;
    @SerializedName("@type")
    public String _type;

    @SerializedName("id")
    public long id;
    @SerializedName("raisonSociale")
    public String raisonSociale;
    @SerializedName("ville")
    public String ville;
    @SerializedName("pays")
    @Nullable
    public String pays;
    @SerializedName("offres")
    public List<String> offres;
}
