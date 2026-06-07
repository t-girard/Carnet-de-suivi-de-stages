package fr.iut2.saeprojet.entity;

import java.util.HashMap;
import java.util.Map;

public class EtatsCandidatures {

    public static final Map<String, Long> etatsCandidature = new HashMap<String, Long>(){
        {
            put("A Envoyer", 1L);
            put("Attente Réponse", 2L);
            put("Refusée", 3L);
            put("Attente RDV", 4L);
            put("Attente Entretien", 5L);
            put("Acceptée", 6L);
        }
    };

    public static final Map<Long, String> etatsCandidatureInverse = new HashMap<Long, String>(){
        {
            put(1L, "A Envoyer");
            put(2L, "Attente Réponse");
            put(3L, "Refusée");
            put(4L, "Attente RDV");
            put(5L, "Attente Entretien");
            put(6L, "Acceptée");
        }
    };

}
