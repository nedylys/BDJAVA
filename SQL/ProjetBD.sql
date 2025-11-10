CREATE TABLE AdressePComplete (
    Latitude FLOAT NOT NULL
        CONSTRAINT Clatitude CHECK (Latitude >= -90 AND Latitude <= 90),
    Longitude FLOAT NOT NULL
        CONSTRAINT CLongitude CHECK (Longitude >= -180 AND Longitude <= 180),
    PRIMARY KEY (Latitude, Longitude) 
);

CREATE TABLE Producteur (
    idProducteur INT PRIMARY KEY,
    email VARCHAR2(100),
    NomProducteur VARCHAR2(30),
    PrenomProducteur VARCHAR2(30),
    Latitude FLOAT NOT NULL,
    Longitude FLOAT NOT NULL,
    CONSTRAINT FK_Producteur_Adresse
        FOREIGN KEY (Latitude, Longitude) 
        REFERENCES AdressePComplete(Latitude, Longitude)
        ON DELETE CASCADE
);

CREATE TABLE Activité(
    TypeActivité VARCHAR2(30) PRIMARY KEY
        CHECK (TypeActivité IN ('Agriculteur', 'Éleveur', 'Fromager'))
);

CREATE TABLE ProducteurAPourActivité(
    idProducteur INT,
    TypeActivité VARCHAR2(30),
    PRIMARY KEY (idProducteur, TypeActivité),
    CONSTRAINT fk_idProducteur
        FOREIGN KEY (idProducteur) 
        REFERENCES Producteur(idProducteur)
        ON DELETE CASCADE,
    CONSTRAINT fk_typeActivité
        FOREIGN KEY (TypeActivité) 
        REFERENCES Activité(TypeActivité)
        ON DELETE CASCADE
);

CREATE TABLE Produit(
    idProduit INT PRIMARY KEY,
    NomProduit VARCHAR2(30),
    CatégorieProduit VARCHAR2(30)
         CHECK (CatégorieProduit IN ('Fromage', 'boisson', 'céréales', 'légumineuse', 'fruits secs', 'huile')),
    DescriptionProduit VARCHAR2(255),
    StockProduit INT CHECK (StockProduit >= 0),
    idProducteur INT,
    CONSTRAINT fk_produit_producteur
        FOREIGN KEY (idProducteur) 
        REFERENCES Producteur(idProducteur)
        ON DELETE CASCADE
);

CREATE TABLE SaisonDisponibilité(
    DateDébut DATE,
    DateFin DATE,
    PRIMARY KEY (DateDébut, DateFin),
    CONSTRAINT ck_dates_saison CHECK (DateDébut < DateFin)
);

CREATE TABLE Caractéristique(
    idProduit INT,
    nomCaractéristique VARCHAR2(50)
        CHECK (nomCaractéristique IN ('bio', 'allergènes', 'pays d’origine')),
    valeur VARCHAR2(50),
    PRIMARY KEY (idProduit, nomCaractéristique),
        CONSTRAINT fk_idProduit
        FOREIGN KEY (idProduit) 
        REFERENCES Produit(idProduit)
        ON DELETE CASCADE
);

CREATE TABLE ProduitAPourSaison(
    idProduit INT,
    DateDébut DATE,
    DateFin DATE,
    PRIMARY KEY (idProduit, DateDébut, DateFin),
    CONSTRAINT fk_idProduit_saison
        FOREIGN KEY (idProduit) 
        REFERENCES Produit(idProduit)
        ON DELETE CASCADE,
    CONSTRAINT fk_saison_disponibilité
        FOREIGN KEY (DateDébut, DateFin) 
        REFERENCES SaisonDisponibilité(DateDébut, DateFin)
        ON DELETE CASCADE
);

CREATE TABLE ClientAnonyme(
    idClient INT PRIMARY KEY
);

CREATE TABLE Client(
    emailClient VARCHAR2(30) PRIMARY KEY,
    NomClient VARCHAR2(30),
    PrenomClient VARCHAR2(30),
    TelephoneClient VARCHAR2(15),
    idClient INT UNIQUE,
    CONSTRAINT fk_client_anonyme
        FOREIGN KEY (idClient) REFERENCES ClientAnonyme(idClient) ON DELETE CASCADE
);

