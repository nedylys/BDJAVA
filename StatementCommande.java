import java.sql.*;

public class StatementCommande{
    static final String STDelai = "select DelaiDisponibilitéHeure from ProduitCommande where idProduit = ? ";
    
    static final String STCommande = "select idProduit from ProduitCommande where idProduit = ? ";
    
    static final String STDispo = "select idProduit from ProduitCommande where idProduit = ? ";
    
    static final String STSaison = "select DateDebut,DateFin from ProduitAPourSaison where idProduit = ?";
    
    static final String STVerifieID = "select idProduit from Produit where idProduit = ?"; 

    static final String STQteStock = "select sum(PoidsUnitaire*QuantitéDisponibleP) from LotProduit where idProduit = ?";
    
    static final String STPrix = "select PrixVentePTTC from LotProduit where idProduit = ?";
    
    private Connection conn;
    
    public StatementCommande(Connection conn){
        this.conn = conn;
    }
   
    public double calculeprix(int idProduit, double quantite){
        // Retourne le prix d'une commande d'un produit
        try{
        PreparedStatement stmt = conn.prepareStatement(STPrix);
        stmt.setInt(1, idProduit);
        ResultSet rset = stmt.executeQuery();
        rset.next();
        double prix = rset.getDouble(1);
        rset.close();
        stmt.close();
        return prix*quantite;
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
            return 0;
      }
    }
    
    public boolean getDispo(int idProduit,double quantite){
    // Verfie si le produit en stock peut être obtenu
    try{
        PreparedStatement stmt1 = conn.prepareStatement(STSaison);
        stmt1.setInt(1, idProduit);
        ResultSet rset1 = stmt1.executeQuery();
        rset1.next();
        java.util.Date debut =  rset1.getDate(1);
        java.util.Date fin =  rset1.getDate(2);
        java.util.Date now = new java.util.Date();
        rset1.close();
        stmt1.close();
        if(!(now.after(debut)) || !(now.before(fin))){
            System.out.println("Le produit n'est pas disponible");
            return false;
        }
        PreparedStatement stmt2 = conn.prepareStatement(STQteStock);
        stmt2.setInt(1, idProduit);
        ResultSet rset2 = stmt2.executeQuery();
        rset2.next();
        double qtedispo = rset2.getDouble(1);
        rset2.close();
        stmt2.close();
        if (qtedispo < quantite){
            System.out.println("Quantité insuffisante");
            return false;
        }else{
            return true;
        }
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
            return false;
      }
    }
    
    public double getDelaiDispo(int idProduit){
      try{
        PreparedStatement stmt = conn.prepareStatement(STDelai);
        stmt.setInt(1, idProduit);
        ResultSet rset = stmt.executeQuery();
        rset.next();
        double disponibilite =  rset.getDouble(1);
        return disponibilite;
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
            return 0;
      }
    }

    public boolean inCommande(int idProduit){
        try{
            PreparedStatement stmt = conn.prepareStatement(STCommande);
            stmt.setInt(1, idProduit);
            ResultSet rset = stmt.executeQuery();
            if (rset.next()){
                System.out.println("Produit sur Commande ");
                rset.close();
                stmt.close();
                return true;
            }else{
                System.out.println("Produit en Stock ");
                rset.close();
                stmt.close();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
            return false;
        }
    }
    public boolean verifieIdProduit(int idProduit){
        try{
            PreparedStatement stmt = conn.prepareStatement(STVerifieID);
            stmt.setInt(1, idProduit);
            ResultSet rset = stmt.executeQuery();
            if (rset.next()){
                rset.close();
                stmt.close();
                return true;
            }else{
                System.out.println("L'IdProduit est faux");
                rset.close();
                stmt.close();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
            return false;
        }
    }
}
