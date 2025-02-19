# 🏉 Spring_Tennis API

![Java](https://img.shields.io/badge/Java-17-blue?style=flat&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen?style=flat&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=flat&logo=postgresql)
![Maven](https://img.shields.io/badge/Maven-3.9.9-orange?style=flat&logo=apachemaven)
![Last Commit](https://img.shields.io/github/last-commit/Escanor1986/Spring_Tennis)

## 📝 Description

Tennis Match Management API est une application REST développée avec **Spring Boot**, **Spring Web**, **Spring Data JPA**, et **PostgreSQL**.  
Elle permet de gérer les matchs de tennis, les joueurs, et d'obtenir des statistiques via une API REST.

🔹 **Nouvelle fonctionnalité** : Connexion à PostgreSQL via **Docker** avec gestion de la persistance des données.

---

## 🚀 **Technologies utilisées**

- **Java 17**
- **Spring Boot 3.4.2**
  - Spring Web (REST API)
  - Spring Data JPA (Gestion des entités)
- **PostgreSQL** (Base de données relationnelle)
- **Docker & Docker Compose** (Gestion de l'environnement PostgreSQL)
- **Maven** (Gestionnaire de dépendances)
- **HikariCP** (Pool de connexion performant)
- **Lombok** (Réduction du boilerplate)
- **Swagger OpenAPI** (Documentation interactive)

---

## 📂 **Installation & Démarrage**

### 🛠️ **Prérequis**

- **Java 21** installé (`java -version` pour vérifier)
- **Maven 3.9+** installé (`mvn -v` pour vérifier)
- **Docker & Docker Compose** installés (`docker -v` pour vérifier)

---

## 🛠 **Démarrer PostgreSQL avec Docker**

Le projet utilise **Docker Compose** pour lancer **PostgreSQL** rapidement.

### ▶️ **Lancer la base de données**

Depuis la racine du projet, lancez :

```bash
docker compose -f src/main/docker/postgresql.yml up -d
```

🤍 PostgreSQL sera disponible sur **localhost:5432** avec persistance des données.

> **Note :**  
> Les scripts d'initialisation situés dans `src/main/docker/init` (comme `init.sql`) s'exécutent **uniquement lors du premier démarrage** du container. Pour forcer leur réexécution (par exemple après correction d'un script), supprimez le dossier de données `postgres-data` :
> 
> ```bash
> rm -rf src/main/docker/postgres-data
> docker compose -f src/main/docker/postgresql.yml up -d
> ```

### ⏹ **Arrêter PostgreSQL**

```bash
docker compose -f src/main/docker/postgresql.yml down
```

🛠 Stoppe le container **sans supprimer les données**.

### 🔄 **Redémarrer PostgreSQL plus tard**

```bash
docker compose -f src/main/docker/postgresql.yml up -d
```

🌟 Relance PostgreSQL avec toutes les données précédentes.

---

## 🐞 **Débogage & Vérification de PostgreSQL**

Pour vous assurer que PostgreSQL fonctionne correctement et que le script d'initialisation s'est bien exécuté :

### 1. Vérifier les logs du container

Utilisez la commande suivante pour afficher les logs du container et rechercher d'éventuelles erreurs :

```bash
docker logs postgresql
```

Recherchez des messages confirmant l'exécution des scripts d'initialisation ou des erreurs pouvant indiquer un problème (comme une erreur de syntaxe dans `init.sql`).

### 2. Accéder à PostgreSQL via le terminal

Vous pouvez ouvrir une session interactive dans le container avec `psql` :

```bash
docker exec -it postgresql psql -U postgres
```

Une fois connecté, vous pouvez lister les tables pour vérifier la création de la table `player` :

```sql
\dt
```

Et interroger la table, par exemple :

```sql
SELECT * FROM player;
```

### 3. Connexion via l'extension Database de VS Code

Configurez une nouvelle connexion avec les paramètres suivants :

- **Hôte :** `localhost`
- **Port :** `5432`
- **Utilisateur :** `postgres`
- **Mot de passe :** `postgres`
- **Base de données :** `postgres` (ou toute base de votre choix)

---

## 📰 **Structure des données PostgreSQL**

Lors du démarrage, PostgreSQL exécute **`init.sql`** pour créer la table des joueurs.

#### **Contenu du script `init.sql` :**

```sql
-- Création de la séquence pour les identifiants des joueurs
CREATE SEQUENCE IF NOT EXISTS player_id_seq;

-- Création de la table des joueurs
CREATE TABLE IF NOT EXISTS player (
  id BIGINT PRIMARY KEY DEFAULT nextval('player_id_seq'),
  last_name CHARACTER VARYING(50) NOT NULL,
  first_name CHARACTER VARYING(50) NOT NULL,
  birth_date DATE NOT NULL,
  points INTEGER NOT NULL,
  rank INTEGER NOT NULL
);

-- Assignation de la séquence à la colonne id
ALTER SEQUENCE player_id_seq OWNED BY player.id;

-- Attribution du propriétaire de la table à postgres
ALTER TABLE IF EXISTS public.player OWNER TO postgres;
```

---

## 👀 **Endpoints REST disponibles**

### **🔹 Health Check**

| Méthode | Endpoint       | Description                  |
|---------|----------------|------------------------------|
| GET     | `/healthcheck` | Vérifie si l'API est active  |

### **🔹 Gestion des joueurs (`Player`)**

| Méthode | Endpoint                | Description                              |
|---------|-------------------------|------------------------------------------|
| GET     | `/player`               | Récupère la liste des joueurs            |
| GET     | `/player/{lastName}`    | Récupère un joueur par son nom           |
| POST    | `/player`               | Ajoute un nouveau joueur                 |
| PUT     | `/player`               | Met à jour un joueur existant            |
| DELETE  | `/player/{lastName}`    | Supprime un joueur par son nom           |

💡 **Tous les endpoints sont documentés dans Swagger UI** :  
🔗 [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

---

## 🏰 **Déploiement avec Docker**

Vous pouvez exécuter l'application dans un conteneur **Docker** avec PostgreSQL.

### **1️⃣ Créer un fichier `Dockerfile`**

```dockerfile
FROM openjdk:21
WORKDIR /app
COPY target/tennis-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### **2️⃣ Modifier `docker-compose.yml` pour inclure l'application**

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: tennis_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: your_password
    ports:
      - "5432:5432"

  tennis-app:
    build: .
    depends_on:
      - postgres
    ports:
      - "8080:8080"
```

### **3️⃣ Lancer l’application avec Docker**

```bash
docker-compose up --build
```

🚀 L'API sera accessible sur **http://localhost:8080/**

---

## 💼 **Licence**

Ce projet est sous licence **MIT**.  
Voir le fichier [LICENSE](LICENSE) pour plus d’informations.

---

## 🎯 **Remerciements**

Merci à tous ceux qui contribuent au projet ! 🙌
