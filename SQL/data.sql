-- un script d’insertion
-- ============================================================
-- Jeu de données exemple pour le projet BD 2025-2026 - "Au Valaisan"
-- ============================================================


DROP TABLE Producteurapouractivite;
DROP TABLE Producteur;
DROP TABLE Activité;
DROP TABLE AdressePComplete;
DROP TABLE LigneCommandeProduit CASCADE CONSTRAINTS;
DROP TABLE LigneCommandeContenant CASCADE CONSTRAINTS;
DROP TABLE CommandeàLivrer CASCADE CONSTRAINTS;
DROP TABLE CommandeenBoutique CASCADE CONSTRAINTS;
DROP TABLE ProduitAPourCaractéristique CASCADE CONSTRAINTS;
DROP TABLE ProduitAPourSaison CASCADE CONSTRAINTS;
DROP TABLE Producteurapouractivite CASCADE CONSTRAINTS;
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
DROP TABLE AdressePComplete CASCADE CONSTRAINTS;

-- === 1. Adresses des producteurs ===
INSERT INTO AdressePComplete VALUES (46.22, 7.35); -- Sion
INSERT INTO AdressePComplete VALUES (46.10, 7.22); -- Martigny
INSERT INTO AdressePComplete VALUES (46.25, 7.45); -- Fully
INSERT INTO AdressePComplete VALUES (46.20, 7.40); -- Sierre
INSERT INTO AdressePComplete VALUES (46.15, 7.30); -- Bagnes
INSERT INTO AdressePComplete (Latitude, Longitude) VALUES (48.8566, 2.3522); -- Paris
INSERT INTO AdressePComplete (Latitude, Longitude) VALUES (50.8503, 4.3517); -- Bruxelles
INSERT INTO AdressePComplete (Latitude, Longitude) VALUES (52.5200, 13.4050); --Berlin
INSERT INTO AdressePComplete (Latitude, Longitude) VALUES (41.9028, 12.4964); -- Rome
INSERT INTO AdressePComplete (Latitude, Longitude) VALUES (40.4168, -3.7038); -- Madrid
INSERT INTO AdressePComplete (Latitude, Longitude) VALUES (52.3676, 4.9041); -- Amsterdam
INSERT INTO AdressePComplete (Latitude, Longitude) VALUES (46.9481, 7.4474); -- Suisse


-- === 2. Producteurs ===
INSERT INTO Producteur VALUES (1, 'ferme.valais@exemple.com', 'Roux', 'Jean', 46.22, 7.35);
INSERT INTO Producteur VALUES (2, 'fromagerie.martigny@exemple.com', 'Berthier', 'Luc', 46.10, 7.22);
INSERT INTO Producteur VALUES (3, 'huile.fully@exemple.com', 'Favre', 'Claire', 46.25, 7.45);
INSERT INTO Producteur VALUES (4, 'fromagerie.sierre@exemple.com', 'Dupont', 'Marc', 46.20, 7.40);
INSERT INTO Producteur VALUES (5, 'ferme.bagnes@exemple.com', 'Gobet', 'Sophie', 46.15, 7.30);
INSERT INTO Producteur (idProducteur, Email, NomProducteur, PrenomProducteur, Latitude, Longitude) 
VALUES (6, 'ferme.paris@exemple.com', 'Martin', 'Paul', 48.8566, 2.3522); 
INSERT INTO Producteur (idProducteur, Email, NomProducteur, PrenomProducteur, Latitude, Longitude) 
VALUES (7, 'fromagerie.bruxelles@exemple.com', 'Lemoine', 'Anne', 50.8503, 4.3517);
INSERT INTO Producteur (idProducteur, Email, NomProducteur, PrenomProducteur, Latitude, Longitude) 
VALUES (8, 'huile.berlin@exemple.com', 'Schmidt', 'Karl', 52.5200, 13.4050);
INSERT INTO Producteur (idProducteur, Email, NomProducteur, PrenomProducteur, Latitude, Longitude) 
VALUES (9, 'fromagerie.rome@exemple.com', 'Bianchi', 'Luca', 41.9028, 12.4964);
INSERT INTO Producteur (idProducteur, Email, NomProducteur, PrenomProducteur, Latitude, Longitude) 
VALUES (10, 'ferme.madrid@exemple.com', 'Garcia', 'Maria', 40.4168, -3.7038);
INSERT INTO Producteur (idProducteur, Email, NomProducteur, PrenomProducteur, Latitude, Longitude) 
VALUES (11, 'ferme.amsterdam@exemple.com', 'Jansen', 'Henk', 52.3676, 4.9041);
INSERT INTO Producteur (idProducteur, Email, NomProducteur, PrenomProducteur, Latitude, Longitude) 
VALUES (12, 'fromagerie.berne@exemple.com', 'Meier', 'Sophie', 46.9481, 7.4474);


-- === 3. Activités ===
INSERT INTO Activité VALUES ('Agriculteur');
INSERT INTO Activité VALUES ('Eleveur');
INSERT INTO Activité VALUES ('Fromager');

