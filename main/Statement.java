package main;

public class Statement{
    static final String PRE_STMT = "select * from Produit";
    static final String ALERTES_PRE = """
        SELECT p.IDPRODUIT,
               p.IDPRODUCTEUR,
               p.NOMPRODUIT,
               p.CATÉGORIEPRODUIT,
               p.STOCKPRODUIT,
               l.DatePeremption,
               CEIL((l.DatePeremption - SYSDATE)) AS jours_restants
        FROM Produit p JOIN LotProduit l ON l.IdProduit = p.IdProduit
        WHERE (l.DatePeremption - CURRENT_DATE) <= 20
        ORDER BY jours_restants ASC
    """;
    //   l.DATERÉCEPTIONP,

    
}
