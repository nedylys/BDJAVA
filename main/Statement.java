import java.sql.*;

public class Statement{
    static final String PRE_STMT = "select * from Produit";
    static final String ALERTES_PRE = """
        SELECT p.IDPRODUIT,
               p.IDPRODUCTEUR,
               p.NOMPRODUIT,
               p.CATÉGORIEPRODUIT,
               p.STOCKPRODUIT,
               l.DATERÉCEPTIONP,
               l.DatePeremption,
               (l.DatePeremption - l.DATERÉCEPTIONP) AS timer
        FROM Produit p JOIN LotProduit l ON l.IdProduit = p.IdProduit
        WHERE (l.DatePeremption - l.DATERÉCEPTIONP) <= 15
        ORDER BY (l.DatePeremption - l.DATERÉCEPTIONP) ASC
    """;
    
    
}