-- === 4. Producteur-Activité ===
INSERT INTO Producteurapouractivite VALUES (1, 'Agriculteur');
INSERT INTO Producteurapouractivite VALUES (2, 'Fromager');
INSERT INTO Producteurapouractivite VALUES (3, 'Agriculteur');
INSERT INTO Producteurapouractivite VALUES (4, 'Fromager');
INSERT INTO Producteurapouractivite VALUES (5, 'Agriculteur');
-- Table Producteurapouractivite (idProducteur, TypeActivité)

-- Nouveau producteurs : 
-- 6 : Paris -> Agriculteur
-- 7 : Bruxelles -> Fromager
-- 8 : Berlin -> Agriculteur
-- 9 : Rome -> Fromager
-- 10 : Madrid -> Agriculteur
-- 11 : Amsterdam -> Eleveur
-- 12 : Berne -> Fromager

INSERT INTO Producteurapouractivite (idProducteur, TypeActivité) VALUES (6, 'Agriculteur');
INSERT INTO Producteurapouractivite (idProducteur, TypeActivité) VALUES (7, 'Fromager');
INSERT INTO Producteurapouractivite (idProducteur, TypeActivité) VALUES (8, 'Agriculteur');
INSERT INTO Producteurapouractivite (idProducteur, TypeActivité) VALUES (9, 'Fromager');
INSERT INTO Producteurapouractivite (idProducteur, TypeActivité) VALUES (10, 'Agriculteur');
INSERT INTO Producteurapouractivite (idProducteur, TypeActivité) VALUES (11, 'Eleveur');
INSERT INTO Producteurapouractivite (idProducteur, TypeActivité) VALUES (12, 'Fromager');


-- === 5. Produits ===
INSERT INTO Produit VALUES (1, 'Pommes du Valais', 'fruits secs', 'Pommes séchées bio du Valais', 1);
INSERT INTO Produit VALUES (2, 'Raclette AOP', 'Fromage', 'Fromage à raclette AOP demi-meule', 2);
INSERT INTO Produit VALUES (3, 'Huile de noix', 'huile', 'Huile pressée à froid, locale', 3);
INSERT INTO Produit VALUES (4, 'Lentilles vertes', 'legumineuse', 'Lentilles vertes de la ferme Roux', 1);
INSERT INTO Produit VALUES (5, 'Miel de montagne', 'boisson', 'Miel pur de montagne bio', 4);
INSERT INTO Produit VALUES (6, 'Châtaignes', 'fruits secs', 'Châtaignes locales grillées', 5);
INSERT INTO Produit VALUES (7, 'Fromage de chèvre', 'Fromage', 'Fromage de chèvre artisanal', 4);

SELECT * FROM Produit;
-- === 6. Saisons de disponibilité ===
INSERT INTO SaisonDisponibilité VALUES (TO_DATE('2025-06-15','YYYY-MM-DD'), TO_DATE('2025-09-30','YYYY-MM-DD')); -- été
INSERT INTO SaisonDisponibilité VALUES (TO_DATE('2025-11-01','YYYY-MM-DD'), TO_DATE('2026-03-31','YYYY-MM-DD')); -- hiver

-- Produits saisonniers
INSERT INTO ProduitAPourSaison VALUES (1, TO_DATE('2025-06-15','YYYY-MM-DD'), TO_DATE('2025-09-30','YYYY-MM-DD'));
INSERT INTO ProduitAPourSaison VALUES (2, TO_DATE('2025-11-01','YYYY-MM-DD'), TO_DATE('2026-03-31','YYYY-MM-DD'));

-- === 7. Caractéristiques ===
INSERT INTO Caracteristique VALUES (1, 'bio', 'oui');
INSERT INTO Caracteristique VALUES (1, 'pays d’origine', 'Suisse');
INSERT INTO Caracteristique VALUES (2, 'allergènes', 'lactose');
INSERT INTO Caracteristique VALUES (5, 'bio', 'oui');
INSERT INTO Caracteristique VALUES (6, 'pays d’origine', 'Suisse'); -- switzerland for life
INSERT INTO Caracteristique VALUES (6, 'bio', 'non');
INSERT INTO Caracteristique VALUES (7, 'allergènes', 'lactose');

-- === 8. Contenants ===
INSERT INTO Contenant VALUES (1, 'bocal en verre', 1.0, 50, 1);
INSERT INTO Contenant VALUES (2, 'sachet kraft', 0.5, 100, 0);
INSERT INTO Contenant VALUES (3, 'sachet tissu', 0.3, 80, 1);
INSERT INTO Contenant VALUES (4, 'papier cire', 0.2, 150, 0);
INSERT INTO Contenant VALUES (5, 'autre', 0.75, 60, 1);

-- === 9. Lots de contenants ===
INSERT INTO LotContenant VALUES (TO_DATE('2025-10-01','YYYY-MM-DD'), 1, 50, 3.00);
INSERT INTO LotContenant VALUES (TO_DATE('2025-10-01','YYYY-MM-DD'), 2, 100, 0.50);
INSERT INTO LotContenant VALUES (TO_DATE('2025-10-05','YYYY-MM-DD'), 3, 80, 1.20);
INSERT INTO LotContenant VALUES (TO_DATE('2025-10-10','YYYY-MM-DD'), 4, 150, 0.20);
INSERT INTO LotContenant VALUES (TO_DATE('2025-10-12','YYYY-MM-DD'), 5, 60, 0.75);

