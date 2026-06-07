package fr.iut2.saeprojet;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests fonctionnels Android (Espresso)
 *
 * Couvre :
 *   - LoginActivity       : connexion valide, invalide, champs vides
 *   - MainActivity        : affichage après connexion, navigation
 *   - ListOffresActivity  : affichage liste, pagination
 *   - OffreActivity       : détail d'une offre, bouton candidature
 *   - ListCandidaturesActivity : affichage des candidatures
 *
 * Prérequis : serveur Symfony démarré et accessible depuis l'émulateur
 *             (modifier BASE_URL dans APIClient si nécessaire)
 *
 * Lancer : ./gradlew connectedAndroidTest
 *          ou via Android Studio → Run > Run 'EspressoTest'
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EspressoTest {

    private static final String LOGIN_VALIDE    = "fontenae";
    private static final String PASSWORD_VALIDE = "eric";
    private static final String LOGIN_INVALIDE  = "utilisateur_inexistant";
    private static final String PASSWORD_INVALIDE = "mauvais_mdp";

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    // =========================================================================
    // 1. LoginActivity — Affichage du formulaire
    // =========================================================================

    /**
     * Le formulaire de connexion affiche les champs login et mot de passe.
     */
    @Test
    public void testAffichageFormulaireConnexion() {
        onView(withId(R.id.username))
                .check(matches(isDisplayed()))
                .check(matches(withHint("login")));

        onView(withId(R.id.password))
                .check(matches(isDisplayed()));

        onView(withId(R.id.login))
                .check(matches(isDisplayed()))
                .check(matches(isClickable()));
    }

    // =========================================================================
    // 2. LoginActivity — Connexion avec identifiants invalides
    // =========================================================================

    /**
     * Une connexion avec de mauvais identifiants affiche un message d'erreur
     * et reste sur l'écran de login.
     */
    @Test
    public void testConnexionAvecIdentifiantsInvalides() {
        onView(withId(R.id.username))
                .perform(clearText(), typeText(LOGIN_INVALIDE), closeSoftKeyboard());

        onView(withId(R.id.password))
                .perform(clearText(), typeText(PASSWORD_INVALIDE), closeSoftKeyboard());

        onView(withId(R.id.login)).perform(click());

        // Après un échec, on doit rester sur l'écran de login
        // Le formulaire doit toujours être visible
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }

        onView(withId(R.id.username)).check(matches(isDisplayed()));
        onView(withId(R.id.password)).check(matches(isDisplayed()));
    }

    /**
     * Une connexion avec des champs vides n'envoie pas de requête valide.
     */
    @Test
    public void testConnexionAvecChampsVides() {
        onView(withId(R.id.username)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());

        // Reste sur LoginActivity
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
        onView(withId(R.id.login)).check(matches(isDisplayed()));
    }

    // =========================================================================
    // 3. LoginActivity → MainActivity — Connexion valide
    // =========================================================================

    /**
     * Une connexion valide redirige vers MainActivity.
     * MainActivity affiche le login de l'utilisateur et les cartes navigation.
     */
    @Test
    public void testConnexionValideAfficheMainActivity() {
        seConnecter(LOGIN_VALIDE, PASSWORD_VALIDE);

        // Attend le chargement (appels API asynchrones)
        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }

        // Le login de l'utilisateur doit être affiché
        onView(withId(R.id.login)).check(matches(withText(LOGIN_VALIDE)));

        // Les deux cartes de navigation doivent être visibles
        onView(withId(R.id.offres)).check(matches(isDisplayed()));
        onView(withId(R.id.candidatures)).check(matches(isDisplayed()));

        // Le bouton de déconnexion doit être présent
        onView(withId(R.id.deconnexion)).check(matches(isDisplayed()));
    }

    /**
     * MainActivity affiche des données (nb offres, candidatures).
     */
    @Test
    public void testMainActivityAfficheStatistiques() {
        seConnecter(LOGIN_VALIDE, PASSWORD_VALIDE);
        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }

        // La date de dernière connexion doit être affichée
        onView(withId(R.id.date)).check(matches(isDisplayed()));
    }

    // =========================================================================
    // 4. MainActivity → ListOffresActivity — Navigation vers les offres
    // =========================================================================

    /**
     * Cliquer sur la carte "Offres" depuis MainActivity ouvre la liste des offres.
     */
    @Test
    public void testNavigationVersListeOffres() {
        seConnecter(LOGIN_VALIDE, PASSWORD_VALIDE);
        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }

        onView(withId(R.id.offres)).perform(click());
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }

        // La liste des offres doit être visible
        onView(withId(R.id.nb_offres)).check(matches(isDisplayed()));
    }

    /**
     * La liste des offres affiche les boutons de pagination.
     */
    @Test
    public void testListeOffresAfficheNavigation() {
        seConnecter(LOGIN_VALIDE, PASSWORD_VALIDE);
        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }

        onView(withId(R.id.offres)).perform(click());
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }

        onView(withId(R.id.buttonPrec)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonNext)).check(matches(isDisplayed()));
        onView(withId(R.id.backSynthese)).check(matches(isDisplayed()));
    }

    /**
     * Au démarrage, le bouton "Précédent" est désactivé (on est à la page 0).
     */
    @Test
    public void testBoutonPrecedentDesactiveAuDepart() {
        seConnecter(LOGIN_VALIDE, PASSWORD_VALIDE);
        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }

        onView(withId(R.id.offres)).perform(click());
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }

        onView(withId(R.id.buttonPrec)).check(matches(not(isEnabled())));
    }

    /**
     * La première offre de la liste est cliquable et ouvre le détail.
     */
    @Test
    public void testCliquerSurPremiereOffreOuvreDetail() {
        seConnecter(LOGIN_VALIDE, PASSWORD_VALIDE);
        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }

        onView(withId(R.id.offres)).perform(click());
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }

        // Clique sur la première offre
        onView(withId(R.id.offre1)).perform(click());
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }

        // L'écran de détail doit afficher le titre et le bouton retour
        onView(withId(R.id.titreOffre)).check(matches(isDisplayed()));
        onView(withId(R.id.backOffres)).check(matches(isDisplayed()));
    }

    // =========================================================================
    // 5. OffreActivity — Détail d'une offre
    // =========================================================================

    /**
     * Le détail d'une offre affiche toutes les informations attendues.
     */
    @Test
    public void testDetailOffreAfficheInformations() {
        seConnecter(LOGIN_VALIDE, PASSWORD_VALIDE);
        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }

        onView(withId(R.id.offres)).perform(click());
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }

        onView(withId(R.id.offre1)).perform(click());
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }

        onView(withId(R.id.titreOffre)).check(matches(isDisplayed()));
        onView(withId(R.id.statutOffre)).check(matches(isDisplayed()));
        onView(withId(R.id.nomEntreprise)).check(matches(isDisplayed()));
        onView(withId(R.id.nomVille)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonCandidature)).check(matches(isDisplayed()));
    }

    /**
     * Le bouton retour depuis le détail d'une offre ramène à la liste.
     */
    @Test
    public void testRetourDepuisDetailOffreVersListe() {
        seConnecter(LOGIN_VALIDE, PASSWORD_VALIDE);
        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }

        onView(withId(R.id.offres)).perform(click());
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }

        onView(withId(R.id.offre1)).perform(click());
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }

        onView(withId(R.id.backOffres)).perform(click());
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }

        // On doit être revenu sur la liste des offres
        onView(withId(R.id.nb_offres)).check(matches(isDisplayed()));
    }

    // =========================================================================
    // 6. MainActivity → ListCandidaturesActivity — Navigation candidatures
    // =========================================================================

    /**
     * Cliquer sur la carte "Candidatures" ouvre la liste des candidatures.
     */
    @Test
    public void testNavigationVersListeCandidatures() {
        seConnecter(LOGIN_VALIDE, PASSWORD_VALIDE);
        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }

        onView(withId(R.id.candidatures)).perform(click());
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }

        // La liste des candidatures doit être visible
        onView(withId(R.id.candidatures)).check(matches(isDisplayed()));
        onView(withId(R.id.retourSynthese)).check(matches(isDisplayed()));
    }

    // =========================================================================
    // 7. Déconnexion
    // =========================================================================

    /**
     * Cliquer sur "Déconnexion" affiche une boîte de dialogue de confirmation.
     */
    @Test
    public void testDeconnexionAfficheDialogue() {
        seConnecter(LOGIN_VALIDE, PASSWORD_VALIDE);
        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }

        onView(withId(R.id.deconnexion)).perform(click());

        // La boîte de dialogue doit apparaître avec les boutons Oui/Non
        onView(withText("Oui")).check(matches(isDisplayed()));
        onView(withText("Non")).check(matches(isDisplayed()));
    }

    /**
     * Annuler la déconnexion reste sur MainActivity.
     */
    @Test
    public void testAnnulerDeconnexionResteMainActivity() {
        seConnecter(LOGIN_VALIDE, PASSWORD_VALIDE);
        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }

        onView(withId(R.id.deconnexion)).perform(click());
        onView(withText("Non")).perform(click());

        // On doit rester sur MainActivity
        onView(withId(R.id.offres)).check(matches(isDisplayed()));
    }

    /**
     * Confirmer la déconnexion retourne à LoginActivity.
     */
    @Test
    public void testConfirmerDeconnexionRetourneLogin() {
        seConnecter(LOGIN_VALIDE, PASSWORD_VALIDE);
        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }

        onView(withId(R.id.deconnexion)).perform(click());
        onView(withText("Oui")).perform(click());

        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }

        // On doit être revenu sur le formulaire de login
        onView(withId(R.id.username)).check(matches(isDisplayed()));
        onView(withId(R.id.password)).check(matches(isDisplayed()));
    }

    // =========================================================================
    // Helper — connexion réutilisable
    // =========================================================================

    private void seConnecter(String login, String password) {
        onView(withId(R.id.username))
                .perform(clearText(), typeText(login), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(clearText(), typeText(password), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
    }
}
