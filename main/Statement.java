package main;

public class Statement{
    static final String PRE_STMT = """
                SELECT l.idproduit, l.modeconditionnement, p.nomproduit, p.descriptionproduit, p.categorieproduit, l.poidsUnitaire, sum(l.quantitedisponiblep) as QantititéDisponible
                FROM lotproduit l, produit p
                where l.idproduit = p.idproduit
                group by (l.idproduit, p.nomproduit, l.modeconditionnement, p.descriptionproduit, p.categorieproduit, poidsUnitaire)
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
            (l.PRIXVENTEPTTC / (0.7)) AS Prix_Vente_TTC_initiale,
            l.REMISE_APPLIQUEE

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
    

    //   l.DATERÉCEPTIONP,

    
}
