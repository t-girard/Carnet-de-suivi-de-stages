package fr.iut2.saeprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.APIService;
import fr.iut2.saeprojet.api.ResultatAppel;
import fr.iut2.saeprojet.entity.Candidature;
import fr.iut2.saeprojet.entity.EtatsCandidatures;
import fr.iut2.saeprojet.entity.Offre;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CandidatureActivity extends StageAppActivity {

    //
    public static final String CANDIDADURE_KEY = "candidature_key";

    // Data
    private Candidature candidature;
    private Offre offre;

    // View
    private TextView retourCandidaturesView;
    private TextView intituleView;
    private TextView etatView;
    private TextView dateActionView;
    private Button mettreAJourView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidature);

        // Data
        candidature = getIntent().getParcelableExtra(CANDIDADURE_KEY);
        offre = getIntent().getParcelableExtra("offre");

        // Init view
        retourCandidaturesView = findViewById(R.id.retourCandidatures);
        intituleView = findViewById(R.id.intitule);
        etatView = findViewById(R.id.etat);
        dateActionView = findViewById(R.id.dateAction);
        mettreAJourView = findViewById(R.id.mettreajour);

        //
        retourCandidaturesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CandidatureActivity.this, ListCandidaturesActivity.class);
                startActivity(intent);
            }
        });

        //
        mettreAJourView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CandidatureActivity.this, CandidatureEditActivity.class);
                intent.putExtra("offre", offre);
                intent.putExtra(CandidatureEditActivity.CANDIDADURE_KEY, candidature);
                startActivity(intent);
            }
        });

        //
        refreshMesInformations(candidature, intituleView);
    }

    private void refreshMesInformations(Candidature candidature, TextView intituleView) {
        //
        etatView.setText(EtatsCandidatures.etatsCandidatureInverse.get(candidature.getEtatCandidatureId()));
        String dateBrute = candidature.dateAction; // "YYYY-MM-DDTHH:mm:00+00:00"
        if (dateBrute != null && dateBrute.contains("T")) {
            // On remplace T par un espace et on coupe au niveau du +
            String dateNettoyee = dateBrute.replace("T", " ").split("\\+")[0];
            dateActionView.setText("le " + dateNettoyee);
        } else {
            dateActionView.setText("le " + dateBrute);
        }

        APIClient.getOffre(this, candidature.getOffreId(), new ResultatAppel<Offre>() {
            @Override
            public void traiterResultat(Offre offre) {
                String intitule = offre.intitule;
                intituleView.setText(intitule);
            }

            @Override
            public void traiterErreur() {

            }
        });
    }
}