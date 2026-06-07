package fr.iut2.saeprojet.api;


import fr.iut2.saeprojet.entity.Auth;
import fr.iut2.saeprojet.entity.Candidature;
import fr.iut2.saeprojet.entity.CandidatureRequest;
import fr.iut2.saeprojet.entity.CandidaturesResponse;
import fr.iut2.saeprojet.entity.CompteEtudiant;
import fr.iut2.saeprojet.entity.Entreprise;
import fr.iut2.saeprojet.entity.EntreprisesResponse;
import fr.iut2.saeprojet.entity.EtatCandidature;
import fr.iut2.saeprojet.entity.EtatCandidaturesResponse;
import fr.iut2.saeprojet.entity.EtatOffre;
import fr.iut2.saeprojet.entity.EtatOffresResponse;
import fr.iut2.saeprojet.entity.EtatRecherche;
import fr.iut2.saeprojet.entity.EtatRecherchesResponse;
import fr.iut2.saeprojet.entity.Etudiant;
import fr.iut2.saeprojet.entity.EtudiantsResponse;
import fr.iut2.saeprojet.entity.LoginResponse;
import fr.iut2.saeprojet.entity.Offre;
import fr.iut2.saeprojet.entity.OffreRetenue;
import fr.iut2.saeprojet.entity.OffresRetenuesResponse;
import fr.iut2.saeprojet.entity.OffreConsultee;
import fr.iut2.saeprojet.entity.OffresConsulteesResponse;
import fr.iut2.saeprojet.entity.OffresResponse;
import fr.iut2.saeprojet.entity.ComptesEtudiantsResponse;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {

    @POST("/api/auth")
    Call<LoginResponse> login(@Body Auth auth);

    // Comptes étudiant
    @GET("/api/compte_etudiants")
    Call<ComptesEtudiantsResponse> doGetCompteEtudiants(@Header("Authorization") String token);
    @GET("/api/compte_etudiants/{id}")
    Call<CompteEtudiant> doGetCompteEtudiant(@Header("Authorization") String token, @Path("id") long id);

    // Etudiants
    @GET("/api/etudiants")
    Call<EtudiantsResponse> doGetEtudiants(@Header("Authorization") String token);
    @GET("/api/etudiants/{id}")
    Call<Etudiant> doGetEtudiant(@Header("Authorization") String token, @Path("id") long id);

    // Etat des recherches
    @GET("/api/etat_recherches")
    Call<EtatRecherchesResponse> doGetEtatRecherches(@Header("Authorization") String token);
    @GET("/api/etat_recherches/{id}")
    Call<EtatRecherche> doGetEtatRecherche(@Header("Authorization") String token, @Path("id") long i);

    // Offres
    @GET("/api/offres")
    Call<OffresResponse> doGetOffres(@Header("Authorization") String token);
    @GET("/api/offres/{id}")
    Call<Offre> doGetOffre(@Header("Authorization") String token, @Path("id") long i);

    // Etat Offre
    @GET("/api/etat_offres")
    Call<EtatOffresResponse> doGetEtatsOffres(@Header("Authorization") String token);
    @GET("/api/etat_offres/{id}")
    Call<EtatOffre> doGetEtatOffre(@Header("Authorization") String token, @Path("id") long i);

    // Entreprises
    @GET("/api/entreprises")
    Call<EntreprisesResponse> doGetEntreprises(@Header("Authorization") String token);
    @GET("/api/entreprises/{id}")
    Call<Entreprise> doGetEntreprise(@Header("Authorization") String token, @Path("id") long i);

    // Offres consultees
    @GET("/api/offre_consultees")
    Call<OffresConsulteesResponse> doGetOffresConsultees(@Header("Authorization") String token);
    @GET("/api/offre_consultees/{id}")
    Call<OffreConsultee> doGetOffreConsultee(@Header("Authorization") String token, @Path("id") long i);

    // Offres retenues
    @GET("/api/offre_retenues")
    Call<OffresRetenuesResponse> doGetOffresRetenues(@Header("Authorization") String token);
    @GET("/api/offre_retenues/{id}")
    Call<OffreRetenue> doGetOffreRetenue(@Header("Authorization") String token, @Path("id") long i);

    // Candidatures
    @GET("/api/candidatures")
    Call<CandidaturesResponse> doGetCandidatures(@Header("Authorization") String token);
    @POST("/api/candidatures")
    Call<Candidature> doCreateCandidature(@Header("Authorization") String token, @Body CandidatureRequest candidature);
    @GET("/api/candidatures/{id}")
    Call<Candidature> doGetCandidature(@Header("Authorization") String token, @Path("id") long i);
    @PUT("/api/candidatures/{id}")
    Call<Candidature> doUpdateCandidature(@Header("Authorization") String token, @Path("id") long id, @Body CandidatureRequest candidature);
    @DELETE("/api/candidatures/{id}")
    Call<Candidature> doRemoveCandidature(@Header("Authorization") String token, @Path("id") long id);

    // Etat candidatures
    @GET("/api/etat_candidatures")
    Call<EtatCandidaturesResponse> doGetEtatCandidatures(@Header("Authorization") String token);
    @GET("/api/etat_candidatures/{id}")
    Call<EtatCandidature> doGetEtatCandidature(@Header("Authorization") String token, @Path("id") long i);
}