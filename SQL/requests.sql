-- Fichier SQL regroupant toutes les requêtes identifiées dans les fichiers java

-- StatementCommande.java
select DelaiDisponibiliteHeure from ProduitCommande where idProduit = ?;
select idProduit from ProduitCommande where idProduit = ?;
select idProduit from ProduitCommande where idProduit = ?;
select DateDebut,DateFin from ProduitAPourSaison where idProduit = ?;
select idProduit from Produit where idProduit = ?;
select idContenant from Contenant where idContenant = ?;
select DISTINCT PoidsUnitaire from LotProduit where idProduit = ? and ModeConditionnement = 'preconditionne';
select sum(PoidsUnitaire*QuantiteDisponibleP) from LotProduit where idProduit = ? and ModeConditionnement = ? and PoidsUnitaire = ? and DatePeremption >= TO_DATE(?, 'YYYY-MM-DD');
select sum(QuantiteDisponibleC) from LotContenant where idContenant = ?;
select PrixVentePTTC from LotProduit where idProduit = ? and ModeConditionnement = ?;
select PrixVenteCTTC from LotContenant where idContenant = ?;
select idClient from Client where emailClient = ?;
INSERT INTO Client VALUES(?,?,?,?,?);
INSERT INTO ClientAnonyme VALUES(?);
INSERT INTO ClientAPourAdresseLivraison VALUES(?,?);
INSERT INTO AdresseLivraison VALUES(?);
select AdresseLivraison from ClientAPourAdresseLivraison where emailClient = ?;
select * from AdresseLivraison where AdresseLivraison = ?;
select * from Client where emailClient = ?;
select * from Commande where idCommande = ?;
select * from Client where idClient = ?;
SELECT COUNT(*) FROM ClientAnonyme;
SELECT COUNT(*) FROM Commande;
SELECT COUNT(*) FROM LigneCommandeProduit;
SELECT COUNT(*) FROM LigneCommandeContenant;
INSERT INTO Commande VALUES(?,TO_DATE(?, 'YYYY-MM-DD'),TO_DATE(?, 'HH24:MI:SS'),?,?,?,null);
INSERT INTO LigneCommandeProduit VALUES(?,?,?,?,?,TO_DATE(?, 'YYYY-MM-DD'),?,?,?);
INSERT INTO LigneCommandeContenant VALUES(?,?,?,TO_DATE(?, 'YYYY-MM-DD'),?,?,?);
SELECT PrixVentePTTC,DateReceptionP,QuantiteDisponibleP FROM LotProduit where idProduit = ? and ModeConditionnement = ? and PoidsUnitaire = ? and DatePeremption >= TO_DATE(?, 'YYYY-MM-DD') ORDER BY DatePeremption ASC;
SELECT PrixVentePTTC,DateReceptionP,QuantiteDisponibleP FROM LotProduit where idProduit = ? and ModeConditionnement = ? and PoidsUnitaire = ?;
SELECT DateReceptionC,QuantiteDisponibleC,PrixVenteCTTC FROM LotContenant where idContenant = ?;
SELECT DISTINCT ModeConditionnement from LotProduit where idProduit = ?;
INSERT INTO CommandeenBoutique VALUES(?,'En preparation');
INSERT INTO CommandeaLivrer VALUES(?,'En preparation',?,TO_DATE(?, 'YYYY-MM-DD'),?);
SELECT * FROM LOTPRODUIT where idProduit = ? and ModeConditionnement = ? and PoidsUnitaire = ? FOR UPDATE WAIT 30;
SELECT * FROM LOTCONTENANT where idContenant = ? FOR UPDATE WAIT 30;

-- Statement.java
SELECT p.IDPRODUIT, p.NOMPRODUIT, p.CATEGORIEPRODUIT, p.DESCRIPTIONPRODUIT, p.IDPRODUCTEUR, TO_CHAR(ps.DateDebut, 'DD/MM/YYYY') || ' - ' || TO_CHAR(ps.DateFin, 'DD/MM/YYYY') AS Saison_Disponibilite FROM Produit p JOIN ProduitAPourSaison ps ON p.IdProduit = ps.IdProduit;
SELECT p.IDPRODUIT, p.IDPRODUCTEUR, p.NOMPRODUIT, p.CATEGORIEPRODUIT, TO_CHAR(l.DatePeremption, 'YYYY-MM-DD') AS DatePeremption, CEIL(l.DatePeremption - SYSDATE) AS jours_restants, l.PRIXVENTEPTTC AS Prix_actuel_euros, ROUND(l.PRIXVENTEPTTC / 0.7, 2) AS Prix_Vente_TTC_initiale FROM Produit p JOIN LotProduit l ON l.IdProduit = p.IdProduit WHERE (l.DatePeremption - SYSDATE) <= 7 AND CEIL(l.DatePeremption - SYSDATE) >= 0 ORDER BY jours_restants ASC;
UPDATE LotProduit SET PRIXVENTEPTTC = PRIXVENTEPTTC * 0.7, REMISE_APPLIQUEE = 1 WHERE (DatePeremption - SYSDATE) <= 7 AND CEIL(DatePeremption - SYSDATE) >= 0 AND (REMISE_APPLIQUEE = 0 OR REMISE_APPLIQUEE IS NULL);
SELECT l.idproduit, p.IDPRODUCTEUR, l.modeconditionnement, p.nomproduit, p.categorieproduit, l.poidsUnitaire, SUM(l.quantitedisponiblep) AS QuantiteDisponible, TO_CHAR(ps.DateDebut, 'DD/MM/YYYY') || ' - ' || TO_CHAR(ps.DateFin, 'DD/MM/YYYY') AS Saison_Disponibilite FROM lotproduit l JOIN produit p ON l.idproduit = p.idproduit JOIN ProduitAPourSaison ps ON p.IdProduit = ps.IdProduit GROUP BY l.idproduit, p.IDPRODUCTEUR, l.modeconditionnement, p.nomproduit, p.categorieproduit, l.poidsUnitaire, ps.DateDebut, ps.DateFin ORDER BY l.idproduit ASC;
SELECT * FROM Contenant ORDER BY idContenant ASC;

