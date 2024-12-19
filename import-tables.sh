#!/bin/bash

# Chemin du dossier contenant les fichiers SQL
SQL_DIR="./database"

# Liste des bases de données et conteneurs associés
DATABASES=("patientdb:patient-db" "practitionerdatabase:practitioner-db" "appointmentdb:appointment-db" "medicalrecorddb:medicalrecord-db")

# Boucle sur chaque base de données
for DB_CONTAINER in "${DATABASES[@]}"; do
    DB="${DB_CONTAINER%%:*}"         # Nom de la base
    CONTAINER="${DB_CONTAINER##*:}"  # Nom du conteneur
    SQL_FILE="${SQL_DIR}/${DB}.sql"

    if [ -f "$SQL_FILE" ]; then
        echo "Importation du fichier $SQL_FILE dans la base $DB via le conteneur $CONTAINER..."
        docker exec -i $CONTAINER mysql -u root -proot $DB < "$SQL_FILE"
        echo "Importation terminée pour la base $DB."
    else
        echo "Fichier $SQL_FILE introuvable. Passage à la base suivante."
    fi
done
