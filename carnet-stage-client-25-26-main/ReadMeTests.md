Prérequis

    Backend : PHP 8.1+, Composer, extension SQLite/MySQL.

    Frontend : Node.js et npm.

    Mobile : Android Studio et un émulateur ou appareil physique configuré.

Exécution des Tests
1. Frontend — Cypress (Interface Web)

Utilisé pour tester la navigation et les scénarios utilisateurs sur l'interface d'administration.

    Note : Le serveur Symfony doit être lancé (symfony serve) avant d'exécuter ces commandes.

    Exécuter les tests en mode console (headless) :

    npm run cy:run

    Ouvrir l'interface interactive de Cypress :

    npx cypress open

2. Backend — PHPUnit (API & Sécurité)

Vérifie les points de terminaison de l'API, la génération des jetons JWT et les règles métier.

    Exécuter toute la suite de tests :

    php bin/phpunit --testdox

3. Mobile — Espresso (Application Android)

Teste l'interface utilisateur mobile, l'affichage des données et les interactions sur Android.

    Lancer les tests sur un appareil connecté ou un émulateur :

    # Windows
    .\gradlew.bat connectedAndroidTest

    # Linux / macOS
    ./gradlew connectedAndroidTest