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

CREATE TABLE Activite(
    TypeActivite VARCHAR2(30) PRIMARY KEY
        CHECK (TypeActivite IN ('Agriculteur', 'Eleveur', 'Fromager'))
);

SELECT constraint_name
FROM user_constraints
WHERE table_name = 'Activite' 
  AND constraint_type = 'C';


CREATE TABLE ProducteurAPourActivite(
    idProducteur INT,
    TypeActivite VARCHAR2(30),
    PRIMARY KEY (idProducteur, TypeActivite),
    CONSTRAINT fk_idProducteur
        FOREIGN KEY (idProducteur) 
        REFERENCES Producteur(idProducteur)
        ON DELETE CASCADE,
    CONSTRAINT fk_typeActivite
        FOREIGN KEY (TypeActivite) 
        REFERENCES Activite(TypeActivite)
        ON DELETE CASCADE
);

CREATE TABLE Produit(
    idProduit INT PRIMARY KEY,
    NomProduit VARCHAR2(30),
    CategorieProduit VARCHAR2(30)
         CHECK (CategorieProduit IN ('Fromage', 'boisson', 'cereales', 'legumineuse', 'fruits secs', 'huile', 'pates', 'viande', 'laitage',
        'legume', 'fruit', 'epice')),
    DescriptionProduit VARCHAR2(255),
    StockProduit FLOAT CHECK (StockProduit >= 0),
    idProducteur INT,
    CONSTRAINT fk_produit_producteur
        FOREIGN KEY (idProducteur) 
        REFERENCES Producteur(idProducteur)
        
        ON DELETE CASCADE
);

CREATE TABLE SaisonDisponibilite(
    DateDebut DATE,
    DateFin DATE,
    PRIMARY KEY (DateDebut, DateFin),
    CONSTRAINT ck_dates_saison CHECK (DateDebut < DateFin)
);

CREATE TABLE Caracteristique(
    idProduit INT,
    nomCaracteristique VARCHAR2(50)
        CHECK (nomCaracteristique IN ('bio', 'allergènes', 'pays d’origine')),
    valeur VARCHAR2(50),
    PRIMARY KEY (idProduit, nomCaracteristique),
        CONSTRAINT fk_idProduit
        FOREIGN KEY (idProduit) 
        REFERENCES Produit(idProduit)
        ON DELETE CASCADE
);

CREATE TABLE ProduitAPourSaison(
    idProduit INT,
    DateDebut DATE,
    DateFin DATE,
    PRIMARY KEY (idProduit, DateDebut, DateFin),
    CONSTRAINT fk_idProduit_saison
        FOREIGN KEY (idProduit) 
        REFERENCES Produit(idProduit)
        ON DELETE CASCADE,
    CONSTRAINT fk_saison_disponibilite
        FOREIGN KEY (DateDebut, DateFin) 
        REFERENCES SaisonDisponibilite(DateDebut, DateFin)
        ON DELETE CASCADE
);

CREATE TABLE ClientAnonyme(
    idClient INT PRIMARY KEY
);

CREATE TABLE Client(
    emailClient VARCHAR2(255) PRIMARY KEY,
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
        CHECK (TypeContenant IN ('bocal en verre', 'sachet kraft', 'sachet tissu', 'papier cire', 'autre')),
    CapaciteContenant INT CHECK (CapaciteContenant >= 0),
    StockDisponible FLOAT CHECK (StockDisponible >= 0),
    ReutilisableContenant INT CHECK (ReutilisableContenant IN (0,1))
);

CREATE TABLE LotContenant(
    DateReceptionC DATE,
    idContenant INT,
    QuantiteDisponibleC INT CHECK (QuantiteDisponibleC >= 0),
    PrixVenteCTTC INT CHECK (PrixVenteCTTC >= 0),
    PRIMARY KEY (DateReceptionC, idContenant),
    CONSTRAINT fk_lot_contenant
        FOREIGN KEY (idContenant) 
        REFERENCES Contenant(idContenant)
        ON DELETE CASCADE
);

