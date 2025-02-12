# 🎾 Spring_Tennis API

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

### 🔧 **Prérequis**

- **Java 21** installé (`java -version` pour vérifier)
- **Maven 3.9+** installé (`mvn -v` pour vérifier)
- **Docker & Docker Compose** installés (`docker -v` pour vérifier)

---

## 🛠 **Démarrer PostgreSQL avec Docker**

Le projet utilise **Docker Compose** pour lancer **PostgreSQL** rapidement.

### ▶️ **Lancer la base de données**

```bash
docker compose -f src/main/docker/postgresql.yml up -d
```

➡ PostgreSQL sera disponible sur **localhost:5432** avec persistance des données.

### ⏹ **Arrêter PostgreSQL**

```bash
docker compose -f src/main/docker/postgresql.yml down
```

➡ Stoppe le conteneur **sans supprimer les données**.

### 🔄 **Redémarrer PostgreSQL plus tard**

```bash
docker compose -f src/main/docker/postgresql.yml up -d
```

➡ Relance PostgreSQL avec toutes les données précédentes.

---

## 📡 **Endpoints REST disponibles**

| Méthode | Endpoint               | Description |
|---------|------------------------|-------------|
| GET     | `/healthcheck`         | Vérifie si l'API est active |
| GET     | `/testdata`            | Récupère toutes les entrées TestData (PostgreSQL) |
| GET     | `/matches`             | Récupère tous les matchs (à venir) |
| GET     | `/matches/{id}`        | Récupère un match spécifique (à venir) |
| POST    | `/matches`             | Ajoute un nouveau match (à venir) |
| PUT     | `/matches/{id}`        | Met à jour un match existant (à venir) |
| DELETE  | `/matches/{id}`        | Supprime un match (à venir) |

---

## 📥 **Cloner le projet & Démarrer l'application**

```bash
git clone https://github.com/Escanor1986/Spring_Tennis.git
cd tennis
./mvnw spring-boot:run
```

L'application démarre sur **<http://localhost:8080/>** 🎾

---

## 📜 **Exemple d'entité TestData (PostgreSQL)**

```java
@Entity
@Table(name = "test_data")
public class TestData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    // Getters & Setters
}
```

💡 Cette entité est accessible via `/testdata` et les données insérées depuis **VS Code** sont visibles via **l'API REST**.

---

## ✅ **Tester la connexion PostgreSQL avec VS Code**

**1️⃣ Ouvrir l'extension PostgreSQL dans VS Code**  

**2️⃣ Ajouter une connexion avec ces paramètres :**

- **Host** : `localhost`
- **Port** : `5432`
- **User** : `postgres`
- **Password** : `postgres`
- **Database** : `postgres`
  
**3️⃣ Créer une table dans PostgreSQL depuis VS Code**

```sql
CREATE TABLE test_data (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);
INSERT INTO test_data (name) VALUES ('Ajout via VS Code');
```

**4️⃣ Vérifier que les données sont accessibles dans Spring Boot :**

```bash
curl http://localhost:8080/testdata
```

💡 Les données insérées dans **VS Code** sont bien récupérées en **JSON** dans l'API ! 🎉

---

## 🛠 **Tests & Débogage**

### 🔍 **Accéder à la documentation API**

Après le démarrage, accède à **Swagger UI** :
👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### ✅ **Lancer les tests**

```bash
mvn test
```

---

## 📦 **Déploiement**

### **Dockerisation de l’application**

Tu peux exécuter l'application dans un conteneur **Docker** avec PostgreSQL.

#### **1️⃣ Créer un fichier `Dockerfile`**

```dockerfile
FROM openjdk:21
WORKDIR /app
COPY target/tennis-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### **2️⃣ Modifier `docker-compose.yml` pour inclure l'application**

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

#### **3️⃣ Lancer Docker**

```bash
docker-compose up --build
```

L'API sera accessible sur **<http://localhost:8080/>** 🚀

---

## 🤝 **Contribuer**

Les contributions sont les bienvenues !

1. Forke le projet 🍴  
2. Crée une branche (`git checkout -b feature/amélioration`)  
3. Commit tes modifications (`git commit -m "Ajout d'une nouvelle fonctionnalité"`)  
4. Push ta branche (`git push origin feature/amélioration`)  
5. Ouvre une **Pull Request** 🚀  

---

## 📜 **Licence**

Ce projet est sous licence **MIT**.  
Voir le fichier [LICENSE](LICENSE) pour plus d’informations.

---

## 🎯 **Remerciements**

Merci à tous ceux qui contribuent au projet ! 🙌
