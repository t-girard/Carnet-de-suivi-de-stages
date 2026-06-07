package fr.iut2.saeprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.APIService;
import fr.iut2.saeprojet.api.EtatCandidatureEnum;
import fr.iut2.saeprojet.api.ResultatAppel;
import fr.iut2.saeprojet.entity.Candidature;
import fr.iut2.saeprojet.entity.CandidatureRequest;
import fr.iut2.saeprojet.entity.CompteEtudiant;
import fr.iut2.saeprojet.entity.Entreprise;
import fr.iut2.saeprojet.entity.EntreprisesResponse;
import fr.iut2.saeprojet.entity.Offre;

public class OffreActivity extends StageAppActivity {
    private TextView retour;
    private Button candidater;
    private TextView intituleOffre;
    private TextView statutOffre;
    private TextView nomEntreprise;
    private TextView nomVille;
    private TextView url;

    private long offre_id = -1;
    private Offre offre = null;
    private Entreprise entreprise = null;
    private Candidature candidature = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offre);

        retour = findViewById(R.id.backOffres);
        candidater = findViewById(R.id.buttonCandidature);
        intituleOffre = findViewById(R.id.titreOffre);
        statutOffre = findViewById(R.id.statutOffre);
        nomEntreprise = findViewById(R.id.nomEntreprise);
        nomVille = findViewById(R.id.nomVille);
        url = findViewById(R.id.offreUrl);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            offre = getIntent().getParcelableExtra("offre");
            refreshOffre();
        }

        candidater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (candidature != null) {
                    executerCandidatureActivity();
                } else {
                    creerCandidature();
                }
            }
        });

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OffreActivity.this, ListOffresActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void refreshOffre() {
        intituleOffre.setText(offre.intitule);
        if (offre.urlPieceJointe != null) {
            url.setText(offre.urlPieceJointe);
        } else {
            url.setText("Pas de descriptif");
        }

        // Statut de l'offre
        statutOffre.setText(statutOffre.getText() + getEnumValue(offre.etatOffre));
        if (offre.etatOffre.equals("/api/etat_offres/1")) {
            statutOffre.setTextColor(Color.parseColor("#6FD121"));
        } else {
            statutOffre.setTextColor(Color.parseColor("#B52216"));;
        }

        // Lien vers le PDF
        refreshEntreprise(offre.entreprise);

        // Bouton candidature
        if (offre.etatOffre.equals("/api/etat_offres/1")) {
            if (offre.candidatures.size() > 0) {
                checkCandidature(offre.candidatures);
            }
        } else {
            candidater.setEnabled(false);
        }
    }

    private void refreshEntreprise(String entreprise_id) {
        APIClient.getEntreprises(this, new ResultatAppel<EntreprisesResponse>() {
            @Override
            public void traiterResultat(EntreprisesResponse liste) {
                for(Entreprise e : liste.entreprises) {
                    if (e._id.equals(entreprise_id)) {
                        OffreActivity.this.entreprise = e;
                        nomEntreprise.setText(entreprise.raisonSociale);
                        nomVille.setText(entreprise.ville);
                    }
                }
            }

            @Override
            public void traiterErreur() {

            }
        });
    }

    private void checkCandidature(List<String> candidatures) {
        APIClient.getCompteEtudiant(this, getCompteId(), new ResultatAppel<CompteEtudiant>() {
            @Override
            public void traiterResultat(CompteEtudiant compte) {
                for(String id : compte.candidatures) {
                    if (candidatures.contains(id)) {
                        refreshCandidature(id);
                        break;
                    }
                }
            }

            @Override
            public void traiterErreur() {

            }
        });
    }

    private void refreshCandidature(String _id) {
        APIClient.getCandidature(this, APIClient.getCandidatureId(_id), new ResultatAppel<Candidature>() {
            @Override
            public void traiterResultat(Candidature candid) {
                candidature = candid;
                if (candid != null) {
                    candidater.setText("Voir la candidature");
                }
            }

            @Override
            public void traiterErreur() {

            }
        });
    }

    private void creerCandidature() {
        CandidatureRequest candidatureReq = new CandidatureRequest();
        candidatureReq.compteEtudiant = getCompte_Id();
        candidatureReq.offre = offre._id;
        candidatureReq.dateAction = String.format("%1$tY-%1$tm-%1$tdT%1$tH:%1$tM:00.000Z",Calendar.getInstance().getTime());
        candidatureReq.typeAction = "Candidature à confirmer par envoi CV+lettre";
        candidatureReq.etatCandidature = EtatCandidatureEnum.OFFRE_RETENUE.get_id();
        APIClient.createCandidature(this, candidatureReq, new ResultatAppel<Candidature>() {
            @Override
            public void traiterResultat(Candidature candid) {
                candidature = candid;
                executerCandidatureActivity();
            }

            @Override
            public void traiterErreur() {

            }
        });
    }

    private void executerCandidatureActivity() {
        Intent intent = new Intent(OffreActivity.this, CandidatureActivity.class);

        intent.putExtra("offre", offre);
        intent.putExtra("candidature_key", candidature);
        startActivity(intent);
    }
}