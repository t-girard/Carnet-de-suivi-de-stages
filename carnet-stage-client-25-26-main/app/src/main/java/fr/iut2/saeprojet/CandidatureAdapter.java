package fr.iut2.saeprojet;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.APIService;
import fr.iut2.saeprojet.entity.Candidature;
import fr.iut2.saeprojet.entity.CandidaturesResponse;
import fr.iut2.saeprojet.entity.Offre;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CandidatureAdapter extends ArrayAdapter<Candidature> {

    // API
    private APIService apiInterface;

    public CandidatureAdapter(Context mCtx, List<Candidature> candidatures) {
        super(mCtx, R.layout.template_candidature, candidatures);

        // Chargement de l'API
        apiInterface = APIClient.getAPIService();
    }

    /**
     * Remplit une ligne de la listView avec les informations de la multiplication associée
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Récupération de la multiplication
        final Candidature candidature = getItem(position);

        // Charge le template XML
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.template_candidature, parent, false);

        // Récupération des objets graphiques dans le template
        TextView intituleView = (TextView) rowView.findViewById(R.id.intitule);
        refreshMesInformations(candidature, intituleView);
        //TextView textViewDesc = (TextView) rowView.findViewById(R.id.textViewDesc);

        //
        intituleView.setText(candidature.etatCandidature);
        //textViewDesc.setText(task.getDescription());

        //
        return rowView;
    }

    private void refreshMesInformations(Candidature candidature, TextView intituleView) {

        //
        SharedPreferences sharedPref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String token = sharedPref.getString(getContext().getString(R.string.token_key), "no token");

        //
        Call<Offre> call = apiInterface.doGetOffre("Bearer " + token, candidature.getOffreId());
        call.enqueue(new Callback<Offre>() {
            @Override
            public void onResponse(Call<Offre> call, Response<Offre> response) {
                String intitule = response.body().intitule;
                if (intitule.length() > 10) {
                    intitule = response.body().intitule.substring(0, 30) + "...";
                }

                intituleView.setText(intitule);
            }

            @Override
            public void onFailure(Call<Offre> call, Throwable t) {
                call.cancel();
                Log.e("TAG",t.getMessage());

            }
        });
    }

}