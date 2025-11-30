package main;

public class Statement{
    static final String PRE_STMT = """
    SELECT
        p.IDPRODUIT,
        p.NOMPRODUIT,
        p.CATEGORIEPRODUIT,
        p.DESCRIPTIONPRODUIT,
        p.IDPRODUCTEUR,
        -- Concaténation des deux dates avec du texte au milieu
        TO_CHAR(ps.DateDebut, 'DD/MM/YYYY') || ' - ' || TO_CHAR(ps.DateFin, 'DD/MM/YYYY') AS Saison_Disponibilite
    FROM Produit p
    JOIN ProduitAPourSaison ps ON p.IdProduit = ps.IdProduit
    """;
    static final String ALERTES_PRE = """
    SELECT  
        p.IDPRODUIT,
        p.IDPRODUCTEUR,
        p.NOMPRODUIT,
        p.CATEGORIEPRODUIT,
        TO_CHAR(l.DatePeremption, 'YYYY-MM-DD') AS DatePeremption,
        CEIL(l.DatePeremption - SYSDATE) AS jours_restants,
        l.PRIXVENTEPTTC AS Prix_actuel_euros,
        ROUND(l.PRIXVENTEPTTC / 0.7, 2) AS Prix_Vente_TTC_initiale  -- AJOUT DE ROUND(..., 2)
    FROM Produit p
    JOIN LotProduit l ON l.IdProduit = p.IdProduit
    WHERE (l.DatePeremption - SYSDATE) <= 7
      AND CEIL(l.DatePeremption - SYSDATE) >= 0
    ORDER BY jours_restants ASC
    """;
    static final String Price_reduce = """
        UPDATE LotProduit
        SET 
            PRIXVENTEPTTC = PRIXVENTEPTTC * 0.7,
            REMISE_APPLIQUEE = 1
        WHERE (DatePeremption - SYSDATE) <= 7
        AND CEIL(DatePeremption - SYSDATE) >= 0
        AND (REMISE_APPLIQUEE = 0 OR REMISE_APPLIQUEE IS NULL)
    """;
        
    static final String PRE_STMT_COMMANDE = """
        SELECT 
            l.idproduit,
            p.IDPRODUCTEUR,
            l.modeconditionnement,
            p.nomproduit,
            p.categorieproduit,
            l.poidsUnitaire,
            SUM(l.quantitedisponiblep) AS QuantiteDisponible,
            TO_CHAR(ps.DateDebut, 'DD/MM/YYYY') || ' - ' || TO_CHAR(ps.DateFin, 'DD/MM/YYYY') AS Saison_Disponibilite
        FROM lotproduit l
        JOIN produit p 
            ON l.idproduit = p.idproduit
        JOIN ProduitAPourSaison ps 
            ON p.IdProduit = ps.IdProduit
        GROUP BY 
            l.idproduit,
            p.IDPRODUCTEUR,
            l.modeconditionnement,
            p.nomproduit,
            p.categorieproduit,
            l.poidsUnitaire,
            ps.DateDebut,
            ps.DateFin
        ORDER BY 
            l.idproduit ASC
    """;
    // Dans la classe Statement.java
    static final String AFFICHER_CONTENANTS = """
        SELECT *
        FROM Contenant 
        ORDER BY idContenant ASC
    """;
    //   l.DATERÉCEPTIONP,

    
}
