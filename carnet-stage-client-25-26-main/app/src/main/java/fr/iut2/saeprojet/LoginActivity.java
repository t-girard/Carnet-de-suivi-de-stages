package fr.iut2.saeprojet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.APIService;
import fr.iut2.saeprojet.api.ResultatAppel;
import fr.iut2.saeprojet.entity.Auth;
import fr.iut2.saeprojet.entity.CompteEtudiant;
import fr.iut2.saeprojet.entity.ComptesEtudiantsResponse;
import fr.iut2.saeprojet.entity.EtatCandidature;
import fr.iut2.saeprojet.entity.EtatCandidaturesResponse;
import fr.iut2.saeprojet.entity.EtatOffre;
import fr.iut2.saeprojet.entity.EtatOffresResponse;
import fr.iut2.saeprojet.entity.EtatRecherche;
import fr.iut2.saeprojet.entity.EtatRecherchesResponse;
import fr.iut2.saeprojet.entity.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends StageAppActivity {

    // View
    private EditText usernameView;
    private EditText passwordView;
    private Button loginView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Init view
        usernameView = findViewById(R.id.username);
        passwordView = findViewById(R.id.password);
        loginView = findViewById(R.id.login);

        //
        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    private void login() {

        String login = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        Auth auth = new Auth(login, password);

        Call<LoginResponse> call = apiInterface.login(auth);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    setAuthData(login, response.body().token);
                    getInformationEtudiant(login);
                } else {
                    // Affiche le code (401 = mauvais mdp, 404 = mauvaise URL, 500 = erreur serveur)
                    String errorDetail = "Erreur " + response.code();
                    if (response.code() == 401) errorDetail = "Identifiants incorrects";
                    if (response.code() == 404) errorDetail = "Route API introuvable";

                    Toast.makeText(LoginActivity.this, errorDetail, Toast.LENGTH_LONG).show();
                    Log.e("API_ERROR", "Status: " + response.code() + " Body: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                call.cancel();
                Log.e("TAG",t.getMessage());
            }
        });
    }

    private void getInformationEtudiant(String login) {
        String token = getToken();

        //
        Call<ComptesEtudiantsResponse> call = apiInterface.doGetCompteEtudiants("Bearer " + token);
        call.enqueue(new Callback<ComptesEtudiantsResponse>() {
            @Override
            public void onResponse(Call<ComptesEtudiantsResponse> call, Response<ComptesEtudiantsResponse> response) {

                if (response.isSuccessful()) {

                    for (CompteEtudiant compteEtudiant : response.body().compteEtudiants) {

                        if (compteEtudiant.login.equals(login)) {
                            setCompteId(compteEtudiant.id, compteEtudiant._id);
                            break;
                        }
                    }

                    //
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(LoginActivity.this, "non pas correquete(getInfos)", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ComptesEtudiantsResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                call.cancel();
                Log.e("TAG",t.getMessage());

            }
        });

        APIClient.getEtatRecherches(this, new ResultatAppel<EtatRecherchesResponse>() {
            @Override
            public void traiterResultat(EtatRecherchesResponse liste) {
                HashMap<String, String> entries = new HashMap<>();
                for(EtatRecherche etat : liste.etatRecherches) {
                    entries.put(etat._id, etat.descriptif);
                }
                LoginActivity.this.setEnumValues(entries);
            }

            @Override
            public void traiterErreur() {

            }
        });

        APIClient.getEtatOffres(this, new ResultatAppel<EtatOffresResponse>() {
            @Override
            public void traiterResultat(EtatOffresResponse liste) {
                HashMap<String, String> entries = new HashMap<>();
                for(EtatOffre etat : liste.etatOffres) {
                    entries.put(etat._id, etat.descriptif);
                }
                LoginActivity.this.setEnumValues(entries);
            }

            @Override
            public void traiterErreur() {

            }
        });

        APIClient.getEtatCandidatures(this, new ResultatAppel<EtatCandidaturesResponse>() {
            @Override
            public void traiterResultat(EtatCandidaturesResponse liste) {
                HashMap<String, String> entries = new HashMap<>();
                for(EtatCandidature etat : liste.etatCandidatures) {
                    entries.put(etat._id, etat.descriptif);
                }
                LoginActivity.this.setEnumValues(entries);
            }

            @Override
            public void traiterErreur() {

            }
        });
    }
}