CREATE TABLE AdresseLivraison(
    AdresseLivraison VARCHAR2(255) PRIMARY KEY
);

CREATE TABLE ClientAPourAdresseLivraison(
    emailClient VARCHAR2(30),
    AdresseLivraison VARCHAR2(255),
    PRIMARY KEY (emailClient, AdresseLivraison),
    CONSTRAINT fk_email_client
        FOREIGN KEY (emailClient) 
        REFERENCES Client(emailClient)
        ON DELETE CASCADE,
    CONSTRAINT fk_adresse_livraison
        FOREIGN KEY (AdresseLivraison) 
        REFERENCES AdresseLivraison(AdresseLivraison)
        ON DELETE CASCADE
);

CREATE TABLE Commande(
    idCommande INT PRIMARY KEY,
    DateCommande DATE,
    HeureCommande DATE,
    ModePaiement VARCHAR2(30)
        CHECK (ModePaiement IN ('En ligne', 'En boutique')),
    ModeRecuperation VARCHAR2(30),
    idClient INT,
    CONSTRAINT fk_commande_client
        FOREIGN KEY (idClient) REFERENCES ClientAnonyme(idClient) ON DELETE CASCADE
);

CREATE TABLE Contenant(
    idContenant INT PRIMARY KEY,
    TypeContenant VARCHAR2(30)
        CHECK (TypeContenant IN ('bocal en verre', 'sachet kraft', 'sachet tissu', 'papier ciré', 'autre')),
    CapacitéContenant INT CHECK (CapacitéContenant >= 0),
    StockDisponible INT CHECK (StockDisponible >= 0),
    ReutilisableContenant INT CHECK (ReutilisableContenant IN (0,1))
);

CREATE TABLE LotContenant(
    DateRéceptionC DATE,
    idContenant INT,
    QuantitéDisponibleC INT CHECK (QuantitéDisponibleC >= 0),
    PrixVenteCTTC INT CHECK (PrixVenteCTTC >= 0),
    PRIMARY KEY (DateRéceptionC, idContenant),
    CONSTRAINT fk_lot_contenant
        FOREIGN KEY (idContenant) 
        REFERENCES Contenant(idContenant)
        ON DELETE CASCADE
);

CREATE TABLE LotProduit(
    ModeConditionnement VARCHAR2(30) 
        CHECK (ModeConditionnement IN ('vrac', 'préconditionné')),
    PoidsUnitaire INT,
    DateRéceptionP DATE,
    QuantitéDisponibleP INT CHECK (QuantitéDisponibleP >= 0),
    DatePéremptionType VARCHAR2(30) CHECK (DatePéremptionType IN ('DLC', 'DLUO')),
    DatePeremption DATE,
    PrixVentePTTC INT CHECK (PrixVentePTTC >= 0),
    PrixAchatProducteur INT CHECK (PrixAchatProducteur >= 0),
    idProduit INT,
    PRIMARY KEY (DateRéceptionP, idProduit, ModeConditionnement, PoidsUnitaire),
    CONSTRAINT fk_lot_produit 
        FOREIGN KEY (idProduit) REFERENCES Produit(idProduit) 
        ON DELETE CASCADE,
    CONSTRAINT ck_date_peremption 
        CHECK (DateRéceptionP < DatePeremption)
);

CREATE TABLE LigneCommandeProduit(
    numLigneP INT,
    idCommande INT,
    idProduit INT,
    ModeConditionnement VARCHAR2(30),
    PoidsUnitaire INT,
    DateRéceptionP DATE,
    QuantitéCommandéeP INT CHECK (QuantitéCommandéeP > 0),
    PrixUnitaireP INT CHECK (PrixUnitaireP > 0),
    SousTotalLigneP INT CHECK (SousTotalLigneP > 0),
    PRIMARY KEY (numLigneP, idCommande),
    CONSTRAINT fk_ligne_commande_produit_commande
        FOREIGN KEY (idCommande) 
        REFERENCES Commande(idCommande)
        ON DELETE CASCADE,
    CONSTRAINT fk_ligne_commande_produit_lot
        FOREIGN KEY (DateRéceptionP, idProduit, ModeConditionnement, PoidsUnitaire) 
        REFERENCES LotProduit(DateRéceptionP, idProduit, ModeConditionnement, PoidsUnitaire)
        ON DELETE CASCADE
);

