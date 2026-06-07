package fr.iut2.saeprojet.entity;

import com.google.gson.annotations.SerializedName;

public class Etudiant {

    @SerializedName("@id")
    public String _id;
    @SerializedName("@type")
    public String _type;
    @SerializedName("id")
    public long id;
    @SerializedName("numeroINE")
    public String numeroINE;
    @SerializedName("nom")
    public String nom;
    @SerializedName("prenom")
    public String prenom;
    @SerializedName("email")
    public String email;
}
