CREATE TABLE AdressePComplete (
    Latitude FLOAT NOT NULL
        CONSTRAINT Clatitude CHECK (Latitude >= -90.0 AND Latitude <= 90.0),
    Longitude FLOAT NOT NULL
        CONSTRAINT CLongitude CHECK (Longitude >= -180.0 AND Longitude <= 180.0),
        
    PRIMARY KEY (Latitude, Longitude) 
);

CREATE TABLE Producteur (
    idProducteur INT PRIMARY KEY,
    
    email VARCHAR(30),
    NomProducteur VARCHAR(30),
    PrenomProducteur VARCHAR(30),
    
    Latitude FLOAT NOT NULL,
    Longitude FLOAT NOT NULL,
    
    CONSTRAINT FK_Producteur_Adresse
        FOREIGN KEY (Latitude, Longitude) 
        REFERENCES AdressePComplete(Latitude, Longitude)
        ON DELETE Cascade
);

CREATE TABLE Activité(
    TypeActivité VARCHAR(30) PRIMARY KEY
);

CREATE TABLE ProducteurAPourActivité(
    idProducteur int,
    TypeActivité VARCHAR(30),

    PRIMARY KEY (idProducteur, TypeActivité),

    CONSTRAINT fk_idProducteur
        FOREIGN KEY (idProducteur) 
        REFERENCES Producteur(idProducteur)
        ON DELETE Cascade,
    
    
    CONSTRAINT fk_typeActivité
        FOREIGN KEY (TypeActivité) 
        REFERENCES Activité(TypeActivité)
        ON DELETE Cascade
);

CREATE TABLE Produit(
    idProduit INT PRIMARY KEY,

    NomProduit VARCHAR(30),
    CatégorieProduit VARCHAR(30),
    DescriptionProduit VARCHAR(255),
    StockProduit FLOAT,
    idProducteur INT,

    CONSTRAINT fk_produit_producteur
        FOREIGN KEY (idProducteur) 
        REFERENCES Producteur(idProducteur)
        ON DELETE Cascade
);


CREATE TABLE SaisonDisponibilité(
    DateDébut DATE,
    DateFin DATE,

    PRIMARY KEY (DateDébut, DateFin)
)

CREATE TABLE Caractéristique(
    nomCaractéristique VARCHAR(50) PRIMARY KEY,
    valeur VARCHAR(50),
);

CREATE TABLE ProduitAPourCaractéristique(
    idProduit INT,
    nomCaractéristique VARCHAR(50),

    PRIMARY KEY (idProduit, nomCaractéristique),

    CONSTRAINT fk_idProduit
        FOREIGN KEY (idProduit) 
        REFERENCES Produit(idProduit)
        ON DELETE Cascade,
    
    
    CONSTRAINT fk_nomCaractéristique
        FOREIGN KEY (nomCaractéristique) 
        REFERENCES Caractéristique(nomCaractéristique)
        ON DELETE Cascade
);

CREATE TABLe ProduitAPourSaison(
    idProduit INT,
    DateDébut DATE,
    DateFin DATE,

    PRIMARY KEY (idProduit, DateDébut, DateFin),

    CONSTRAINT fk_idProduit_saison
        FOREIGN KEY (idProduit) 
        REFERENCES Produit(idProduit)
        ON DELETE Cascade,
    
    
    CONSTRAINT fk_saison_disponibilité
        FOREIGN KEY (DateDébut, DateFin) 
        REFERENCES SaisonDisponibilité(DateDébut, DateFin)
        ON DELETE Cascade
);

CREATE TABLE ClientAnonyme(
    idClient INT PRIMARY KEY,
)


CREATE TABLE Client(
    emailClient VARCHAR(30) PRIMARY KEY,
    NomClient VARCHAR(30),
    PrenomClient VARCHAR(30),
    TelephoneClient VARCHAR(15),
    idClient INT,

    CONSTRAINT fk_client_anonyme
        FOREIGN KEY (idClient) 
        REFERENCES ClientAnonyme(idClient)
        ON DELETE Cascade
);

CREATE TABLE AdresseLivraison(
    AdresseLivraison VARCHAR(255) PRIMARY KEY,
)

CREATE TABLE ClientAPourAdresseLivraison(
    emailClient VARCHAR(30),
    AdresseLivraison VARCHAR(255),

    PRIMARY KEY (emailClient, AdresseLivraison),

    CONSTRAINT fk_email_client
        FOREIGN KEY (emailClient) 
        REFERENCES Client(emailClient)
        ON DELETE Cascade,
    
    
    CONSTRAINT fk_adresse_livraison
        FOREIGN KEY (AdresseLivraison) 
        REFERENCES AdresseLivraison(AdresseLivraison)
        ON DELETE Cascade
);


CREATE TABLE Commande(
    idCommande INT PRIMARY KEY,
    DateCommande DATE,
    HeureCommande TIME,
    ModePaiement VARCHAR(30),
    ModeRecuperation VARCHAR(30),
    idClient INT,

    CONSTRAINT fk_commande_client
        FOREIGN KEY (idClient) 
        REFERENCES ClientAnonyme(idClient)
        ON DELETE Cascade
);


CREATE TABLE Contenant(
    idContenant INT PRIMARY KEY,
    TypeContenant VARCHAR(30),
    CapacitéContenant FLOAT,
    StockDisponible INT,
    ReutilisableContenant BOOLEAN
);


