package fr.iut2.saeprojet.entity;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EtatCandidature {
    @SerializedName("@id")
    public String _id;
    @SerializedName("@type")
    public String _type;

    @SerializedName("id")
    public long id;
    @SerializedName("etat")
    @Nullable
    public String etat;
    @SerializedName("descriptif")
    public String descriptif;
    @SerializedName("candidatures")
    public List<String> candidatures;
}
