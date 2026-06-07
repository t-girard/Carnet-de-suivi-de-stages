package fr.iut2.saeprojet.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CompteEtudiant implements Parcelable {
    @SerializedName("@id")
    public String _id;
    @SerializedName("@type")
    public String _type;
    @SerializedName("id")
    public long id;
    @SerializedName("login")
    public String login;
    @SerializedName("roles")
    public List<String> roles;
    @SerializedName("password")
    public String password;
    @SerializedName("parcours")
    public String parcours;
    @SerializedName("derniereConnexion")
    @Nullable
    public String derniereConnexion;
    @SerializedName("etatRecherche")
    public String etatRecherche;

    @SerializedName("etudiant")
    public String etudiant;
    @SerializedName("offreConsultees")
    public List<String> offreConsultees;
    @SerializedName("offreRetenues")
    public List<String> offreRetenues;
    @SerializedName("candidatures")
    public List<String> candidatures;
    @SerializedName("userIdentifier")
    public String userIdentifier;
    @SerializedName("username")
    public String username;

    protected CompteEtudiant(Parcel in) {
        _id = in.readString();
        _type = in.readString();
        id = in.readLong();
        login = in.readString();
        roles = in.createStringArrayList();
        password = in.readString();
        parcours = in.readString();
        derniereConnexion = in.readString();
        etatRecherche = in.readString();
        etudiant = in.readString();
        offreConsultees = in.createStringArrayList();
        offreRetenues = in.createStringArrayList();
        candidatures = in.createStringArrayList();
        userIdentifier = in.readString();
        username = in.readString();
    }

    public static final Creator<CompteEtudiant> CREATOR = new Creator<CompteEtudiant>() {
        @Override
        public CompteEtudiant createFromParcel(Parcel in) {
            return new CompteEtudiant(in);
        }

        @Override
        public CompteEtudiant[] newArray(int size) {
            return new CompteEtudiant[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(_type);
        parcel.writeLong(id);
        parcel.writeString(login);
        parcel.writeStringList(roles);
        parcel.writeString(password);
        parcel.writeString(parcours);
        parcel.writeString(derniereConnexion);
        parcel.writeString(etatRecherche);
        parcel.writeString(etudiant);
        parcel.writeStringList(offreConsultees);
        parcel.writeStringList(offreRetenues);
        parcel.writeStringList(candidatures);
        parcel.writeString(userIdentifier);
        parcel.writeString(username);
    }
}