-- === 10. Lots de produits ===
INSERT INTO LotProduit VALUES ('vrac', 1, TO_DATE('2025-10-10','YYYY-MM-DD'), 50, 'DLUO', TO_DATE('2025-11-14','YYYY-MM-DD'), 10.00, 6.00, 1);
INSERT INTO LotProduit VALUES ('preconditionne', 0.5, TO_DATE('2025-10-12','YYYY-MM-DD'), 40, 'DLC', TO_DATE('2026-01-20','YYYY-MM-DD'), 25.00, 15.00, 2);
INSERT INTO LotProduit VALUES ('vrac', 1, TO_DATE('2025-09-15','YYYY-MM-DD'), 30, 'DLUO', TO_DATE('2026-09-15','YYYY-MM-DD'), 12.00, 7.00, 3);
INSERT INTO LotProduit VALUES ('vrac', 1, TO_DATE('2025-10-01','YYYY-MM-DD'), 80, 'DLUO', TO_DATE('2026-05-01','YYYY-MM-DD'), 4.50, 2.00, 4);
INSERT INTO LotProduit VALUES ('vrac', 1, TO_DATE('2025-10-18','YYYY-MM-DD'), 60, 'DLUO', TO_DATE('2026-02-01','YYYY-MM-DD'), 15.00, 8.00, 5);
INSERT INTO LotProduit VALUES ('preconditionne', 0.5, TO_DATE('2025-10-20','YYYY-MM-DD'), 40, 'DLC', TO_DATE('2026-01-15','YYYY-MM-DD'), 10.00, 5.00, 6);
INSERT INTO LotProduit VALUES ('vrac', 0.3, TO_DATE('2025-10-25','YYYY-MM-DD'), 25, 'DLUO', TO_DATE('2026-03-01','YYYY-MM-DD'), 12.00, 7.50, 7);

SELECT * FROM LotProduit;
-- === 11. Clients anonymes ===
INSERT INTO ClientAnonyme VALUES (101);
INSERT INTO ClientAnonyme VALUES (102);
INSERT INTO ClientAnonyme VALUES (103);
INSERT INTO ClientAnonyme VALUES (104);

-- === 12. Clients ===
INSERT INTO Client VALUES ('alice@exemple.com', 'Durand', 'Alice', '0791234567', 101);
INSERT INTO Client VALUES ('bob@exemple.com', 'Martin', 'Bob', '0799876543', 102);
INSERT INTO Client VALUES ('carole@exemple.com', 'Petit', 'Carole', '0791112233', 103);
INSERT INTO Client VALUES ('david@exemple.com', 'Lemoine', 'David', '0794445566', 104);

-- === 13. Adresses de livraison ===
INSERT INTO AdresseLivraison VALUES ('12 Rue des Alpes, Sion');
INSERT INTO AdresseLivraison VALUES ('3 Chemin du Rhône, Martigny');
INSERT INTO AdresseLivraison VALUES ('8 Avenue du Rhône, Sierre');
INSERT INTO AdresseLivraison VALUES ('15 Rue Centrale, Bagnes');

-- === 13. Clients et adresses de livraison ===
INSERT INTO ClientAPourAdresseLivraison VALUES ('alice@exemple.com', '12 Rue des Alpes, Sion');
INSERT INTO ClientAPourAdresseLivraison VALUES ('bob@exemple.com', '3 Chemin du Rhône, Martigny');
INSERT INTO ClientAPourAdresseLivraison VALUES ('carole@exemple.com', '8 Avenue du Rhône, Sierre');
INSERT INTO ClientAPourAdresseLivraison VALUES ('david@exemple.com', '15 Rue Centrale, Bagnes');

-- === 14. Commandes ===
INSERT INTO Commande VALUES (1001, TO_DATE('2025-10-15','YYYY-MM-DD'), TO_DATE('2025-10-15 14:00:00','YYYY-MM-DD HH24:MI:SS'), 'En ligne', 'Livraison', 101);
INSERT INTO Commande VALUES (1002, TO_DATE('2025-10-16','YYYY-MM-DD'), TO_DATE('2025-10-16 11:00:00','YYYY-MM-DD HH24:MI:SS'), 'En boutique', 'Retrait', 102);
INSERT INTO Commande VALUES (1003, TO_DATE('2025-10-17','YYYY-MM-DD'), TO_DATE('2025-10-17 09:00:00','YYYY-MM-DD HH24:MI:SS'), 'En ligne', 'Livraison', 103);
INSERT INTO Commande VALUES (1004, TO_DATE('2025-10-18','YYYY-MM-DD'), TO_DATE('2025-10-18 10:30:00','YYYY-MM-DD HH24:MI:SS'), 'En boutique', 'Retrait', 104);