CREATE TABLE LotProduit(
    ModeConditionnement VARCHAR2(30) 
        CHECK (ModeConditionnement IN ('vrac', 'preconditionne')),
    PoidsUnitaire FLOAT,
    DateReceptionP DATE,
    QuantiteDisponibleP FLOAT CHECK (QuantiteDisponibleP >= 0),
    DatePeremptionType VARCHAR2(30) CHECK (DatePeremptionType IN ('DLC', 'DLUO')),
    DatePeremption DATE,
    PrixVentePTTC FLOAT CHECK (PrixVentePTTC >= 0),
    PrixAchatProducteur FLOAT CHECK (PrixAchatProducteur >= 0),
    idProduit INT,
    PRIMARY KEY (DateReceptionP, idProduit, ModeConditionnement, PoidsUnitaire),
    CONSTRAINT fk_lot_produit 
        FOREIGN KEY (idProduit) REFERENCES Produit(idProduit) 
        ON DELETE CASCADE,
    CONSTRAINT ck_date_peremption 
        CHECK (DateReceptionP < DatePeremption)
);

CREATE TABLE LigneCommandeProduit(
    numLigneP INT,
    idCommande INT,
    idProduit INT,
    ModeConditionnement VARCHAR2(30),
    PoidsUnitaire Float,
    DateReceptionP DATE,
    QuantiteCommandeeP FLOAT CHECK (QuantiteCommandeeP > 0),
    PrixUnitaireP FLOAT CHECK (PrixUnitaireP > 0),
    SousTotalLigneP FLOAT CHECK (SousTotalLigneP > 0),
    PRIMARY KEY (numLigneP, idCommande),
    CONSTRAINT fk_ligne_commande_produit_commande
        FOREIGN KEY (idCommande) 
        REFERENCES Commande(idCommande)
        ON DELETE CASCADE,
    CONSTRAINT fk_ligne_commande_produit_lot
        FOREIGN KEY (DateReceptionP, idProduit, ModeConditionnement, PoidsUnitaire) 
        REFERENCES LotProduit(DateReceptionP, idProduit, ModeConditionnement, PoidsUnitaire)
        ON DELETE CASCADE
);

/* ALTER TABLE LotProduit MODIFY (PoidsUnitaire Float);

ALTER TABLE LigneCommandeProduit MODIFY (PoidsUnitaire Float); */

CREATE TABLE LigneCommandeContenant(
    numLigneC INT,
    idCommande INT,
    idContenant INT,
    DateReceptionC DATE,
    QuantiteCommandeeC INT CHECK (QuantiteCommandeeC > 0),
    PrixUnitaireC FLOAT CHECK (PrixUnitaireC > 0),
    SousTotalLigneC FLOAT CHECK (SousTotalLigneC > 0),
    PRIMARY KEY (numLigneC, idCommande),
    CONSTRAINT fk_ligne_commande_contenant_commande
        FOREIGN KEY (idCommande) 
        REFERENCES Commande(idCommande)
        ON DELETE CASCADE,
    CONSTRAINT fk_ligne_commande_contenant_lot
        FOREIGN KEY (DateReceptionC, idContenant) 
        REFERENCES LotContenant(DateReceptionC, idContenant)
        ON DELETE CASCADE
);