CREATE TABLE LotContenant(
    DateRéceptionC DATE,
    idContenant INT,
    QuantitéDisponibleC INT,
    PrixVenteCTTC FLOAT,

    PRIMARY KEY (DateRéceptionC, idContenant),

    CONSTRAINT fk_lot_contenant
        FOREIGN KEY (idContenant) 
        REFERENCES Contenant(idContenant)
        ON DELETE Cascade
);

CREATE TABLE LotProduit(
    ModeConditionnement VARCHAR(30),
    PoidsUnitaire FLOAT,
    DateRéceptionP DATE,
    QuantitéDisponibleP INT,
    DatePéremptionType VARCHAR(30),
    DatePeremption DATE,
    PrixVentePTTC FLOAT,
    PrixAchatProducteur FLOAT,
    idProduit INT,

    PRIMARY KEY (DateRéceptionP, idProduit, ModeConditionnement, PoidsUnitaire),

    CONSTRAINT fk_lot_produit
        FOREIGN KEY (idProduit) 
        REFERENCES Produit(idProduit)
        ON DELETE Cascade
);


CREATE TABLE LigneCommandeProduit(
    numLigneP INT,
    idCommande INT,
    idProduit INT,
    ModeConditionnement VARCHAR(30),
    PoidsUnitaire FLOAT,
    DateRéceptionP DATE,
    QuantitéCommandéeP FLOAT,
    PrixUnitaireP FLOAT,
    SousTotalLigneP FLOAT,

    PRIMARY KEY (numLigneP, idCommande),

    CONSTRAINT fk_ligne_commande_produit_commande
        FOREIGN KEY (idCommande) 
        REFERENCES Commande(idCommande)
        ON DELETE Cascade,

    CONSTRAINT fk_ligne_commande_produit_lot
        FOREIGN KEY (DateRéceptionP, idProduit, ModeConditionnement, PoidsUnitaire) 
        REFERENCES LotProduit(DateRéceptionP, idProduit, ModeConditionnement, PoidsUnitaire)
        ON DELETE Cascade
);


CREATE TABLE LigneCommandeContenant(
    numLigneC INT,
    idCommande INT,
    idContenant INT,
    DateRéceptionC DATE,
    QuantitéCommandéeC INT,
    PrixUnitaireC FLOAT,
    SousTotalLigneC FLOAT,

    PRIMARY KEY (numLigneC, idCommande),

    CONSTRAINT fk_ligne_commande_contenant_commande
        FOREIGN KEY (idCommande) 
        REFERENCES Commande(idCommande)
        ON DELETE Cascade,

    CONSTRAINT fk_ligne_commande_contenant_lot
        FOREIGN KEY (DateRéceptionC, idContenant) 
        REFERENCES LotContenant(DateRéceptionC, idContenant)
        ON DELETE Cascade
);

CREATE TABLE CommandeàLivrer(
    idCommande INT Primary KEY,
    StatutCommandeL VARCHAR(30),
    FraisLivraison FLOAT,
    DateLivraisonEstimée DATE,
    AdresseLivraison VARCHAR(255),

    CONSTRAINT fk_commande_livrer_commande
        FOREIGN KEY (idCommande) 
        REFERENCES Commande(idCommande)
        ON DELETE Cascade,

    CONSTRAINT fk_commande_livrer_adresse
        FOREIGN KEY (AdresseLivraison) 
        REFERENCES AdresseLivraison(AdresseLivraison)
        ON DELETE Cascade
)

CREATE TABLE CommandeenBoutique(
    idCommande INT Primary KEY,
    StatutCommandeB VARCHAR(30),

    CONSTRAINT fk_commande_boutique_commande
        FOREIGN KEY (idCommande) 
        REFERENCES Commande(idCommande)
        ON DELETE Cascade
);

CREATE TABLE PerteProduit(
    idPerteP INT,
    idProduit INT,
    DatePerteP DATE,
    QuantitéPerdueP FLOAT,
    NaturePerteP VARCHAR(255),

    PRIMARY KEY (idPerteP, idProduit),
    CONSTRAINT fk_perte_produit
        FOREIGN KEY (idProduit) 
        REFERENCES Produit(idProduit)
        ON DELETE Cascade
);

CREATE TABLE PerteContenant(
    idPerteC INT,
    idContenant INT,
    DatePerteC DATE,
    QuantitéPerdueC INT,
    NaturePerteC VARCHAR(255),

    PRIMARY KEY (idPerteC, idContenant),
    CONSTRAINT fk_perte_contenant
        FOREIGN KEY (idContenant) 
        REFERENCES Contenant(idContenant)
        ON DELETE Cascade
);

CREATE TABLE ProduitStock(
    idProduit INT PRIMARY KEY,
    CONSTRAINT fk_produit_stock
        FOREIGN KEY (idProduit) 
        REFERENCES Produit(idProduit)
        ON DELETE Cascade
)

CREATE TABLE ProduitCommande(
    idProduit INT PRIMARY KEY,
    DélaiDisponibilitéHeure FLOAT,
    CONSTRAINT fk_produit_commande
        FOREIGN KEY (idProduit) 
        REFERENCES Produit(idProduit)
        ON DELETE Cascade
)

-- DROP TABLE ProducteurAPourActivité;
-- DROP TABLE Producteur;
-- DROP TABLE Activité;
-- DROP TABLE AdressePComplete;



