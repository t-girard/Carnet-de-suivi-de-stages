package fr.iut2.saeprojet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.APIService;
import fr.iut2.saeprojet.api.ResultatAppel;
import fr.iut2.saeprojet.entity.Candidature;
import fr.iut2.saeprojet.entity.CandidaturesResponse;
import fr.iut2.saeprojet.entity.CompteEtudiant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListCandidaturesActivity extends StageAppActivity {
    // View
    private TextView retourSyntheseView;
    private ListView candidaturesView;

    // data
    private CandidatureAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_candidatures);

        // Init view
        retourSyntheseView = findViewById(R.id.retourSynthese);
        candidaturesView = findViewById(R.id.candidatures);

        // Lier l'adapter au listView
        adapter = new CandidatureAdapter(this, new ArrayList<Candidature>());
        candidaturesView.setAdapter(adapter);

        //
        retourSyntheseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListCandidaturesActivity.this, MainActivity.class);
                startActivity(intent);;
            }
        });

        candidaturesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Récupération de la tâche cliquée à l'aide de l'adapter
                Candidature candidature = adapter.getItem(position);

                //
                Intent intent = new Intent(ListCandidaturesActivity.this, CandidatureActivity.class);
                intent.putExtra(CandidatureActivity.CANDIDADURE_KEY, candidature);
                startActivity(intent);
            }
        });

        //
        refreshMesInformations();
    }

    private void refreshMesInformations() {
        APIClient.getCandidatures(this, new ResultatAppel<CandidaturesResponse>() {
            @Override
            public void traiterResultat(CandidaturesResponse candidatures) {
                // Mettre à jour l'adapter avec la liste de taches
                adapter.clear();
                adapter.addAll(candidatures.candidatures);

                // Now, notify the adapter of the change in source
                adapter.notifyDataSetChanged();
            }

            @Override
            public void traiterErreur() {

            }
        });

        APIClient.getCompteEtudiant(this, getCompteId(), new ResultatAppel<CompteEtudiant>() {
            @Override
            public void traiterResultat(CompteEtudiant compteEtudiant) {
                TextView mesOffresEnCoursView = findViewById(R.id.txtEnCours);
                mesOffresEnCoursView.setText(String.valueOf(compteEtudiant.candidatures.size()) + " " + mesOffresEnCoursView.getText().toString());
            }

            @Override
            public void traiterErreur() {
            }
        });
    }
}