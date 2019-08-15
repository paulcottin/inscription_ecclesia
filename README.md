# Inscription_ecclesia #
Algorithme de répartition des inscriptions pour Ecclesia Cantic.
Cet outil se base sur les données d'inscription fournies par les participants via le formulaire Google et stockées dans plusieurs feuilles de calcul sur Google Drive.

Cet outil produit, en sortie, les fichiers csv suivants :
- La répartition des personnes en groupe d'évangélisation
- La répartition des personnes en groupe de concert
- La répartition des personnes dans les masterclasses
- La répartition des masterclasses par salles
- Les données nécessaires à la génération des badges des participants
- Les fiches d'émargement pour les masterclasses

Les méthodes de répartition et de calcul seront détaillées par la suite.

# Prérequis techniques #

Pour compiler et lancer l'outil à partir du code présent dans ce dépôt il est nécessaire d'avoir :
- Installé la JDK (version minimale 1.8) et l'avoir ajouté au `PATH`
- Installé Maven (version minimale 3.5.0) et l'avoir ajouté au `PATH`
- Installé Git et l'avoir ajouté au `PATH`
- Un compte Google qui ait les droits de lecture sur les feuilles de calcul des inscriptions
- Autorisé l'utilisation de l'API Google via [ce tutoriel](https://developers.google.com/drive/api/v3/enable-drive-api)


# Compilation et lancement #
- Récupérer le code de ce projet via Git
- Ouvrir une invite de commande dans le répertoire contenant le projet et taper la commande suivantee : `mvn package`
- Vérifier que la commande se termine bien, il doit être indiqué à la fin : BUILD SUCCESS
- Cela va créer un dossier `target` contenant 
  - Un fichier `inscription-*.jar` 
  - Un dossier `lib`
- Il faut ensuite créer un fichier `launch.bat` contenant la ligne suivante : `java -jar inscription-<numero de version>.jar`
- Un double clic sur le fichier `launch.bat` permettra de lancer l'outil

# Prérequis fonctionnels #

Avant d'utiliser l'outil il est nécessaire d'avoir préparé les données qui vont servir de base aux répartitions.
Cela se fait en organisant les feuilles Google que l'outil va télécharger.

Le paramétrage des URLs des feuilles et du nom des onglets et colonnes s'effectue dans l'outil, il n'y a pas de normes particulières à respecter si ce n'est d'éviter les caractères spéciaux.

Les feuilles de données suivantes doivent exister et être remplies. 
Voici les colonnes minimales devant être présentes (Les noms des colonnes sont à titre indicatif et peuvent être modifiés)
Les valeurs présentes dans les colonnes ne sont pas figées. 
Il est possible de modifier une données dans la feuille Google et de relancer ensuite l'outil pour impacter directement la répartition.

- La liste des personnes inscrites
  - Nom
  - Prénom
  - Sexe
  - Pays
  - Ville
  - Code postal
  - Numéro de billet
  - Email
  - Date d'inscription
  - Date de naissance
  - Est-ce qu'il souhaite chanter
  - Voeu 1
  - Voeu 2
  - Voeu 3
  - Voeu 4
  - Voeu 5
  - Pupitre
  - Chorale
- La liste des chorales inscrites
  - Nom de la chorale
  - Est-ce une chorale référente ? (oui / non)
  - Identifiant du groupe missionaire auquel cette chorale sera associée
- La liste des salles disponibles
  - Nom de la salle
  - Capacité 
  - Est-elle disponible pour le créneau 1
  - Est-elle disponible pour le créneau 2
  - Est-elle disponible pour le créneau 3
- La liste des masterclasses disponibles
  - Nom de la masterclasse	
  - Est-elle disponible pour le créneau 1
  - Est-elle disponible pour le créneau 2
  - Est-elle disponible pour le créneau 3
  - Diviser en	? (Permet de diviser une masterclasse très demandée en 2 ou plus. Mettre 1 par défaut)
  - Salle : Salle dans laquelle se déroulera la masterclasse
- La liste de répartition des régions en groupe d'évangélisation
  - Pays
  - Département 
  - Code postal (contenant le numéro d'arrondisseent pour Paris
  - Groupe d'évangélisation	
  - Nombre limite de participants
- La liste de répartition des groupes d'évangélisation en groupe de concert
  - Identifiant du groupe d'évangelisation	
  - Identifiant du groupe de concert
  
  
Une fois que ces feuilles sont créées on peut passer à la configuration de l'outil avant de lancer la répartition.


# Paramétrage de l'outil #

Tout le paramétrage peut être fait depuis l'interface de l'outil, il est normalement pas nécessaire de modifier du code pour y parvenir.
Les propriétés à renseignées sont de plusieurs ordres : 
- Mode de fonctionnement de l'outil
- Localisation des données
- Mapping des colonnes
- Propriétés propres à l'édition d'Ecclesia Cantique
- Propriétés de l'algorithme

## Configuration du mode de fonctionnement ##

L'outil peut fonctionner soit avec les données qui proviennent de Google ou avec des données téléchargées une fois.
Le mode de fonctionnement depuis Google permet d'impacter automatiquement la répartition en fonction des champs modifiés ou des nouvelles inscriptions.
Le mode de fonctionnement local est plus rapide (pas de téléchargement à faire) et permet de travailler sans connexion Internet.

Ce choix se paramètre depuis l'interface de l'outil, il y a un switch présent dans le paragraphe "Type d'exécution" 

## Configuration des données ##

Que ce soit en mode "Local" ou en mode "Google" il faut renseigner le moyen d'accéder aux données.
Depuis l'interface, un bouton "Configuration" permet d'ouvrir une fenêtre où les informations sont à saisir.

### Configuration des données Google ###
- L'adresse mail utilisée pour accéder à l'API.
- Pour chaque feuille à télécharger il faut les informations suivantes : 
  - Google ID : C'est l'identifiant qui se trouve dans l'URL de la feuille Google lorsqu'elle est ouverte dans un navigateur
  - Plage de données :  `ONGLET`!`Cellule haut gauche`:`Cellule bas droite`. Exemple : inscrits pr algo!A1:BJ
  - Nom du fichier à enregistrer en local. Ce fichier sera enregistré dans \<REPERTOIRE DU PROJET>/data_export/\<DATE HEURE>

### Configuration des données locale ###
Dans le paragraphe "Fichier sources locaux".
Pour chaque feuille un sélecteur de fichier permet de les choisir

### Configuration des résultats ### 
Dans le paragraphe "Fichier d'export des résultats" on peut rentrer le nom voulu. Il est possible de rajouter un / pour placer le fichier dans un répertoire existant : data_export/repartition_salle_mc.csv

## Configuration du mapping ##
Cela permet de configurer les noms de colonne. Pour ouvrir l'écran de configuration, depuis l'interface, appuyer sur le bouton "Mapping".

### Configuration des colonnes ###
Par feuille les noms des colonnes sont à renseigner.
D'autres champs permettent plus de souplesse : 
- Actif : Le champ est-il toujours utilisé pour cette edition d'Ecclesia Cantique
- Nom du champ : Doit correspondre au nom de la colonne dans la feuille Google 
- Peut être vide : Permet de renseigner si le champ peut être vide. Par défaut ils sont censés être tous renseignés

Un bouton de test permet, une fois les champs renseignés, de valider que le mapping est correct.
Une fois le test effectué un pop up averti si le test est ok ou si il y a une erreur.

### Configuration des constantes ###

Les constantes sont les énumérations finies habituelle : 
- Le pupitre 
- Le sexe

Il est possible de renseigner plusieurs valeurs avec le séparateur `|`. Exemple : `Monsieur|M.|Mr.`

## Configuration propre à l'édition d'Ecclesia Cantique ##

Cette configuration se fait dans l'écran ouvert à partir du bouton `Configuration`.
Le paragraphe à renseigner est le suivant : "Propriétés propres à l'édition d'Ecclesia Cantique"

- Nombre de places réservées pour les volontaires par salle : modifier ce paramètre permet d'influer sur les résultats de l'algorithme
- Nombre de voeux total par participants
- Nombre de voeux à prendre en compte : Doit correspondre au nombre de créneaux.
- Marqueur de vrai dans Excel : Permet de dire comment on répond "Oui" dans les feuilles de données
- Faut-il avantager les personnes par date d'inscription ?

## Configuration de l'algorithme ##

Tous les paramètres sont présents sur l'interface qui apparaît au lancement de l'algorithme. 
Ils vont directement influer sur la répartition. Le but est de les fixer au fur et à mesure des essais pour avoir le moins d'erreurs possibles.

- Nombre de salle beaucoup plus grande que les autres : Permet d'influer sur la répartition des MCs les plus demandées dans les salles les plus grandes.
- Nombre de MC très peu populaires : Permet de donner aux gens qui ont choisi ces MC leur choix directement pour libérer des possibilités pour les MC très demandées
- Nombre min de participant pour considérer qu'une MC est remplie : 
  - Permet d'aider à attribuer la salle la plus adaptée à une MC en fonction de sa popularité.
  - N'est plus utilisé car on fixe les salles pour les masterclass maintenant
- Prendre en compte les participants qui ont des voeux mal remplis
  - Permet de ne pas les prendre en compte.
  - Cela sert car il est arrivé que pour certaines inscriptions des participants aient mis plusieurs fois le même voeu et donc entraîne des soucis ensuite dans la répartition des voeu au autres personnes
- Calculer la répartition des groupes d'évangélisation
  - Si c'est coché le calcul sera fait
  - Si ce n'est pas coché l'outil s'attendra au fait qu'une colonne remplie avec l'identifiant du groupe d'évangélisation soit présente pour chaque participant.
  - En effet en fonction des contraintes la calcul doit être quelque fois terminé à la main. Une fois fixé, le groupe d'évangélisation sert de données de base pour la génération des autres sorties
- Nombre min / max de participants à une MC pour considérer qu'elle soit peu populaire :
  - Permet de jouer sur l'algotithme, les MCs les moins populaires étant distribuées les premières
- Taux min / max de remplissage d'une MC au premier passage avant de considérer qu'elle est pleine
 - C'est ces paramètres qui devront être fait modifier pour jouer avec l'algorithme
 - Ce sont des nombres à virgule compris entre 0 et 1
 - L'algorithme va effectuer une proposition de répartition pour chaque pas de 0.05 entre la borne min et max.
 - Ex : Si `min = 0.1` et `max = 0.2`, 3 propositions de répartition seront faîtes (0.1, 0.15, 0.2), il suffira de choisir la meilleure à la fin du calcul


# Résultats #

Une petite console est disponible dans l'interface pour afficher les résultats de l'algorithme.
Elle n'est pas complètement fonctionnelle, aussi, si l'on veut avoir toutes les informations il est préférable de regarder dans l'invite de commande qui a servi à lancer l'outil. On y retrouve toutes les informations nécessaire.
A la fin de l'algorithme un pop up demande laquelle des répartitions on veut sauvegarder.