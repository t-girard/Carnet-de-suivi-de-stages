package fr.iut2.saeprojet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.APIService;
import fr.iut2.saeprojet.entity.GlobalExceptionHandler;

public class StageAppActivity extends AppCompatActivity {
    protected APIService apiInterface = null;

    public APIService getApiInterface() {
        return apiInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Chargement de l'API
        apiInterface = APIClient.getAPIService();

        // Active la capture des crashs pour toutes les activités qui héritent de celle-ci
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler(this));
    }

    private SharedPreferences getSharedPreferences() {
        return getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
    }

    public String getToken() {
        return getSharedPreferences().getString(getString(R.string.token_key), "no token");
    }

    public String getLogin() {
        return getSharedPreferences().getString(getString(R.string.login_key), "no login");
    }

    public long getCompteId() {
        return getSharedPreferences().getLong(getString(R.string.login_id_key), 0);
    }

    public String getCompte_Id() {
        return getSharedPreferences().getString(getString(R.string.login_id_path), "");
    }

    protected void setCompteId(long id, String _id) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putLong(getString(R.string.login_id_key), id);
        editor.putString(getString(R.string.login_id_path), _id);
        editor.apply();
    }

    protected void setAuthData(String login, String token) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(getString(R.string.token_key), token);
        editor.putString(getString(R.string.login_key), login);
        editor.apply();
    }

    protected void setEnumValues(HashMap<String, String> entries) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();

        for(String key : entries.keySet()) {
            editor.putString(key, entries.get(key));
        }

        editor.apply();
    }

    protected String getEnumValue(String key) {
        SharedPreferences prefs = getSharedPreferences();

        return prefs.getString(key, "");
    }
}
