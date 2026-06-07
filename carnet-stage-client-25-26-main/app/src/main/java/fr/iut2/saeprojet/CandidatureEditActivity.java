package fr.iut2.saeprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.APIService;
import fr.iut2.saeprojet.api.EtatCandidatureEnum;
import fr.iut2.saeprojet.api.ResultatAppel;
import fr.iut2.saeprojet.entity.Candidature;
import fr.iut2.saeprojet.entity.CandidatureRequest;
import fr.iut2.saeprojet.entity.EtatsCandidatures;
import fr.iut2.saeprojet.entity.Offre;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CandidatureEditActivity extends StageAppActivity {

    public static final String CANDIDADURE_KEY = "candidature_key";

    // Data
    private Candidature candidature;
    private Offre offre;
    private ArrayAdapter<EtatCandidatureEnum> adapter;

    // Table de correspondance pour l'affichage des enums
    private Map<EtatCandidatureEnum, String> tableNomsEtats;

    // View
    private TextView retourCandidaturesView;
    private TextView intituleView;
    private Button annulerView;
    private Button validerView;
    private Button abandonView;
    private Spinner etatsCandidatureView;
    private EditText dateActionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidature_edit);

        // Data
        candidature = getIntent().getParcelableExtra(CANDIDADURE_KEY);
        offre = getIntent().getParcelableExtra("offre");

        // Initialisation de la table de correspondance
        initTableNomsEtats();

        // Init view
        retourCandidaturesView = findViewById(R.id.retourCandidatures);
        intituleView = findViewById(R.id.intitule);
        annulerView = findViewById(R.id.annuler);
        validerView = findViewById(R.id.valider);
        abandonView = findViewById(R.id.abandonCandidature);
        etatsCandidatureView = findViewById(R.id.etatsCandidature);
        dateActionView = findViewById(R.id.dateAction);

        retourCandidaturesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        annulerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        validerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCandidature();
            }
        });

        abandonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCandidature();
            }
        });

        // Préparation de la liste des enums
        List<EtatCandidatureEnum> etats = Arrays.asList(EtatCandidatureEnum.values());

        // Création de l'ArrayAdapter avec surcharge pour utiliser la table de correspondance
        adapter = new ArrayAdapter<EtatCandidatureEnum>(this, android.R.layout.simple_spinner_item, etats) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView v = (TextView) super.getView(position, convertView, parent);
                v.setText(tableNomsEtats.get(getItem(position)));
                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView v = (TextView) super.getDropDownView(position, convertView, parent);
                v.setText(tableNomsEtats.get(getItem(position)));
                return v;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etatsCandidatureView.setAdapter(adapter);

        refreshMesInformations(intituleView);
    }

    /**
     * Remplit la Map avec les libellés à afficher pour chaque valeur de l'Enum
     */
    private void initTableNomsEtats() {
        tableNomsEtats = new HashMap<>();
        tableNomsEtats.put(EtatCandidatureEnum.OFFRE_RETENUE, "Offre retenue");
        tableNomsEtats.put(EtatCandidatureEnum.ATTENTE_REPONSE, "En attente de réponse");
        tableNomsEtats.put(EtatCandidatureEnum.REFUSEE, "Refusée");
        tableNomsEtats.put(EtatCandidatureEnum.ATTENTE_RDV, "En attente de RDV");
        tableNomsEtats.put(EtatCandidatureEnum.ATTENTE_ENTRETIEN, "En attente d'entretien");
        tableNomsEtats.put(EtatCandidatureEnum.ACCEPTEE, "Acceptée");
    }

    private void refreshMesInformations(TextView intituleView) {
        APIClient.getOffre(this, candidature.getOffreId(), new ResultatAppel<Offre>() {
            @Override
            public void traiterResultat(Offre offre) {
                String intitule = offre.intitule;
                intituleView.setText(intitule);

                // Sélection automatique de l'état actuel dans le Spinner
                EtatCandidatureEnum[] etats = EtatCandidatureEnum.values();
                for (int i = 0; i < etats.length; i++) {
                    if (etats[i].get_id().equals(candidature.etatCandidature)) {
                        etatsCandidatureView.setSelection(i);
                        break;
                    }
                }

                // Nettoyage de la date (enlève le T et le fuseau horaire)
                String dateBrute = candidature.dateAction;
                if (dateBrute != null && dateBrute.contains("T")) {
                    String dateNettoyee = dateBrute.replace("T", " ").split("\\+")[0];
                    dateActionView.setText(dateNettoyee);
                } else {
                    dateActionView.setText(dateBrute);
                }
            }

            @Override
            public void traiterErreur() {
            }
        });
    }

    private void updateCandidature() {
        CandidatureRequest candidatureReq = new CandidatureRequest();
        candidatureReq.compteEtudiant = getCompte_Id();
        candidatureReq.offre = candidature.offre;
        candidatureReq.dateAction = String.format("%1$tY-%1$tm-%1$tdT%1$tH:%1$tM:00.000Z", Calendar.getInstance().getTime());
        candidatureReq.typeAction = "Candidature à confirmer par envoi CV+lettre";

        // On récupère l'ID technique de l'enum sélectionné pour l'API
        candidatureReq.etatCandidature = ((EtatCandidatureEnum) etatsCandidatureView.getSelectedItem()).get_id();

        APIClient.updateCandidature(this, APIClient.getCandidatureId(candidature._id), candidatureReq, new ResultatAppel<Candidature>() {
            @Override
            public void traiterResultat(Candidature response) {
                Intent intent = new Intent(CandidatureEditActivity.this, ListCandidaturesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent); // Retour après mise à jour
            }

            @Override
            public void traiterErreur() {
            }
        });
    }

    private void deleteCandidature() {
        APIClient.removeCandidature(this, APIClient.getCandidatureId(candidature._id), new ResultatAppel<Candidature>() {
            @Override
            public void traiterResultat(Candidature response) {
                Intent intent = new Intent(CandidatureEditActivity.this, ListCandidaturesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void traiterErreur() {
            }
        });
    }
}