CREATE TABLE CommandeaLivrer(
    idCommande INT PRIMARY KEY,
    StatutCommandeL VARCHAR2(30) CHECK (StatutCommandeL IN ('En preparation', 'Prete', 'En livraison', 'Livree', 'Annulee')),
    FraisLivraison FLOAT CHECK (FraisLivraison >= 0),
    DateLivraisonEstimee DATE,
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
/* ALTER TABLE CommandeaLivrer
DROP CONSTRAINT SYS_C001323420; */

/* ALTER TABLE CommandeaLivrer
ADD CONSTRAINT ck_statut_commande_l
CHECK (StatutCommandeL IN ('En preparation', 'Prete', 'En livraison', 'Livree', 'Annulee')); */


CREATE TABLE CommandeenBoutique(
    idCommande INT PRIMARY KEY,
    StatutCommandeB VARCHAR2(30) CHECK (StatutCommandeB IN ('En preparation', 'Prete', 'En livraison', 'Recuperee', 'Annulee')),
    CONSTRAINT fk_commande_boutique_commande
        FOREIGN KEY (idCommande) 
        REFERENCES Commande(idCommande)
        ON DELETE CASCADE
);

/* ALTER TABLE CommandeenBoutique
DROP CONSTRAINT SYS_C001323405;

ALTER TABLE CommandeenBoutique
DROP CONSTRAINT SYS_C001323404;


ALTER TABLE CommandeenBoutique
ADD CONSTRAINT ck_statut_commande_b
CHECK (StatutCommandeB IN ('En preparation', 'Prete', 'En livraison', 'Livree', 'Annulee')); */

/* SELECT StatutCommandeB FROM CommandeenBoutique;
UPDATE CommandeenBoutique
SET StatutCommandeB = 'Prete'
WHERE StatutCommandeB = 'Prête'; */


CREATE TABLE PerteProduit(
    idPerteP INT,
    idProduit INT,
    DatePerteP DATE,
    QuantitePerdueP INT CHECK (QuantitePerdueP > 0),
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
    QuantitePerdueC INT CHECK (QuantitePerdueC > 0),
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
    DelaiDisponibiliteHeure INT CHECK (DelaiDisponibiliteHeure > 0),
    CONSTRAINT fk_produit_commande
        FOREIGN KEY (idProduit) 
        REFERENCES Produit(idProduit)
        ON DELETE CASCADE
);


/* CREATE OR REPLACE TRIGGER Verif_Suppression_Client
-- Trigger pour vérifier si un client a des commandes avant suppression
BEFORE DELETE ON Client -- avant la suppression d'un client (une ligne de la table Client)
FOR EACH ROW
BEGIN
    DECLARE
        nb_cmd INT;
    BEGIN
        SELECT COUNT(*) INTO nb_cmd
        FROM Commande
        WHERE idClient = :OLD.idClient; -- Utilisation de :OLD pour accéder a l'ancienne valeur de idClient

        IF nb_cmd > 0 THEN
            RAISE_APPLICATION_ERROR(-31, 'Ce client a encore des commandes.');
        END IF;
    END;
END;
/ */


/* CREATE OR REPLACE TRIGGER Verif_sous_total_ligneP
BEFORE INSERT OR UPDATE ON LigneCommandeProduit
FOR EACH ROW
BEGIN
    IF :NEW.SousTotalLigneP <> :NEW.QuantiteCommandeeP * :NEW.PrixUnitaireP THEN
        RAISE_APPLICATION_ERROR(-32, 'Le sous-total de la ligne de commande produit est incorrect.');
    END IF;
END;
/ */

/* CREATE OR REPLACE TRIGGER Verif_sous_total_ligneC
BEFORE INSERT OR UPDATE ON LigneCommandeContenant
FOR EACH ROW
BEGIN
    IF :NEW.SousTotalLigneC <> :NEW.QuantiteCommandeeC * :NEW.PrixUnitaireC THEN
        RAISE_APPLICATION_ERROR(-32, 'Le sous-total de la ligne de commande contenant est incorrect.');
    END IF;
END;
/ */


/* CREATE OR REPLACE TRIGGER verif_statut_commande
BEFORE UPDATE ON CommandeaLivrer
FOR EACH ROW
BEGIN
    IF :OLD.StatutCommandeL IN ('Livree', 'Annulee')
       AND :NEW.StatutCommandeL NOT IN ('Livree', 'Annulee') THEN
        RAISE_APPLICATION_ERROR(-33, 'Statut invalide : une commande livree ou annulee ne peut pas être réouverte.');
    END IF;
END;
/ */

/* CREATE OR REPLACE TRIGGER Verif_commandeP_stock
BEFORE INSERT OR UPDATE ON LigneCommandeProduit
FOR EACH ROW
DECLARE
    stock_disponible FLOAT;
BEGIN
    SELECT StockProduit INTO stock_disponible
    FROM Produit WHERE idProduit = :NEW.idProduit;

    IF :NEW.QuantiteCommandeeP > stock_disponible THEN
        RAISE_APPLICATION_ERROR(-34, 'Quantité commandée pour ce produit > stock disponible');
    END IF;
END;
/ */

/* CREATE OR REPLACE TRIGGER Verif_commandeC_stock
BEFORE INSERT OR UPDATE ON LigneCommandeContenant
FOR EACH ROW
DECLARE
    stock_disponible FLOAT;
BEGIN
    SELECT StockDisponible INTO stock_disponible
    FROM Contenant WHERE idContenant = :NEW.idContenant;

    IF :NEW.QuantiteCommandeeC > stock_disponible THEN
        RAISE_APPLICATION_ERROR(-35, 'Quantité commandée pour ce contenant > stock disponible');
    END IF;
END;
/ */

/* CREATE OR REPLACE TRIGGER Verif_perte_produit_stock
BEFORE INSERT OR UPDATE ON PerteProduit
FOR EACH ROW
DECLARE
    stock_dispo INT;
BEGIN
    SELECT StockProduit INTO stock_dispo
    FROM Produit WHERE idProduit = :NEW.idProduit;

    IF :NEW.QuantitePerdueP > stock_dispo THEN
        RAISE_APPLICATION_ERROR(-36, 'Quantité perdue pour ce produit > stock disponible');
    END IF;
END;
/
 */

/* CREATE OR REPLACE TRIGGER Verif_perte_contenant_stock
BEFORE INSERT OR UPDATE ON PerteContenant
FOR EACH ROW
DECLARE
    stock_dispo INT;
BEGIN
    SELECT StockDisponible INTO stock_dispo
    FROM Contenant WHERE idContenant = :NEW.idContenant;

    IF :NEW.QuantitePerdueC > stock_dispo THEN
        RAISE_APPLICATION_ERROR(-36, 'Quantité perdue pour ce contenant > stock disponible');
    END IF;
END;
/ */


/* CREATE OR REPLACE TRIGGER verif_stock_produit
BEFORE INSERT OR UPDATE ON LotProduit
FOR EACH ROW
DECLARE
    total_stock FLOAT;
    stock_produit FLOAT;
BEGIN
    SELECT NVL(SUM(QuantiteDisponibleP), 0)
    INTO total_stock
    FROM LotProduit
    WHERE idProduit = :NEW.idProduit
      AND (DateReceptionP != :OLD.DateReceptionP OR :OLD.DateReceptionP IS NULL);

    total_stock := total_stock + :NEW.QuantiteDisponibleP;

    SELECT StockProduit INTO stock_produit
    FROM Produit WHERE idProduit = :NEW.idProduit;

    IF total_stock != stock_produit THEN
        RAISE_APPLICATION_ERROR(-37, 'Erreur : la somme des quantités des lots ne correspond pas au stock du produit.');
    END IF;
END;
/
 */

/* CREATE OR REPLACE TRIGGER verif_stock_contenant
BEFORE INSERT OR UPDATE ON LotContenant
FOR EACH ROW
DECLARE
    total_stock FLOAT;
    stock_contenant FLOAT;
BEGIN
    SELECT NVL(SUM(QuantiteDisponibleC), 0)
    INTO total_stock
    FROM LotContenant
    WHERE idContenant = :NEW.idContenant
      AND (DateReceptionC != :OLD.DateReceptionC OR :OLD.DateReceptionC IS NULL);

    total_stock := total_stock + :NEW.QuantiteDisponibleC;

    SELECT StockDisponible INTO stock_contenant
    FROM Contenant WHERE idContenant = :NEW.idContenant;

    IF total_stock != stock_contenant THEN
        RAISE_APPLICATION_ERROR(-37, 'Erreur : la somme des quantités des lots ne correspond pas au stock du contenant.');
    END IF;
END;
/ */
/* commit;

DROP TRIGGER TRG_CREATE_CLIENT_ANONYME;
DROP TRIGGER VERIF_COMMANDEC_STOCK;
DROP TRIGGER VERIF_commandep_stock;
DROP trigger verif_perte_contenant_stock;
DROP trigger verif_perte_produit_stock;
DROP trigger verif_sous_total_lignec;
DROP trigger verif_sous_total_lignep;
DROP trigger verif_statut_commande;
DROP trigger verif_stock_contenant;
DROP trigger verif_stock_produit;
DROP trigger verif_suppression_client;

-- Pour determiner la table faisant figurer une contrainte check dont on a le nom
SELECT 
    uc.constraint_name,
    uc.table_name,
    ucc.column_name
FROM user_constraints uc
JOIN user_cons_columns ucc
    ON uc.constraint_name = ucc.constraint_name
WHERE uc.constraint_name = 'SYS_C001323422'; */

-- Pour déterminer le nom d'une contrainte check
/* SELECT constraint_name
FROM user_constraints
WHERE table_name = 'PRODUIT'
  AND constraint_type = 'C'; -- 'C' is for check
 */
/*  ALTER TABLE clientapouradresselivraison
MODIFY emailclient VARCHAR2(255);
commit; */