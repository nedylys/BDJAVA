SELECT * FROM AdressePComplete;
SELECT * FROM Producteur;
SELECT * FROM Produit;
SELECT * FROM Commande;

-- Produits Bio
SELECT p.nomProduit
FROM Produit p
JOIN Caractéristique c ON p.idProduit = c.idProduit
WHERE c.nomCaractéristique = 'bio' AND c.valeur = 'oui';

-- Commande par Client 
SELECT cl.nomClient, COUNT(*) AS nbCommandes
FROM Client cl
JOIN Commande c ON cl.idClient = c.idClient
GROUP BY cl.nomClient;

SELECT * FROM Produit;