-- === 15. Commandes à livrer et en boutique ===
INSERT INTO CommandeaLivrer VALUES (1001, 'En preparation', 5.50, TO_DATE('2025-10-20','YYYY-MM-DD'), '12 Rue des Alpes, Sion');
INSERT INTO CommandeenBoutique VALUES (1002, 'Prete');
INSERT INTO CommandeaLivrer VALUES (1003, 'En preparation', 4.50, TO_DATE('2025-10-22','YYYY-MM-DD'), '8 Avenue du Rhône, Sierre');
INSERT INTO CommandeenBoutique VALUES (1004, 'Prete');

SELECT * FROM CommandeaLivrer;

ALTER TABLE CommandeaLivrer 
DROP CONSTRAINT SYS_C001323404;



-- === 16. Lignes de commande produits ===
INSERT INTO LigneCommandeProduit VALUES (1, 1001, 1, 'vrac', 1, TO_DATE('2025-10-10','YYYY-MM-DD'), 2, 10.00, 20.00);
INSERT INTO LigneCommandeProduit VALUES (1, 1002, 2, 'preconditionne', 0.5, TO_DATE('2025-10-12','YYYY-MM-DD'), 1, 25.00, 25.00);
INSERT INTO LigneCommandeProduit VALUES (1, 1003, 5, 'vrac', 1, TO_DATE('2025-10-18','YYYY-MM-DD'), 3, 15.00, 45.00);
INSERT INTO LigneCommandeProduit VALUES (1, 1004, 6, 'preconditionne', 0.5, TO_DATE('2025-10-20','YYYY-MM-DD'), 5, 10.00, 50.00);

SELECT * FROM LigneCommandeProduit;


-- === 17. Lignes de commande contenants ===
INSERT INTO LigneCommandeContenant VALUES (1, 1001, 1, TO_DATE('2025-10-01','YYYY-MM-DD'), 1, 3.00, 3.00);
INSERT INTO LigneCommandeContenant VALUES (1, 1002, 2, TO_DATE('2025-10-01','YYYY-MM-DD'), 2, 0.50, 1.00);
INSERT INTO LigneCommandeContenant VALUES (1, 1003, 4, TO_DATE('2025-10-10','YYYY-MM-DD'), 10, 0.20, 2.00);
INSERT INTO LigneCommandeContenant VALUES (1, 1004, 5, TO_DATE('2025-10-12','YYYY-MM-DD'), 2, 0.75, 1.50);

-- === 18. Pertes ===
INSERT INTO PerteProduit VALUES (1, 4, TO_DATE('2025-10-20','YYYY-MM-DD'), 2, 'casse');
INSERT INTO PerteContenant VALUES (1, 3, TO_DATE('2025-10-22','YYYY-MM-DD'), 1, 'vol');
INSERT INTO PerteProduit VALUES (2, 6, TO_DATE('2025-10-25','YYYY-MM-DD'), 1, 'casse');
INSERT INTO PerteContenant VALUES (2, 4, TO_DATE('2025-10-26','YYYY-MM-DD'), 5, 'vol');

COMMIT;

INSERT INTO Produit (idProduit, NomProduit, CategorieProduit, DescriptionProduit, idProducteur)
VALUES (8, 'Yaourt nature fermier', 'laitage',
        'Yaourt artisanal au lait entier de ferme', 7);

INSERT INTO Produit VALUES
(10, 'Jus d’orange frais', 'boisson',
 'Jus pressé artisanal 100% orange', 6);

INSERT INTO Produit VALUES
(11, 'Riz basmati premium', 'cereales',
 'Riz basmati long grain parfumé', 12);

INSERT INTO Produit VALUES
(12, 'Haricots rouges', 'legumineuse',
 'Haricots rouges secs de qualité', 9);

INSERT INTO Produit VALUES
(13, 'Amandes grillées', 'fruits secs',
 'Amandes grillées sans sel', 8);

INSERT INTO Produit VALUES
(14, 'Huile d’olive vierge', 'huile',
 'Huile d’olive pressée à froid', 10);

INSERT INTO Produit VALUES
(15, 'Steak haché bio', 'viande',
 'Bœuf élevé en plein air', 11);

INSERT INTO Produit VALUES
(16, 'Lait demi-écrémé', 'laitage',
 'Lait frais demi-écrémé pasteurisé', 2);

INSERT INTO Produit VALUES
(17, 'Tomates cerises', 'legume',
 'Tomates cerises locales', 11);

INSERT INTO Produit VALUES
(18, 'Pommes Golden', 'fruit',
 'Pommes Golden croquantes', 5);

INSERT INTO Produit VALUES
(20, 'Spaghetti artisanal', 'pates',
 'Pâtes produites de manière traditionnelle', 3);

select * from produit order by idproduit;

-- Produit 1 : Pommes du Valais
INSERT INTO LotProduit VALUES
('vrac', 1, TO_DATE('01-NOV-25','DD-MON-RR'), 30, 'DLUO', TO_DATE('05-DEC-25','DD-MON-RR'), 4, 2, 1, 0),
('preconditionne', 0.5, TO_DATE('10-NOV-25','DD-MON-RR'), 20, 'DLUO', TO_DATE('30-NOV-25','DD-MON-RR'), 5, 2.5, 1, 1);

