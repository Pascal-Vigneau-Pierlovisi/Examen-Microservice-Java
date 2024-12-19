# Guide de Lancement du Projet

Ce guide explique les étapes nécessaires pour lancer ce projet avec succès.

## Prérequis

### Logiciels requis
- **Docker** : Assurez-vous que Docker est installé et configuré.
- **MySQL** : Une installation locale de MySQL est requise. Si vous êtes sur macOS, installez MySQL à l'aide de **Homebrew** :
  ```bash
  brew install mysql
  ```
- **Java** : Assurez-vous d'avoir une version compatible du JDK installée (Java 17 ou supérieure).
- **Maven** : Vérifiez que Maven est installé et accessible depuis votre terminal.

### Vérifications
- Assurez-vous que les ports requis (par exemple, `3306`, `8080`, `8761`, etc.) ne sont pas occupés par d'autres services.

---

## Étapes de lancement

### 1. Lancer le script Docker

Le script Docker se trouve dans le répertoire `/database`. Il est responsable de configurer et démarrer les conteneurs nécessaires, notamment MySQL.

#### Commande :
```bash
cd /database
./docker-compose up -d
```

Cela démarre les bases de données dans des conteneurs Docker. Attendez quelques secondes que tous les services soient opérationnels.

### 2. Importer les tables dans les bases de données

Un script Bash est fourni pour importer automatiquement les fichiers SQL dans les bases de données MySQL créées. Assurez-vous d'avoir MySQL installé localement pour que cette étape fonctionne.

#### Installer MySQL (macOS via Homebrew) :
Si MySQL n'est pas encore installé sur votre système, utilisez Homebrew :
```bash
brew install mysql
```

#### Lancer le script d'importation :
```bash
./import-tables.sh
```

Ce script va :
- Se connecter aux bases de données MySQL démarrées via Docker.
- Importer les fichiers `.sql` se trouvant dans le dossier `/database`.

---

### 3. Lancer le serveur Eureka

Le serveur Eureka est nécessaire pour gérer l'enregistrement et la découverte des services. Rendez-vous dans le dossier `eureka-server` et exécutez les commandes suivantes :

```bash
cd eureka-server
mvn clean install
mvn spring-boot:run
```

Attendez que le serveur soit opérationnel avant de lancer les autres services.

### 4. Lancer les autres services

Pour chaque service (comme `patient-service`, `appointment-service`, etc.), suivez ces étapes :

1. Rendez-vous dans le dossier du service :
   ```bash
   cd [nom-du-service]
   ```

2. Compilez le service :
   ```bash
   mvn clean install
   ```

3. Lancez le service :
   ```bash
   mvn spring-boot:run
   ```

Répétez ces étapes pour chaque service.

---

## Vérifier l'état des services

Une fois tous les services démarrés, ouvrez l'interface du serveur Eureka pour vérifier que tous les services sont bien enregistrés. L'interface est accessible à l'adresse suivante :

```
http://localhost:8761
```

Vous y trouverez la liste des services avec leurs ports respectifs pour les tests.

---

## Conclusion

En conclusion :
- La partie avec Google Calendar n'a pas pu être finalisée dans le temps imparti.
- Les bases de données ont été correctement dockerisées.
- Les trois premiers services (`patient-service`, `practitioner-service`, `medical-record-service`) fonctionnent avec succès et sont intégrés via le `gateway-service`.

Ce projet peut être étendu à l'avenir pour inclure les fonctionnalités Google Calendar et d'autres améliorations si nécessaires.

