-- Fichier SQL regroupant toutes les requêtes identifiées dans les fichiers java

-- StatementCommande.java

-- Passage d'une Commande Produit :

-- Vérifie si l'idProduit existe dans la base.
select idProduit from Produit where idProduit = ?;

-- Vérifie si le produit est disponible en fonction de sa saison.
select DateDebut,DateFin from ProduitAPourSaison where idProduit = ?;

-- Affiche les différents modes de conditionnement d’un produit.
SELECT DISTINCT ModeConditionnement from LotProduit where idProduit = ?;

-- Si le produit est préconditionné, affiche les différents poids unitaires disponibles.
select DISTINCT PoidsUnitaire from LotProduit where idProduit = ? and ModeConditionnement = 'preconditionne';

-- Vérifie si le produit est sur commande ou en stock.
select idProduit from ProduitCommande where idProduit = ?;

-- Si le produit est en stock, récupère son délai de disponibilité.
select DelaiDisponibiliteHeure from ProduitCommande where idProduit = ?;

-- Vérifie que la quantité demandée est disponible.
select sum(PoidsUnitaire*QuantiteDisponibleP) from LotProduit 
where idProduit = ? and ModeConditionnement = ? and PoidsUnitaire = ? and DatePeremption >= TO_DATE(?, 'YYYY-MM-DD');

-- Calcule le prix du produit lorsqu’il est en stock.
SELECT PrixVentePTTC FROM LotProduit 
where idProduit = ? and ModeConditionnement = ? and PoidsUnitaire = ? 
and QuantiteDisponibleP != 0 and DatePeremption >= TO_DATE(?, 'YYYY-MM-DD')
ORDER BY DatePeremption ASC;

-- Si le produit est sur commande, la date de péremption n'est plus prise en compte (voir documentation).
SELECT PrixVentePTTC FROM LotProduit 
where idProduit = ? and ModeConditionnement = ? and PoidsUnitaire = ?;



-- Passage d'une Commande Contenant :

-- Vérifie si l'idContenant existe dans la base.
select idContenant from Contenant where idContenant = ?;

-- Vérifie que la quantité demandée pour le contenant est disponible.
select sum(QuantiteDisponibleC) from LotContenant where idContenant = ?;

-- Calcule le prix du contenant.
select PrixVenteCTTC from LotContenant where idContenant = ? and QuantiteDisponibleC != 0;



-- Finalisation de la commande :

-- Vérifie si le client existe déjà via son email.
select * from Client where emailClient = ?;

-- Récupère l'idClient d’un ancien client.
select idClient from Client where emailClient = ?;

-- Si le client est nouveau, on l’ajoute dans la base.
INSERT INTO Client VALUES(?,?,?,?,?);
INSERT INTO ClientAnonyme VALUES(?);

-- Commit de la création du nouveau client.
COMMIT;

-- Vérifie si l’adresse de livraison existe déjà.
select * from AdresseLivraison where AdresseLivraison = ?;

-- Si l'adresse est nouvelle, on l’insère et on l’associe au client.
INSERT INTO ClientAPourAdresseLivraison VALUES(?,?);
INSERT INTO AdresseLivraison VALUES(?);

-- Si le client possède déjà des adresses, on les affiche pour choix.
select AdresseLivraison from ClientAPourAdresseLivraison where emailClient = ?;

-- Insertion de la commande.
INSERT INTO Commande VALUES(?,TO_DATE(?, 'YYYY-MM-DD'),TO_DATE(?, 'HH24:MI:SS'),?,?,?);

-- Si la commande est en boutique.
INSERT INTO CommandeenBoutique VALUES(?,'En preparation');

-- Si elle est à livrer.
INSERT INTO CommandeaLivrer VALUES(?,'En preparation',?,TO_DATE(?, 'YYYY-MM-DD'),?);

-- Commit de l'ajout de la commande.
COMMIT;

-- Comptages pour déterminer idCommande, idClient, numLigneP, numLigneC.
SELECT COUNT(*) FROM ClientAnonyme;
SELECT COUNT(*) FROM Commande;
SELECT COUNT(*) FROM LigneCommandeProduit;
SELECT COUNT(*) FROM LigneCommandeContenant;

-- Verrouillage des produits en stock et des contenants.
SELECT * FROM LOTPRODUIT 
where idProduit = ? and ModeConditionnement = ? and PoidsUnitaire = ? 
FOR UPDATE WAIT 30;

SELECT * FROM LOTCONTENANT where idContenant = ? FOR UPDATE WAIT 30;

-- Récupère les informations nécessaires à l’insertion des lignes de commande (produit stock, produit sur commande, contenants).
SELECT PrixVentePTTC,DateReceptionP,QuantiteDisponibleP FROM LotProduit 
where idProduit = ? and ModeConditionnement = ? and PoidsUnitaire = ? 
and QuantiteDisponibleP != 0 and DatePeremption >= TO_DATE(?, 'YYYY-MM-DD') 
ORDER BY DatePeremption ASC;

SELECT PrixVentePTTC,DateReceptionP,QuantiteDisponibleP FROM LotProduit 
where idProduit = ? and ModeConditionnement = ? and PoidsUnitaire = ?;

SELECT DateReceptionC,QuantiteDisponibleC,PrixVenteCTTC 
FROM LotContenant where idContenant = ? and QuantiteDisponibleC != 0 ;

-- Insertion des lignes produit et contenant dans la commande.
INSERT INTO LigneCommandeProduit VALUES(?,?,?,?,?,TO_DATE(?, 'YYYY-MM-DD'),?,?,?);
INSERT INTO LigneCommandeContenant VALUES(?,?,?,TO_DATE(?, 'YYYY-MM-DD'),?,?,?);



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