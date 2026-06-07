package fr.iut2.saeprojet.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OffresConsulteesResponse {
    @SerializedName("@context")
    public String _context;
    @SerializedName("@id")
    public String _id;
    @SerializedName("@type")
    public String _type;
    @SerializedName("hydra:totalItems")
    public Integer _totalItems;
    @SerializedName("hydra:member")
    public List<OffreConsultee> offresConsultees = new ArrayList();
}