-- ClotureCommande.java
SELECT DISTINCT c.idCommande, c.idClient, c.ModeRecuperation FROM commande c, commandeenboutique, commandealivrer WHERE (c.idCommande = commandeenboutique.idCommande OR c.idCommande = commandealivrer.idCommande) AND (commandeenboutique.statutcommandeb != 'Recuperee' AND commandeenboutique.statutcommandeb != 'Annulee' AND commandealivrer.statutcommandel != 'Livree' AND commandealivrer.statutcommandel != 'Annulee');
Update Commandeenboutique set StatutCommandeB = 'Annulee' where idCommande = ?;
Select StatutCommandeB from Commandeenboutique where idCommande = ?;
Update Commandeenboutique set StatutCommandeB = ? where idCommande = ?;
UPDATE LotProduit SET QuantiteDisponibleP = QuantiteDisponibleP - (SELECT QuantiteCommandeeP FROM LigneCommandeProduit WHERE idCommande = ? AND LigneCommandeProduit.idProduit = LotProduit.idProduit AND LigneCommandeProduit.DateReceptionP = LotProduit.DateReceptionP AND LigneCommandeProduit.ModeConditionnement = LotProduit.ModeConditionnement AND LigneCommandeProduit.PoidsUnitaire = LotProduit.PoidsUnitaire) WHERE EXISTS (SELECT 1 FROM LigneCommandeProduit WHERE idCommande = ? AND LigneCommandeProduit.idProduit = LotProduit.idProduit AND LigneCommandeProduit.DateReceptionP = LotProduit.DateReceptionP AND LigneCommandeProduit.ModeConditionnement = LotProduit.ModeConditionnement AND LigneCommandeProduit.PoidsUnitaire = LotProduit.PoidsUnitaire);
UPDATE LotContenant SET QuantiteDisponibleC = QuantiteDisponibleC - (SELECT QuantiteCommandeeC FROM LigneCommandeContenant WHERE idCommande = ? AND LigneCommandeContenant.idContenant = LotContenant.idContenant AND LigneCommandeContenant.DateReceptionC = LotContenant.DateReceptionC) WHERE EXISTS (SELECT 1 FROM LigneCommandeContenant WHERE idCommande = ? AND LigneCommandeContenant.idContenant = LotContenant.idContenant AND LigneCommandeContenant.DateReceptionC = LotContenant.DateReceptionC);
Update CommandeaLivrer set StatutCommandeL = 'Annulee' where idCommande = ?;
Select StatutCommandeL from CommandeaLivrer where idCommande = ?;
UPDATE CommandeaLivrer SET StatutCommandeL = ? WHERE idCommande = ?;
UPDATE Commandeenboutique SET StatutCommandeB = ? WHERE idCommande = ?;
UPDATE CommandeaLivrer SET StatutCommandeL = ? WHERE idCommande = ?;
UPDATE LotProduit SET QuantiteDisponibleP = QuantiteDisponibleP + (SELECT QuantiteCommandeeP FROM LigneCommandeProduit WHERE idCommande = ? AND LigneCommandeProduit.idProduit = LotProduit.idProduit AND LigneCommandeProduit.DateReceptionP = LotProduit.DateReceptionP AND LigneCommandeProduit.ModeConditionnement = LotProduit.ModeConditionnement AND LigneCommandeProduit.PoidsUnitaire = LotProduit.PoidsUnitaire) WHERE EXISTS (SELECT 1 FROM LigneCommandeProduit WHERE idCommande = ? AND LigneCommandeProduit.idProduit = LotProduit.idProduit AND LigneCommandeProduit.DateReceptionP = LotProduit.DateReceptionP AND LigneCommandeProduit.ModeConditionnement = LotProduit.ModeConditionnement AND LigneCommandeProduit.PoidsUnitaire = LotProduit.PoidsUnitaire);
UPDATE LotContenant SET QuantiteDisponibleC = QuantiteDisponibleC + (SELECT QuantiteCommandeeC FROM LigneCommandeContenant WHERE idCommande = ? AND LigneCommandeContenant.idContenant = LotContenant.idContenant AND LigneCommandeContenant.DateReceptionC = LotContenant.DateReceptionC) WHERE EXISTS (SELECT 1 FROM LigneCommandeContenant WHERE idCommande = ? AND LigneCommandeContenant.idContenant = LotContenant.idContenant AND LigneCommandeContenant.DateReceptionC = LotContenant.DateReceptionC);


-- SuiviCommande.java
SELECT * from CommandeaLivrer WHERE idCommande = ?;