-- Produit 2 : Raclette AOP
INSERT INTO LotProduit VALUES
('vrac', 1, TO_DATE('15-SEP-25','DD-MON-RR'), 32.928, 'DLUO', TO_DATE('15-SEP-26','DD-MON-RR'), 7, 3, 2, 0);

-- Produit 3 : Huile de noix
INSERT INTO LotProduit VALUES
('vrac', 1, TO_DATE('18-OCT-25','DD-MON-RR'), 41.16, 'DLUO', TO_DATE('01-FEB-26','DD-MON-RR'), 8, 5, 3, 0);

-- Produit 4 : Lentilles vertes
INSERT INTO LotProduit VALUES
('vrac', 1, TO_DATE('25-OCT-25','DD-MON-RR'), 32.928, 'DLUO', TO_DATE('01-MAR-26','DD-MON-RR'), 7.5, 7, 4, 0);

-- Produit 5 : Miel de montagne
INSERT INTO LotProduit VALUES
('preconditionne', 0.5, TO_DATE('20-OCT-25','DD-MON-RR'), 27.44, 'DLC', TO_DATE('15-JAN-26','DD-MON-RR'), 5, 6, 5, 0),
('preconditionne', 0.5, TO_DATE('12-OCT-25','DD-MON-RR'), 68.6, 'DLC', TO_DATE('30-NOV-25','DD-MON-RR'), 15, 2, 5, 1);

-- Produit 6 : Châtaignes
INSERT INTO LotProduit VALUES
('vrac', 1, TO_DATE('18-OCT-25','DD-MON-RR'), 40, 'DLUO', TO_DATE('01-FEB-26','DD-MON-RR'), 10, 5, 6, 0);

-- Produit 7 : Fromage de chèvre
INSERT INTO LotProduit VALUES
('vrac', 1, TO_DATE('10-OCT-25','DD-MON-RR'), 30, 'DLUO', TO_DATE('30-NOV-25','DD-MON-RR'), 9, 6, 7, 1);

-- Produit 8 : Yaourt nature fermier
INSERT INTO LotProduit VALUES
('preconditionne', 0.5, TO_DATE('20-NOV-25','DD-MON-RR'), 50, 'DLC', TO_DATE('27-NOV-25','DD-MON-RR'), 2.5, 1.8, 8, 1);

-- Produit 9 : Msmn marocain
INSERT INTO LotProduit VALUES
('vrac', 1, TO_DATE('05-OCT-25','DD-MON-RR'), 25, 'DLUO', TO_DATE('05-DEC-25','DD-MON-RR'), 5, 3, 9, 0);

-- Produit 10 : Jus d’orange frais
INSERT INTO LotProduit VALUES
('preconditionne', 0.5, TO_DATE('15-NOV-25','DD-MON-RR'), 40, 'DLC', TO_DATE('22-NOV-25','DD-MON-RR'), 3.5, 2.2, 10, 1);

-- Produit 11 : Riz basmati premium
INSERT INTO LotProduit VALUES
('vrac', 1, TO_DATE('01-OCT-25','DD-MON-RR'), 50, 'DLUO', TO_DATE('01-SEP-26','DD-MON-RR'), 8, 5, 11, 0);

-- Produit 12 : Haricots rouges
INSERT INTO LotProduit VALUES
('vrac', 1, TO_DATE('05-NOV-25','DD-MON-RR'), 30, 'DLUO', TO_DATE('05-DEC-25','DD-MON-RR'), 6, 3, 12, 1);

-- Produit 13 : Amandes grillées
INSERT INTO LotProduit VALUES
('vrac', 1, TO_DATE('10-OCT-25','DD-MON-RR'), 25, 'DLUO', TO_DATE('10-DEC-25','DD-MON-RR'), 7, 4, 13, 0);

-- Produit 14 : Huile d’olive vierge
INSERT INTO LotProduit VALUES
('vrac', 1, TO_DATE('15-OCT-25','DD-MON-RR'), 20, 'DLUO', TO_DATE('15-JAN-26','DD-MON-RR'), 10, 6, 14, 0);

-- Produit 15 : Steak haché bio
INSERT INTO LotProduit VALUES
('vrac', 1, TO_DATE('20-OCT-25','DD-MON-RR'), 15, 'DLUO', TO_DATE('20-NOV-25','DD-MON-RR'), 12, 8, 15, 1);

-- Produit 16 : Lait demi-écrémé
INSERT INTO LotProduit VALUES
('preconditionne', 0.5, TO_DATE('10-NOV-25','DD-MON-RR'), 50, 'DLC', TO_DATE('17-NOV-25','DD-MON-RR'), 1.5, 1, 16, 1);

-- Produit 17 : Tomates cerises
INSERT INTO LotProduit VALUES
('vrac', 1, TO_DATE('01-NOV-25','DD-MON-RR'), 20, 'DLUO', TO_DATE('01-DEC-25','DD-MON-RR'), 4, 2.5, 17, 1);

-- Produit 18 : Pommes Golden
INSERT INTO LotProduit VALUES
('vrac', 1, TO_DATE('15-OCT-25','DD-MON-RR'), 30, 'DLUO', TO_DATE('15-DEC-25','DD-MON-RR'), 5, 3, 18, 0);

