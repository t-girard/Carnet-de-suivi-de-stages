package fr.iut2.saeprojet.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

public class Candidature implements Parcelable {

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
    @SerializedName("typeAction")
    public String typeAction;
    @SerializedName("dateAction")
    public String dateAction;
    @SerializedName("etatCandidature")
    public String etatCandidature;

    public Candidature() {
    }
    protected Candidature(Parcel in) {
        _id = in.readString();
        _type = in.readString();
        id = in.readLong();
        compteEtudiant = in.readString();
        offre = in.readString();
        typeAction = in.readString();
        dateAction = in.readString();
        etatCandidature = in.readString();
    }

    public static final Creator<Candidature> CREATOR = new Creator<Candidature>() {
        @Override
        public Candidature createFromParcel(Parcel in) {
            return new Candidature(in);
        }

        @Override
        public Candidature[] newArray(int size) {
            return new Candidature[size];
        }
    };

    public long getOffreId() {
        return Long.valueOf(offre.replace("/api/offres/", ""));
    }

    public long getEtatCandidatureId() {
        return Long.valueOf(etatCandidature.replace("/api/etat_candidatures/", ""));
    }

    public void setEtatCandidatureId(long candidatureId) {
        etatCandidature = "/api/etat_candidatures/" + candidatureId;
    }

    public void setDateAction(String dateAction) {
        this.dateAction = dateAction;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(_type);
        parcel.writeLong(id);
        parcel.writeString(compteEtudiant);
        parcel.writeString(offre);
        parcel.writeString(typeAction);
        parcel.writeString(dateAction);
        parcel.writeString(etatCandidature);
    }
}