CREATE TABLE LigneCommandeContenant(
    numLigneC INT,
    idCommande INT,
    idContenant INT,
    DateRéceptionC DATE,
    QuantitéCommandéeC INT CHECK (QuantitéCommandéeC > 0),
    PrixUnitaireC INT CHECK (PrixUnitaireC > 0),
    SousTotalLigneC INT CHECK (SousTotalLigneC > 0),
    PRIMARY KEY (numLigneC, idCommande),
    CONSTRAINT fk_ligne_commande_contenant_commande
        FOREIGN KEY (idCommande) 
        REFERENCES Commande(idCommande)
        ON DELETE CASCADE,
    CONSTRAINT fk_ligne_commande_contenant_lot
        FOREIGN KEY (DateRéceptionC, idContenant) 
        REFERENCES LotContenant(DateRéceptionC, idContenant)
        ON DELETE CASCADE
);

CREATE TABLE CommandeàLivrer(
    idCommande INT PRIMARY KEY,
    StatutCommandeL VARCHAR2(30) CHECK (StatutCommandeL IN ('En préparation', 'Prête', 'En livraison', 'Livrée', 'Annulée')),
    FraisLivraison INT CHECK (FraisLivraison >= 0),
    DateLivraisonEstimée DATE,
    AdresseLivraison VARCHAR2(255),
    CONSTRAINT fk_commande_livrer_commande
        FOREIGN KEY (idCommande) 
        REFERENCES Commande(idCommande)
        ON DELETE CASCADE,
    CONSTRAINT fk_commande_livrer_adresse
        FOREIGN KEY (AdresseLivraison) 
        REFERENCES AdresseLivraison(AdresseLivraison)
        ON DELETE CASCADE
);

CREATE TABLE CommandeenBoutique(
    idCommande INT PRIMARY KEY,
    StatutCommandeB VARCHAR2(30) CHECK (StatutCommandeB IN ('En préparation', 'Prête', 'En livraison', 'Récupérée', 'Annulée')),
    CONSTRAINT fk_commande_boutique_commande
        FOREIGN KEY (idCommande) 
        REFERENCES Commande(idCommande)
        ON DELETE CASCADE
);

CREATE TABLE PerteProduit(
    idPerteP INT,
    idProduit INT,
    DatePerteP DATE,
    QuantitéPerdueP INT CHECK (QuantitéPerdueP > 0),
    NaturePerteP VARCHAR2(255) CHECK (NaturePerteP IN ('vol','casse')),
    PRIMARY KEY (idPerteP, idProduit),
    CONSTRAINT fk_perte_produit
        FOREIGN KEY (idProduit) 
        REFERENCES Produit(idProduit)
        ON DELETE CASCADE
);

CREATE TABLE PerteContenant(
    idPerteC INT,
    idContenant INT,
    DatePerteC DATE,
    QuantitéPerdueC INT CHECK (QuantitéPerdueC > 0),
    NaturePerteC VARCHAR2(255) CHECK (NaturePerteC IN ('vol','casse')),
    PRIMARY KEY (idPerteC, idContenant),
    CONSTRAINT fk_perte_contenant
        FOREIGN KEY (idContenant) 
        REFERENCES Contenant(idContenant)
        ON DELETE CASCADE
);

CREATE TABLE ProduitStock(
    idProduit INT PRIMARY KEY,
    CONSTRAINT fk_produit_stock
        FOREIGN KEY (idProduit) 
        REFERENCES Produit(idProduit)
        ON DELETE CASCADE
);

CREATE TABLE ProduitCommande(
    idProduit INT PRIMARY KEY,
    DélaiDisponibilitéHeure INT CHECK (DélaiDisponibilitéHeure > 0),
    CONSTRAINT fk_produit_commande
        FOREIGN KEY (idProduit) 
        REFERENCES Produit(idProduit)
        ON DELETE CASCADE
);