-- Produit 20 : Spaghetti artisanal
INSERT INTO LotProduit VALUES
('vrac', 1, TO_DATE('01-NOV-25','DD-MON-RR'), 40, 'DLUO', TO_DATE('01-DEC-25','DD-MON-RR'), 6, 3, 20, 0);

-- Exemple produit 1 : Pommes du Valais, deux lots préconditionnés
INSERT INTO LotProduit (
    ModeConditionnement, PoidsUnitaire, DateReceptionP, QuantiteDisponibleP,
    DatePeremptionType, DatePeremption, PrixVentePTTC, PrixAchatProducteur,
    idProduit, REMISE_APPLIQUEE
) VALUES (
    'preconditionne', 0.75, TO_DATE('15-NOV-25','DD-MON-RR'), 15,
    'DLUO', TO_DATE('22-NOV-25','DD-MON-RR'), 2.5, 2.2,
    1, 1
);

INSERT INTO LotProduit (
    ModeConditionnement, PoidsUnitaire, DateReceptionP, QuantiteDisponibleP,
    DatePeremptionType, DatePeremption, PrixVentePTTC, PrixAchatProducteur,
    idProduit, REMISE_APPLIQUEE
) VALUES (
    'preconditionne', 2.5, TO_DATE('20-NOV-25','DD-MON-RR'), 10,
    'DLUO', TO_DATE('28-NOV-25','DD-MON-RR'), 3, 2.5,
    1, 1
);

INSERT INTO LotProduit (
    ModeConditionnement, PoidsUnitaire, DateReceptionP, QuantiteDisponibleP,
    DatePeremptionType, DatePeremption, PrixVentePTTC, PrixAchatProducteur, idProduit, REMISE_APPLIQUEE
) VALUES (
    'preconditionne', 0.75, TO_DATE('10-NOV-25','DD-MON-RR'), 20,
    'DLUO', TO_DATE('18-NOV-25','DD-MON-RR'), 8.5, 7, 2, 1
);

INSERT INTO LotProduit (
    ModeConditionnement, PoidsUnitaire, DateReceptionP, QuantiteDisponibleP,
    DatePeremptionType, DatePeremption, PrixVentePTTC, PrixAchatProducteur, idProduit, REMISE_APPLIQUEE
) VALUES (
    'vrac', 1, TO_DATE('12-NOV-25','DD-MON-RR'), 30,
    'DLUO', TO_DATE('22-NOV-25','DD-MON-RR'), 7.5, 6.5, 2, 1
);


-- Produit 15 : Steak haché bio (idProduit = 15)
INSERT INTO LotProduit (
    ModeConditionnement, PoidsUnitaire, DateReceptionP, QuantiteDisponibleP,
    DatePeremptionType, DatePeremption, PrixVentePTTC, PrixAchatProducteur, idProduit, REMISE_APPLIQUEE
) VALUES (
    'preconditionne', 2.5, TO_DATE('20-NOV-25','DD-MON-RR'), 25,
    'DLC', TO_DATE('15-DEC-25','DD-MON-RR'), 12.5, 10, 15, 1
);

INSERT INTO LotProduit (
    ModeConditionnement, PoidsUnitaire, DateReceptionP, QuantiteDisponibleP,
    DatePeremptionType, DatePeremption, PrixVentePTTC, PrixAchatProducteur, idProduit, REMISE_APPLIQUEE
) VALUES (
    'preconditionne', 3, TO_DATE('22-NOV-25','DD-MON-RR'), 30,
    'DLC', TO_DATE('18-DEC-25','DD-MON-RR'), 15, 12, 15, 1
);

-- Produit 17 : Tomates cerises (idProduit = 17)
INSERT INTO LotProduit (
    ModeConditionnement, PoidsUnitaire, DateReceptionP, QuantiteDisponibleP,
    DatePeremptionType, DatePeremption, PrixVentePTTC, PrixAchatProducteur, idProduit, REMISE_APPLIQUEE
) VALUES (
    'preconditionne', 2.5, TO_DATE('21-NOV-25','DD-MON-RR'), 18,
    'DLUO', TO_DATE('29-NOV-25','DD-MON-RR'), 6.5, 5, 17, 1
);

INSERT INTO LotProduit (
    ModeConditionnement, PoidsUnitaire, DateReceptionP, QuantiteDisponibleP,
    DatePeremptionType, DatePeremption, PrixVentePTTC, PrixAchatProducteur, idProduit, REMISE_APPLIQUEE
) VALUES (
    'preconditionne', 3, TO_DATE('23-NOV-25','DD-MON-RR'), 20,
    'DLUO', TO_DATE('01-DEC-25','DD-MON-RR'), 7.5, 5.5, 17, 1
);



-- Ligne 1
UPDATE LotProduit
SET QuantiteDisponibleP = 20
WHERE ModeConditionnement = 'vrac' 
  AND PoidsUnitaire = 1
  AND DateReceptionP = TO_DATE('10-OCT-25','DD-MON-RR')
  AND idProduit = 9;

