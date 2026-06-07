package fr.iut2.saeprojet.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Offre implements Parcelable {

    @SerializedName("@id")
    public String _id;
    @SerializedName("@type")
    public String _type;
    @SerializedName("id")
    public long id;
    @SerializedName("intitule")
    public String intitule;
    @SerializedName("descriptif")
    @Nullable
    public String descriptif;
    @SerializedName("dateDepot")
    public String dateDepot;
    @SerializedName("parcours")
    @Nullable
    public String parcours;
    @SerializedName("motsCles")
    @Nullable
    public String motsCles;
    @SerializedName("urlPieceJointe")
    @Nullable
    public String urlPieceJointe;
    @SerializedName("etatOffre")
    public String etatOffre;
    @SerializedName("entreprise")
    public String entreprise;
    @SerializedName("offreConsultees")
    public List<String> offreConsultees;
    @SerializedName("offreRetenues")
    public List<String> offreRetenues;
    @SerializedName("candidatures")
    public List<String> candidatures;

    protected Offre(Parcel in) {
        _id = in.readString();
        _type = in.readString();
        id = in.readLong();
        intitule = in.readString();
        descriptif = in.readString();
        dateDepot = in.readString();
        parcours = in.readString();
        motsCles = in.readString();
        urlPieceJointe = in.readString();
        etatOffre = in.readString();
        entreprise = in.readString();
        offreConsultees = in.createStringArrayList();
        offreRetenues = in.createStringArrayList();
        candidatures = in.createStringArrayList();
    }

    public static final Creator<Offre> CREATOR = new Creator<Offre>() {
        @Override
        public Offre createFromParcel(Parcel in) {
            return new Offre(in);
        }

        @Override
        public Offre[] newArray(int size) {
            return new Offre[size];
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
        parcel.writeString(intitule);
        parcel.writeString(descriptif);
        parcel.writeString(dateDepot);
        parcel.writeString(parcours);
        parcel.writeString(motsCles);
        parcel.writeString(urlPieceJointe);
        parcel.writeString(etatOffre);
        parcel.writeString(entreprise);
        parcel.writeStringList(offreConsultees);
        parcel.writeStringList(offreRetenues);
        parcel.writeStringList(candidatures);
    }
}
