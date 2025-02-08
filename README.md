# 🎾 Tennis Match Management API

![Java](https://img.shields.io/badge/Java-21-blue?style=flat&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen?style=flat&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=flat&logo=postgresql)
![Maven](https://img.shields.io/badge/Maven-3.9.9-orange?style=flat&logo=apachemaven)
![Last Commit](https://img.shields.io/github/last-commit/Escanor1986/Tennis-Match-Management)

## 📝 Description

Tennis Match Management API est une application REST développée avec **Spring Boot**, **Spring Web**, **Spring Data JPA**, et **PostgreSQL**.  
Elle permet de gérer les matchs de tennis, les joueurs, et d'obtenir des statistiques via une API REST.

---

## 🚀 **Technologies utilisées**

- **Java 17**
- **Spring Boot 3.4.2**
  - Spring Web (REST API)
  - Spring Data JPA (Gestion des entités)
- **PostgreSQL** (Base de données relationnelle)
- **Maven** (Gestionnaire de dépendances)
- **HikariCP** (Pool de connexion performant)
- **Lombok** (Réduction du boilerplate)
- **Swagger OpenAPI** (Documentation interactive)

---

## 📂 **Installation & Démarrage**

### 🔧 **Prérequis**

- **Java 21** installé (`java -version` pour vérifier)
- **Maven 3.9+** installé (`mvn -v` pour vérifier)
- **PostgreSQL** installé et en cours d'exécution

### 📥 **Cloner le projet**

```bash
git clone https://github.com/Escanor1986/Tennis-Match-Management.git
cd Tennis-Match-Management
```

### ⚙️ **Configurer la base de données**

Modifie `src/main/resources/application.properties` :

```properties
# PostgreSQL Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/tennis_db
spring.datasource.username=postgres
spring.datasource.password=your_password

# Hibernate Configuration
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

---

## ▶️ **Démarrer l'application**

Lance l'application avec Maven :

```bash
./mvnw spring-boot:run
```

L'application démarre sur **http://localhost:8080/** 🎾

---

## 📡 **Endpoints REST disponibles**

| Méthode | Endpoint               | Description |
|---------|------------------------|-------------|
| GET     | `/api/healthcheck`      | Vérifie si l'API est active |
| GET     | `/api/matches`          | Récupère tous les matchs |
| GET     | `/api/matches/{id}`     | Récupère un match spécifique |
| POST    | `/api/matches`          | Ajoute un nouveau match |
| PUT     | `/api/matches/{id}`     | Met à jour un match existant |
| DELETE  | `/api/matches/{id}`     | Supprime un match |

💡 **Exemple de requête GET :**

```bash
curl -X GET http://localhost:8080/api/matches
```

---

## 📜 **Exemple d'entité Match**

```java
@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String joueur1;
    private String joueur2;
    private String score;
    private LocalDate date;

    // Getters et setters
}
```

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

### **Dockerisation**

Tu peux exécuter l'application dans un conteneur **Docker** avec PostgreSQL :

#### 1️⃣ **Créer un fichier `Dockerfile`**

```dockerfile
FROM openjdk:21
WORKDIR /app
COPY target/tennis-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### 2️⃣ **Créer un fichier `docker-compose.yml`**

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

#### 3️⃣ **Lancer Docker**

```bash
docker-compose up --build
```

L'API sera accessible sur **http://localhost:8080/** 🚀

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