-- Ligne 2
UPDATE LotProduit
SET QuantiteDisponibleP = 15
WHERE ModeConditionnement = 'vrac' 
  AND PoidsUnitaire = 1
  AND DateReceptionP = TO_DATE('10-OCT-25','DD-MON-RR')
  AND idProduit = 1;

-- Ligne 3
UPDATE LotProduit
SET QuantiteDisponibleP = 30
WHERE ModeConditionnement = 'vrac' 
  AND PoidsUnitaire = 1
  AND DateReceptionP = TO_DATE('15-SEP-25','DD-MON-RR')
  AND idProduit = 3;

-- Ligne 4
UPDATE LotProduit
SET QuantiteDisponibleP = 25
WHERE ModeConditionnement = 'vrac' 
  AND PoidsUnitaire = 1
  AND DateReceptionP = TO_DATE('18-OCT-25','DD-MON-RR')
  AND idProduit = 5;

-- Ligne 5
UPDATE LotProduit
SET QuantiteDisponibleP = 28
WHERE ModeConditionnement = 'vrac' 
  AND PoidsUnitaire = 1
  AND DateReceptionP = TO_DATE('25-OCT-25','DD-MON-RR')
  AND idProduit = 7;

-- Ligne 6
UPDATE LotProduit
SET QuantiteDisponibleP = 12
WHERE ModeConditionnement = 'preconditionne' 
  AND PoidsUnitaire = 0.5
  AND DateReceptionP = TO_DATE('20-OCT-25','DD-MON-RR')
  AND idProduit = 6;

-- Ligne 7
UPDATE LotProduit
SET QuantiteDisponibleP = 18
WHERE ModeConditionnement = 'preconditionne' 
  AND PoidsUnitaire = 0.5
  AND DateReceptionP = TO_DATE('12-OCT-25','DD-MON-RR')
  AND idProduit = 2;

-- Ligne 1
UPDATE LotProduit
SET QuantiteDisponibleP = 20
WHERE ModeConditionnement = 'vrac' 
  AND PoidsUnitaire = 1
  AND DateReceptionP = TO_DATE('10-OCT-25','DD-MON-RR')
  AND idProduit = 9;

-- Ligne 2
UPDATE LotProduit
SET QuantiteDisponibleP = 15
WHERE ModeConditionnement = 'vrac' 
  AND PoidsUnitaire = 1
  AND DateReceptionP = TO_DATE('10-OCT-25','DD-MON-RR')
  AND idProduit = 1;

-- Ligne 3
UPDATE LotProduit
SET QuantiteDisponibleP = 30
WHERE ModeConditionnement = 'vrac' 
  AND PoidsUnitaire = 1
  AND DateReceptionP = TO_DATE('15-SEP-25','DD-MON-RR')
  AND idProduit = 3;

-- Ligne 4
UPDATE LotProduit
SET QuantiteDisponibleP = 25
WHERE ModeConditionnement = 'vrac' 
  AND PoidsUnitaire = 1
  AND DateReceptionP = TO_DATE('18-OCT-25','DD-MON-RR')
  AND idProduit = 5;

-- Ligne 5
UPDATE LotProduit
SET QuantiteDisponibleP = 28
WHERE ModeConditionnement = 'vrac' 
  AND PoidsUnitaire = 1
  AND DateReceptionP = TO_DATE('25-OCT-25','DD-MON-RR')
  AND idProduit = 7;

-- Ligne 6
UPDATE LotProduit
SET QuantiteDisponibleP = 12
WHERE ModeConditionnement = 'preconditionne' 
  AND PoidsUnitaire = 0.5
  AND DateReceptionP = TO_DATE('20-OCT-25','DD-MON-RR')
  AND idProduit = 6;

-- Ligne 7
UPDATE LotProduit
SET QuantiteDisponibleP = 18
WHERE ModeConditionnement = 'preconditionne' 
  AND PoidsUnitaire = 0.5
  AND DateReceptionP = TO_DATE('12-OCT-25','DD-MON-RR')
  AND idProduit = 2;

  UPDATE LotContenant
SET QUANTITEDISPONIBLEC = 70
WHERE QUANTITEDISPONIBLEC IS NULL;

INSERT INTO ProduitCommande (idProduit, DelaiDisponibiliteHeure)
SELECT idProduit, 48
FROM Produit
WHERE idProduit IN (1, 2, 3, 4, 5)
AND idProduit NOT IN (SELECT idProduit FROM ProduitCommande);

INSERT INTO ProduitStock (idProduit)
SELECT idProduit
FROM Produit
WHERE idProduit NOT IN (SELECT idProduit FROM ProduitCommande)
AND idProduit NOT IN (SELECT idProduit FROM ProduitStock);

INSERT INTO SaisonDisponibilite (DateDebut, DateFin)
VALUES (TO_DATE('01-JAN-25','DD-MON-RR'), TO_DATE('31-MAR-25','DD-MON-RR'));

INSERT INTO SaisonDisponibilite (DateDebut, DateFin)
VALUES (TO_DATE('01-APR-25','DD-MON-RR'), TO_DATE('30-JUN-25','DD-MON-RR'));

