package fr.iut2.saeprojet.entity;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EtatOffre {
    @SerializedName("@id")
    public String _id;
    @SerializedName("@type")
    public String _type;

    @SerializedName("id")
    public long id;
    @SerializedName("etat")
    public String etat;
    @SerializedName("descriptif")
    @Nullable
    public String descriptif;
    @SerializedName("offres")
    public List<String> offres;
}
