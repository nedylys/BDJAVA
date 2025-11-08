CREATE TABLE AdressePComplete (
    Latitude NUMBER NOT NULL
        CONSTRAINT Clatitude CHECK (Latitude >= -90 AND Latitude <= 90),
    Longitude NUMBER NOT NULL
        CONSTRAINT CLongitude CHECK (Longitude >= -180 AND Longitude <= 180),
    PRIMARY KEY (Latitude, Longitude) 
);

CREATE TABLE Producteur (
    idProducteur NUMBER PRIMARY KEY,
    email VARCHAR2(30),
    NomProducteur VARCHAR2(30),
    PrenomProducteur VARCHAR2(30),
    Latitude NUMBER NOT NULL,
    Longitude NUMBER NOT NULL,
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
    idProducteur NUMBER,
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
    idProduit NUMBER PRIMARY KEY,
    NomProduit VARCHAR2(30),
    CatégorieProduit VARCHAR2(30)
         CHECK (CatégorieProduit IN ('Fromage', 'boisson', 'céréales', 'légumineuse', 'fruits secs', 'huile')),
    DescriptionProduit VARCHAR2(255),
    StockProduit NUMBER CHECK (StockProduit >= 0),
    idProducteur NUMBER,
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
    nomCaractéristique VARCHAR2(50) PRIMARY KEY
        CHECK (nomCaractéristique IN ('bio', 'allergènes', 'pays d’origine')),
    valeur VARCHAR2(50)
);

CREATE TABLE ProduitAPourCaractéristique(
    idProduit NUMBER,
    nomCaractéristique VARCHAR2(50),
    PRIMARY KEY (idProduit, nomCaractéristique),
    CONSTRAINT fk_idProduit
        FOREIGN KEY (idProduit) 
        REFERENCES Produit(idProduit)
        ON DELETE CASCADE,
    CONSTRAINT fk_nomCaractéristique
        FOREIGN KEY (nomCaractéristique) 
        REFERENCES Caractéristique(nomCaractéristique)
        ON DELETE CASCADE
);

CREATE TABLE ProduitAPourSaison(
    idProduit NUMBER,
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
    idClient NUMBER PRIMARY KEY
);

CREATE TABLE Client(
    emailClient VARCHAR2(30) PRIMARY KEY,
    NomClient VARCHAR2(30),
    PrenomClient VARCHAR2(30),
    TelephoneClient VARCHAR2(15),
    idClient NUMBER UNIQUE,
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
    idCommande NUMBER PRIMARY KEY,
    DateCommande DATE,
    HeureCommande DATE,
    ModePaiement VARCHAR2(30)
        CHECK (ModePaiement IN ('En ligne', 'En boutique')),
    ModeRecuperation VARCHAR2(30),
    idClient NUMBER,
    CONSTRAINT fk_commande_client
        FOREIGN KEY (idClient) REFERENCES ClientAnonyme(idClient) ON DELETE CASCADE
);

CREATE TABLE Contenant(
    idContenant NUMBER PRIMARY KEY,
    TypeContenant VARCHAR2(30)
        CHECK (TypeContenant IN ('bocal en verre', 'sachet kraft', 'sachet tissu', 'papier ciré', 'autre')),
    CapacitéContenant NUMBER CHECK (CapacitéContenant >= 0),
    StockDisponible NUMBER CHECK (StockDisponible >= 0),
    ReutilisableContenant CHAR(1)
);

CREATE TABLE LotContenant(
    DateRéceptionC DATE,
    idContenant NUMBER,
    QuantitéDisponibleC NUMBER CHECK (QuantitéDisponibleC >= 0),
    PrixVenteCTTC NUMBER CHECK (PrixVenteCTTC >= 0),
    PRIMARY KEY (DateRéceptionC, idContenant),
    CONSTRAINT fk_lot_contenant
        FOREIGN KEY (idContenant) 
        REFERENCES Contenant(idContenant)
        ON DELETE CASCADE
);

CREATE TABLE LotProduit(
    ModeConditionnement VARCHAR2(30) 
        CHECK (ModeConditionnement IN ('vrac', 'préconditionné')),
    PoidsUnitaire NUMBER,
    DateRéceptionP DATE,
    QuantitéDisponibleP NUMBER CHECK (QuantitéDisponibleP >= 0),
    DatePéremptionType VARCHAR2(30) CHECK (DatePéremptionType IN ('DLC', 'DLUO')),
    DatePeremption DATE,
    PrixVentePTTC NUMBER CHECK (PrixVentePTTC >= 0),
    PrixAchatProducteur NUMBER CHECK (PrixAchatProducteur >= 0),
    idProduit NUMBER,
    PRIMARY KEY (DateRéceptionP, idProduit, ModeConditionnement, PoidsUnitaire),
    CONSTRAINT fk_lot_produit 
        FOREIGN KEY (idProduit) REFERENCES Produit(idProduit) 
        ON DELETE CASCADE,
    CONSTRAINT ck_date_peremption 
        CHECK (DateRéceptionP < DatePeremption)
);

