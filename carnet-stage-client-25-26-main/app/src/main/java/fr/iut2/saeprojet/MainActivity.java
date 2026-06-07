package fr.iut2.saeprojet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.ResultatAppel;
import fr.iut2.saeprojet.entity.Candidature;
import fr.iut2.saeprojet.entity.CandidaturesResponse;
import fr.iut2.saeprojet.entity.CompteEtudiant;
import fr.iut2.saeprojet.entity.OffresResponse;

public class MainActivity extends StageAppActivity {

    // View
    private CardView offresView;
    private CardView candidaturesView;
    private Button deconnexionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init View
        offresView = findViewById(R.id.offres);
        candidaturesView = findViewById(R.id.candidatures);
        deconnexionView = findViewById(R.id.deconnexion);

        deconnexionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                afficherDialogueDeconnexion();
            }
        });

        offresView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListOffresActivity.class);
                startActivity(intent);
            }
        });

        candidaturesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListCandidaturesActivity.class);
                startActivity(intent);
            }
        });

        //déclenchée lors d'un appui sur le bouton retour
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                afficherDialogueDeconnexion();
            }
        });

        refreshLogin();
        refreshNBOffres();
        refreshCandidatures();
    }

    /**
     * Affiche une boîte de dialogue pour confirmer la déconnexion
     */
    private void afficherDialogueDeconnexion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Voulez-vous vraiment vous déconnecter ?");

        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Redirection vers le login et nettoyage de la pile d'activités
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void refreshLogin() {
        TextView loginView = findViewById(R.id.login);
        loginView.setText(getLogin());
        refreshMesInformations();
    }

    private void refreshNBOffres() {
        APIClient.getOffres(this, new ResultatAppel<OffresResponse>() {
            @Override
            public void traiterResultat(OffresResponse offres) {
                TextView nbOffresView = findViewById(R.id.textView8);
                nbOffresView.setText(String.valueOf(offres.offres.size()) + " " + nbOffresView.getText().toString());
            }
            @Override
            public void traiterErreur() {}
        });
    }

    private void refreshMesInformations() {
        APIClient.getCompteEtudiant(this, getCompteId(), new ResultatAppel<CompteEtudiant>() {
            @Override
            public void traiterResultat(CompteEtudiant compteEtudiant) {
                TextView mesOffresConsulteesView = findViewById(R.id.textView10);
                TextView mesOffresRetenuesView = findViewById(R.id.textView11);
                TextView mesCandidaturesView = findViewById(R.id.textView12);
                TextView derniereConnexionView = findViewById(R.id.date);

                mesOffresConsulteesView.setText(String.valueOf(compteEtudiant.offreConsultees.size()) + " " + mesOffresConsulteesView.getText().toString());
                mesOffresRetenuesView.setText(String.valueOf(compteEtudiant.offreRetenues.size()) + " " + mesOffresRetenuesView.getText().toString());
                mesCandidaturesView.setText(String.valueOf(compteEtudiant.candidatures.size()) + " " + mesCandidaturesView.getText().toString());

                String dateBrute = compteEtudiant.derniereConnexion;
                if (dateBrute != null && dateBrute.contains("T")) {
                    String dateNettoyee = dateBrute.replace("T", " ").split("\\+")[0];
                    derniereConnexionView.setText(dateNettoyee);
                } else {
                    derniereConnexionView.setText(dateBrute);
                }
            }
            @Override
            public void traiterErreur() {}
        });
    }

    private void refreshCandidatures() {
        APIClient.getCandidatures(this, new ResultatAppel<CandidaturesResponse>() {
            @Override
            public void traiterResultat(CandidaturesResponse candidatures) {
                TextView nbCandidaturesView = findViewById(R.id.textView13);
                TextView nbCandidaturesRefuseesView = findViewById(R.id.textView14);

                int count = 0;
                for(Candidature c : candidatures.candidatures) {
                    if (c.etatCandidature.equals("/api/etat_candidature/3")) {
                        count ++;
                    }
                }
                nbCandidaturesRefuseesView.setText(String.valueOf(count) + " " + nbCandidaturesRefuseesView.getText().toString());
                nbCandidaturesView.setText(String.valueOf(candidatures.candidatures.size() - count) + " " + nbCandidaturesView.getText().toString());
            }
            @Override
            public void traiterErreur() {}
        });
    }
}