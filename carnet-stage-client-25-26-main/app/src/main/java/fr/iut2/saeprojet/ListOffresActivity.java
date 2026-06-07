package fr.iut2.saeprojet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.ResultatAppel;
import fr.iut2.saeprojet.entity.Offre;
import fr.iut2.saeprojet.entity.OffresResponse;

public class ListOffresActivity extends StageAppActivity {

    private class ClickableEntry implements View.OnClickListener {
        public Offre offre;
        private int no_entry;
        private TextView view;

        public ClickableEntry(TextView view, int no_entry) {
            this.no_entry = no_entry;
            this.view = view;
        }

        @Override
        public void onClick(View view) {
            if (view.isEnabled() && (offre != null)) {
                Intent intent = new Intent(ListOffresActivity.this, OffreActivity.class);
                intent.putExtra("offre", offre);
                startActivity(intent);
            }
        }
    }

    // View
    private TextView nb_offres_view;
    private TextView[] offres = new TextView[5];
    private View[] rows = new View[5]; // Les conteneurs de ligne
    private View[] separators = new View[4]; // Les lignes de séparation
    private ClickableEntry[] entries = new ClickableEntry[5];

    private ArrayList<Offre> listeOffres = new ArrayList<>();
    private int no_page = 0, nb_pages = 0;

    private Button precedent;
    private Button suivant;
    private TextView retour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_offres);

        // Init view
        nb_offres_view = findViewById(R.id.nb_offres);

        // Initialisation des tableaux de vues
        for (int i = 0; i < 5; i++) {
            // Récupération dynamique des IDs (offre1, offre2... et rowOffre1, rowOffre2...)
            int viewId = getResources().getIdentifier("offre" + (i + 1), "id", getPackageName());
            int rowId = getResources().getIdentifier("rowOffre" + (i + 1), "id", getPackageName());

            offres[i] = findViewById(viewId);
            rows[i] = findViewById(rowId);

            entries[i] = new ClickableEntry(offres[i], i);
            offres[i].setOnClickListener(entries[i]);
            if (rows[i] != null) rows[i].setOnClickListener(entries[i]);

            // Récupération des séparateurs (sep1 à sep4)
            if (i < 4) {
                int sepId = getResources().getIdentifier("sep" + (i + 1), "id", getPackageName());
                separators[i] = findViewById(sepId);
            }
        }

        precedent = findViewById(R.id.buttonPrec);
        suivant = findViewById(R.id.buttonNext);
        retour = findViewById(R.id.backSynthese);

        retour.setOnClickListener(view -> {
            Intent intent = new Intent(ListOffresActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        suivant.setOnClickListener(view -> {
            if (no_page < nb_pages) {
                no_page += 1;
                updateNavigation();
            }
        });

        precedent.setOnClickListener(view -> {
            if (no_page > 0) {
                no_page -= 1;
                updateNavigation();
            }
        });

        doGetOffreList();
    }

    private void updateNavigation() {
        suivant.setEnabled(no_page < nb_pages);
        precedent.setEnabled(no_page > 0);
        setIntituleOffres(no_page);
    }

    private void setIntituleOffres(int page) {
        for (int i = 0; i < 5; i++) {
            int index = i + 5 * page;

            if (index < listeOffres.size()) {
                // ON AFFICHE L'OFFRE
                Offre offre = listeOffres.get(index);
                String intitule = (offre.intitule.length() < 32) ?
                        offre.intitule : offre.intitule.substring(0, 28) + " ...";

                offres[i].setText(intitule);
                rows[i].setVisibility(View.VISIBLE);
                if (i < 4) separators[i].setVisibility(View.VISIBLE);

                offres[i].setEnabled(true);
                entries[i].offre = offre;
            } else {
                // ON CACHE LA LIGNE VIDE
                rows[i].setVisibility(View.GONE);
                if (i < 4) separators[i].setVisibility(View.GONE);
                entries[i].offre = null;
            }
        }
    }

    private void doGetOffreList() {
        APIClient.getOffres(this, new ResultatAppel<OffresResponse>() {
            @Override
            public void traiterResultat(OffresResponse offresResponse) {
                listeOffres.clear();
                if (offresResponse.offres != null) {
                    listeOffres.addAll(offresResponse.offres);
                }

                int total = listeOffres.size();
                nb_offres_view.setText(total + " Offres disponibles");

                // Calcul correct du nombre de pages (ex: 12 offres -> 3 pages (0,1,2))
                nb_pages = (int) Math.ceil(total / 5.0) - 1;
                if (nb_pages < 0) nb_pages = 0;

                no_page = 0;
                updateNavigation();
            }

            @Override
            public void traiterErreur() {
                Log.e("ListOffres", "Erreur API");
            }
        });
    }
}