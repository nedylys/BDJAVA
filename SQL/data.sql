-- un script d’insertion
-- ============================================================
-- Jeu de données exemple pour le projet BD 2025-2026 - "Au Valaisan"
-- ============================================================


DROP TABLE ProducteurAPourActivité;
DROP TABLE Producteur;
DROP TABLE Activite;
DROP TABLE AdressePComplete;
DROP TABLE LigneCommandeProduit CASCADE CONSTRAINTS;
DROP TABLE LigneCommandeContenant CASCADE CONSTRAINTS;
DROP TABLE CommandeeLivrer CASCADE CONSTRAINTS;
DROP TABLE CommandeenBoutique CASCADE CONSTRAINTS;
DROP TABLE ProduitAPourCaractristique CASCADE CONSTRAINTS;
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
DROP TABLE Activite CASCADE CONSTRAINTS;
DROP TABLE Caracteristique CASCADE CONSTRAINTS;
DROP TABLE SaisonDisponibilité CASCADE CONSTRAINTS;
DROP TABLE Producteur CASCADE CONSTRAINTS;
DROP TABLE AdressePComplete CASCADE CONSTRAINTS;

-- === 1. Adresses des producteurs ===
INSERT INTO AdressePComplete VALUES (46.22, 7.35); -- Sion
INSERT INTO AdressePComplete VALUES (46.10, 7.22); -- Martigny
INSERT INTO AdressePComplete VALUES (46.25, 7.45); -- Fully
INSERT INTO AdressePComplete VALUES (46.20, 7.40); -- Sierre
INSERT INTO AdressePComplete VALUES (46.15, 7.30); -- Bagnes

-- === 2. Producteurs ===
INSERT INTO Producteur VALUES (1, 'ferme.valais@exemple.com', 'Roux', 'Jean', 46.22, 7.35);
INSERT INTO Producteur VALUES (2, 'fromagerie.martigny@exemple.com', 'Berthier', 'Luc', 46.10, 7.22);
INSERT INTO Producteur VALUES (3, 'huile.fully@exemple.com', 'Favre', 'Claire', 46.25, 7.45);
INSERT INTO Producteur VALUES (4, 'fromagerie.sierre@exemple.com', 'Dupont', 'Marc', 46.20, 7.40);
INSERT INTO Producteur VALUES (5, 'ferme.bagnes@exemple.com', 'Gobet', 'Sophie', 46.15, 7.30);

-- === 3. Activités ===
INSERT INTO Activité VALUES ('Agriculteur');
INSERT INTO Activité VALUES ('Éleveur');
INSERT INTO Activité VALUES ('Fromager');

-- === 4. Producteur-Activité ===
INSERT INTO ProducteurAPourActivité VALUES (1, 'Agriculteur');
INSERT INTO ProducteurAPourActivité VALUES (2, 'Fromager');
INSERT INTO ProducteurAPourActivité VALUES (3, 'Agriculteur');
INSERT INTO ProducteurAPourActivité VALUES (4, 'Fromager');
INSERT INTO ProducteurAPourActivité VALUES (5, 'Agriculteur');

-- === 5. Produits ===
INSERT INTO Produit VALUES (1, 'Pommes du Valais', 'fruits secs', 'Pommes séchées bio du Valais', 100, 1);
INSERT INTO Produit VALUES (2, 'Raclette AOP', 'Fromage', 'Fromage à raclette AOP demi-meule', 50, 2);
INSERT INTO Produit VALUES (3, 'Huile de noix', 'huile', 'Huile pressée à froid, locale', 30, 3);
INSERT INTO Produit VALUES (4, 'Lentilles vertes', 'légumineuse', 'Lentilles vertes de la ferme Roux', 80, 1);
INSERT INTO Produit VALUES (5, 'Miel de montagne', 'boisson', 'Miel pur de montagne bio', 60, 4);
INSERT INTO Produit VALUES (6, 'Châtaignes', 'fruits secs', 'Châtaignes locales grillées', 40, 5);
INSERT INTO Produit VALUES (7, 'Fromage de chèvre', 'Fromage', 'Fromage de chèvre artisanal', 25, 4);

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
INSERT INTO LotProduit VALUES ('préconditionné', 0.5, TO_DATE('2025-10-12','YYYY-MM-DD'), 40, 'DLC', TO_DATE('2026-01-20','YYYY-MM-DD'), 25.00, 15.00, 2);
INSERT INTO LotProduit VALUES ('vrac', 1, TO_DATE('2025-09-15','YYYY-MM-DD'), 30, 'DLUO', TO_DATE('2026-09-15','YYYY-MM-DD'), 12.00, 7.00, 3);
INSERT INTO LotProduit VALUES ('vrac', 1, TO_DATE('2025-10-01','YYYY-MM-DD'), 80, 'DLUO', TO_DATE('2026-05-01','YYYY-MM-DD'), 4.50, 2.00, 4);
INSERT INTO LotProduit VALUES ('vrac', 1, TO_DATE('2025-10-18','YYYY-MM-DD'), 60, 'DLUO', TO_DATE('2026-02-01','YYYY-MM-DD'), 15.00, 8.00, 5);
INSERT INTO LotProduit VALUES ('préconditionné', 0.5, TO_DATE('2025-10-20','YYYY-MM-DD'), 40, 'DLC', TO_DATE('2026-01-15','YYYY-MM-DD'), 10.00, 5.00, 6);
INSERT INTO LotProduit VALUES ('vrac', 0.3, TO_DATE('2025-10-25','YYYY-MM-DD'), 25, 'DLUO', TO_DATE('2026-03-01','YYYY-MM-DD'), 12.00, 7.50, 7);

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
INSERT INTO CommandeaLivrer VALUES (1001, 'En préparation', 5.50, TO_DATE('2025-10-20','YYYY-MM-DD'), '12 Rue des Alpes, Sion');
INSERT INTO CommandeenBoutique VALUES (1002, 'Prête');
INSERT INTO CommandeaLivrer VALUES (1003, 'En préparation', 4.50, TO_DATE('2025-10-22','YYYY-MM-DD'), '8 Avenue du Rhône, Sierre');
INSERT INTO CommandeenBoutique VALUES (1004, 'Prête');

-- === 16. Lignes de commande produits ===
INSERT INTO LigneCommandeProduit VALUES (1, 1001, 1, 'vrac', 1, TO_DATE('2025-10-10','YYYY-MM-DD'), 2, 10.00, 20.00);
INSERT INTO LigneCommandeProduit VALUES (1, 1002, 2, 'préconditionné', 0.5, TO_DATE('2025-10-12','YYYY-MM-DD'), 1, 25.00, 25.00);
INSERT INTO LigneCommandeProduit VALUES (1, 1003, 5, 'vrac', 1, TO_DATE('2025-10-18','YYYY-MM-DD'), 3, 15.00, 45.00);
INSERT INTO LigneCommandeProduit VALUES (1, 1004, 6, 'préconditionné', 0.5, TO_DATE('2025-10-20','YYYY-MM-DD'), 5, 10.00, 50.00);


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