CREATE TABLE LigneCommandeProduit(
    numLigneP NUMBER,
    idCommande NUMBER,
    idProduit NUMBER,
    ModeConditionnement VARCHAR2(30),
    PoidsUnitaire NUMBER,
    DateRéceptionP DATE,
    QuantitéCommandéeP NUMBER CHECK (QuantitéCommandéeP > 0),
    PrixUnitaireP NUMBER CHECK (PrixUnitaireP > 0),
    SousTotalLigneP NUMBER CHECK (SousTotalLigneP > 0),
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
    numLigneC NUMBER,
    idCommande NUMBER,
    idContenant NUMBER,
    DateRéceptionC DATE,
    QuantitéCommandéeC NUMBER CHECK (QuantitéCommandéeC > 0),
    PrixUnitaireC NUMBER CHECK (PrixUnitaireC > 0),
    SousTotalLigneC NUMBER CHECK (SousTotalLigneC > 0),
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
    idCommande NUMBER PRIMARY KEY,
    StatutCommandeL VARCHAR2(30) CHECK (StatutCommandeL IN ('En préparation', 'Prête', 'En livraison', 'Livrée', 'Annulée')),
    FraisLivraison NUMBER CHECK (FraisLivraison >= 0),
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
    idCommande NUMBER PRIMARY KEY,
    StatutCommandeB VARCHAR2(30) CHECK (StatutCommandeB IN ('En préparation', 'Prête', 'En livraison', 'Récupérée', 'Annulée')),
    CONSTRAINT fk_commande_boutique_commande
        FOREIGN KEY (idCommande) 
        REFERENCES Commande(idCommande)
        ON DELETE CASCADE
);

CREATE TABLE PerteProduit(
    idPerteP NUMBER,
    idProduit NUMBER,
    DatePerteP DATE,
    QuantitéPerdueP NUMBER CHECK (QuantitéPerdueP > 0),
    NaturePerteP VARCHAR2(255) CHECK (NaturePerteP IN ('vol','casse')),
    PRIMARY KEY (idPerteP, idProduit),
    CONSTRAINT fk_perte_produit
        FOREIGN KEY (idProduit) 
        REFERENCES Produit(idProduit)
        ON DELETE CASCADE
);

CREATE TABLE PerteContenant(
    idPerteC NUMBER,
    idContenant NUMBER,
    DatePerteC DATE,
    QuantitéPerdueC NUMBER CHECK (QuantitéPerdueC > 0),
    NaturePerteC VARCHAR2(255) CHECK (NaturePerteC IN ('vol','casse')),
    PRIMARY KEY (idPerteC, idContenant),
    CONSTRAINT fk_perte_contenant
        FOREIGN KEY (idContenant) 
        REFERENCES Contenant(idContenant)
        ON DELETE CASCADE
);

CREATE TABLE ProduitStock(
    idProduit NUMBER PRIMARY KEY,
    CONSTRAINT fk_produit_stock
        FOREIGN KEY (idProduit) 
        REFERENCES Produit(idProduit)
        ON DELETE CASCADE
);

CREATE TABLE ProduitCommande(
    idProduit NUMBER PRIMARY KEY,
    DélaiDisponibilitéHeure NUMBER CHECK (DélaiDisponibilitéHeure > 0),
    CONSTRAINT fk_produit_commande
        FOREIGN KEY (idProduit) 
        REFERENCES Produit(idProduit)
        ON DELETE CASCADE
);

-- DROP TABLE ProducteurAPourActivité;
-- DROP TABLE Producteur;
-- DROP TABLE Activité;
-- DROP TABLE AdressePComplete;
/*DROP TABLE LigneCommandeProduit CASCADE CONSTRAINTS;
DROP TABLE LigneCommandeContenant CASCADE CONSTRAINTS;
DROP TABLE CommandeàLivrer CASCADE CONSTRAINTS;
DROP TABLE CommandeenBoutique CASCADE CONSTRAINTS;
DROP TABLE ProduitAPourCaractéristique CASCADE CONSTRAINTS;
DROP TABLE ProduitAPourSaison CASCADE CONSTRAINTS;
DROP TABLE ProducteurAPourActivité CASCADE CONSTRAINTS;
DROP TABLE PerteProduit CASCADE CONSTRAINTS;
DROP TABLE PerteContenant CASCADE CONSTRAINTS;
DROP TABLE LotProduit CASCADE CONSTRAINTS;
DROP TABLE LotContenant CASCADE CONSTRAINTS;
DROP TABLE ProduitStock CASCADE CONSTRAINTS;
DROP TABLE ProduitCommande CASCADE CONSTRAINTS;
DROP TABLE Produit CASCADE CONSTRAINTS;
DROP TABLE Contenant CASCADE CONSTRAINTS;
DROP TABLE LigneCommandeProduit CASCADE CONSTRAINTS; -- si pas déjà supprimée
DROP TABLE Commande CASCADE CONSTRAINTS;
DROP TABLE ClientAPourAdresseLivraison CASCADE CONSTRAINTS;
DROP TABLE Client CASCADE CONSTRAINTS;
DROP TABLE ClientAnonyme CASCADE CONSTRAINTS;
DROP TABLE AdresseLivraison CASCADE CONSTRAINTS;
DROP TABLE Activité CASCADE CONSTRAINTS;
DROP TABLE Caractéristique CASCADE CONSTRAINTS;
DROP TABLE SaisonDisponibilité CASCADE CONSTRAINTS;
DROP TABLE Producteur CASCADE CONSTRAINTS;
DROP TABLE AdressePComplete CASCADE CONSTRAINTS;*/




