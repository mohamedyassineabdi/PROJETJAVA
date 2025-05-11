# 🎟️ Application de Billetterie en Ligne - Cinéma (Java)

## 🧾 Description

Cette application de billetterie en ligne permet aux clients de réserver des places de cinéma et aux administrateurs de gérer les films, salles, utilisateurs et séances.  
Elle est développée en Java avec une interface graphique en **Java Swing**, et connectée à une base de données MySQL.

---

## 👥 Acteurs & Fonctionnalités

### 1. 👤 Client (Utilisateur)

- Créer un compte et s’authentifier pour accéder à son espace personnel.
- Rechercher un film avec filtres (par nom, genre...).
- Réserver une ou plusieurs places pour une séance (film, salle, date, nombre de places).
- Effectuer un paiement (simulation dans l’interface).
- Annuler une réservation.
- Consulter les détails d’une réservation (billet généré en PDF).

### 2. 🛠️ Administrateur

- S’authentifier pour accéder à l’espace de gestion.
- Ajouter, modifier ou supprimer un film.
- Gérer les utilisateurs clients (ajout, modification, suppression).
- Gérer les salles.
- Programmer une séance (ajout, affectation d’un film à une salle/date).
- Supprimer ou modifier une séance existante.

---

## 🚀 Lancement de l’application

### ✅ Prérequis :

- Java JDK 17+
- Apache NetBeans (ou tout IDE compatible)
- MySQL

### ⚙️ Étapes :

1. Importer le projet dans NetBeans.
2. Exécuter le script `cinema/src/main/java/sql/billetteriecinema.sql` pour créer la base de données et les tables.
3. Vérifier les identifiants de connexion dans le fichier `Database.java`.
4. Lancer l’application via le fichier principal `Cinema.java`.

---

## 🧪 Exemple d’utilisation

### Côté Client :
- Création de compte puis connexion.
- Sélection d’un film → choix d’une séance → réservation de 2 places.
- Simulation de paiement → billet généré en PDF.

### Côté Admin :
- Connexion.
- Ajout d’un nouveau film.
- Création d’une salle → planification d’une séance.

---

## 📁 Organisation du code

```plaintext
cinema/src/main/java/com/mycompany/cinema/
├── dao/         # Accès aux données (FilmDAO, ClientDAO, etc.)
├── database/    # Connexion à la base de données
├── gui/         # Interfaces graphiques (Swing)
├── models/      # Entités (Film, Client, Réservation, etc.)
├── tests/       # Classes de test unitaires
├── utils/       # Fonctions utilitaires
└── Cinema.java  # Fichier principal de l’application