INSERT INTO SaisonDisponibilite (DateDebut, DateFin)
VALUES (TO_DATE('01-JUL-25','DD-MON-RR'), TO_DATE('30-SEP-25','DD-MON-RR'));

INSERT INTO SaisonDisponibilite (DateDebut, DateFin)
VALUES (TO_DATE('01-OCT-25','DD-MON-RR'), TO_DATE('31-DEC-25','DD-MON-RR'));


-- Produit 6 : 1 saison
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (6, TO_DATE('01-JAN-25','DD-MON-RR'), TO_DATE('31-MAR-25','DD-MON-RR'));

-- Produit 7 : 2 saisons
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (7, TO_DATE('01-APR-25','DD-MON-RR'), TO_DATE('30-JUN-25','DD-MON-RR'));
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (7, TO_DATE('01-JUL-25','DD-MON-RR'), TO_DATE('30-SEP-25','DD-MON-RR'));

-- Produit 8 : 1 saison
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (8, TO_DATE('01-OCT-25','DD-MON-RR'), TO_DATE('31-DEC-25','DD-MON-RR'));

-- Produit 9 : 2 saisons
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (9, TO_DATE('01-JAN-25','DD-MON-RR'), TO_DATE('31-MAR-25','DD-MON-RR'));
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (9, TO_DATE('01-APR-25','DD-MON-RR'), TO_DATE('30-JUN-25','DD-MON-RR'));

-- Produit 10 : 1 saison
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (10, TO_DATE('01-OCT-25','DD-MON-RR'), TO_DATE('31-DEC-25','DD-MON-RR'));

-- Produit 11 : 3 saisons
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (11, TO_DATE('01-JAN-25','DD-MON-RR'), TO_DATE('31-MAR-25','DD-MON-RR'));
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (11, TO_DATE('01-APR-25','DD-MON-RR'), TO_DATE('30-JUN-25','DD-MON-RR'));
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (11, TO_DATE('01-JUL-25','DD-MON-RR'), TO_DATE('30-SEP-25','DD-MON-RR'));

-- Produit 12 : 2 saisons
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (12, TO_DATE('01-APR-25','DD-MON-RR'), TO_DATE('30-JUN-25','DD-MON-RR'));
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (12, TO_DATE('01-OCT-25','DD-MON-RR'), TO_DATE('31-DEC-25','DD-MON-RR'));

-- Produit 13 : 1 saison
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (13, TO_DATE('01-JUL-25','DD-MON-RR'), TO_DATE('30-SEP-25','DD-MON-RR'));

-- Produit 14 : 2 saisons
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (14, TO_DATE('01-JAN-25','DD-MON-RR'), TO_DATE('31-MAR-25','DD-MON-RR'));
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (14, TO_DATE('01-OCT-25','DD-MON-RR'), TO_DATE('31-DEC-25','DD-MON-RR'));

-- Produit 15 : 1 saison
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (15, TO_DATE('01-APR-25','DD-MON-RR'), TO_DATE('30-JUN-25','DD-MON-RR'));

-- Produit 16 : 1 saison
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (16, TO_DATE('01-JUL-25','DD-MON-RR'), TO_DATE('30-SEP-25','DD-MON-RR'));

-- Produit 17 : 2 saisons
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (17, TO_DATE('01-JAN-25','DD-MON-RR'), TO_DATE('31-MAR-25','DD-MON-RR'));
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (17, TO_DATE('01-APR-25','DD-MON-RR'), TO_DATE('30-JUN-25','DD-MON-RR'));

-- Produit 18 : 1 saison
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (18, TO_DATE('01-OCT-25','DD-MON-RR'), TO_DATE('31-DEC-25','DD-MON-RR'));

-- Produit 19 : 3 saisons
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (19, TO_DATE('01-JAN-25','DD-MON-RR'), TO_DATE('31-MAR-25','DD-MON-RR'));
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (19, TO_DATE('01-APR-25','DD-MON-RR'), TO_DATE('30-JUN-25','DD-MON-RR'));
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (19, TO_DATE('01-JUL-25','DD-MON-RR'), TO_DATE('30-SEP-25','DD-MON-RR'));

-- Produit 20 : 2 saisons
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (20, TO_DATE('01-APR-25','DD-MON-RR'), TO_DATE('30-JUN-25','DD-MON-RR'));
INSERT INTO ProduitAPourSaison (idProduit, DateDebut, DateFin) VALUES (20, TO_DATE('01-OCT-25','DD-MON-RR'), TO_DATE('31-DEC-25','DD-MON-RR'));

INSERT INTO DateRecuperation (DateRecuperation)
VALUES (TO_DATE('30-NOV-25', 'DD-MON-RR'));

INSERT INTO CommandeApourRecup (idCommande, DateRecuperation)
VALUES (4, TO_DATE('30-NOV-25', 'DD-MON-RR'));

INSERT INTO CommandeApourRecup (idCommande, DateRecuperation)
VALUES (5, TO_DATE('30-NOV-25', 'DD-MON-RR'));

INSERT INTO CommandeApourRecup (idCommande, DateRecuperation)
VALUES (3, TO_DATE('30-NOV-25', 'DD-MON-RR'));