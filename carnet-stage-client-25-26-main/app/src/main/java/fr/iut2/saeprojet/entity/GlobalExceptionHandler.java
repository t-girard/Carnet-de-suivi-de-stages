package fr.iut2.saeprojet.entity;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;
import androidx.annotation.NonNull;

import fr.iut2.saeprojet.LoginActivity;

public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Context context;
    private final Thread.UncaughtExceptionHandler defaultHandler;

    public GlobalExceptionHandler(Context context) {
        this.context = context;
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
        // On affiche un message d'erreur dans un thread séparé pour ne pas bloquer
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, "Une erreur inattendue est survenue. Retour au login.", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        // On attend un peu que le Toast s'affiche
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }

        // On redirige vers l'activité de Login (remplacez LoginActivity par le nom de votre classe)
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // On tue le processus qui a crashé pour repartir sur du propre
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
