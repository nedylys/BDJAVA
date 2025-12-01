package main;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class StatementCommande{
    static final String STDelai = "select DelaiDisponibiliteHeure from ProduitCommande where idProduit = ? ";
    
    static final String STCommande = "select idProduit from ProduitCommande where idProduit = ? ";
    
    static final String STDispo = "select idProduit from ProduitCommande where idProduit = ? ";
    
    static final String STSaison = "select DateDebut,DateFin from ProduitAPourSaison where idProduit = ?";
    
    static final String STVerifieIDProduit = "select idProduit from Produit where idProduit = ?"; 

    static final String STVerifieIDContenant = "select idContenant from Contenant where idContenant = ?"; 

    static final String STVERIFPOIDS = "select DISTINCT PoidsUnitaire from LotProduit where idProduit = ? and ModeConditionnement = 'preconditionne'"; 

    static final String STQteStock = "select sum(PoidsUnitaire*QuantiteDisponibleP) from LotProduit where idProduit = ? and ModeConditionnement = ? and PoidsUnitaire = ? and DatePeremption >= TO_DATE(?, 'YYYY-MM-DD')";
    
    static final String STQteContenant = "select sum(QuantiteDisponibleC) from LotContenant where idContenant = ?";
    
    static final String STPrixProduit = "select PrixVentePTTC from LotProduit where idProduit = ? and ModeConditionnement = ?";

    static final String STPrixContenant = "select PrixVenteCTTC from LotContenant where idContenant = ?";

    static final String STIDCLIENT = "select idClient from Client where emailClient = ?";
    
    static final String STNvClient = " INSERT INTO Client VALUES(?,?,?,?,?) ";

    static final String STNVCLIENTANONYME = " INSERT INTO ClientAnonyme VALUES(?) ";

    static final String STNVADRESSECLIENT = " INSERT INTO ClientAPourAdresseLivraison VALUES(?,?) ";
    
    static final String STNVADRESSE = " INSERT INTO AdresseLivraison VALUES(?) ";

    static final String STGETADRESS = "select AdresseLivraison from ClientAPourAdresseLivraison where emailClient = ?";

    static final String STVERIFIEADRESSEXISTE = "select * from AdresseLivraison where AdresseLivraison = ?";

    static final String STVERIFIEEMAILEXIST = "select * from Client where emailClient = ?";

    static final String STVERIFIEIDCOMMANDE = "select * from Commande where idCommande = ?";

    static final String STVERIFIEIDCLIENT = "select * from Client where idClient = ?";

    static final String STNBIDCLIENT = " SELECT COUNT(*) FROM ClientAnonyme";

    static final String STNBIDCOMMANDE = "SELECT COUNT(*) FROM Commande";

    static final String STNBLIGNEP = "SELECT COUNT(*) FROM LigneCommandeProduit";

    static final String STNBLIGNEC = "SELECT COUNT(*) FROM LigneCommandeContenant";

    static final String STNVCOMMANDE = " INSERT INTO Commande VALUES(?,TO_DATE(?, 'YYYY-MM-DD'),TO_DATE(?, 'HH24:MI:SS'),?,?,?)";

    static final String STNVLIGNEP = " INSERT INTO LigneCommandeProduit VALUES(?,?,?,?,?,TO_DATE(?, 'YYYY-MM-DD'),?,?,?) ";

    static final String STNVLIGNEC = " INSERT INTO LigneCommandeContenant VALUES(?,?,?,TO_DATE(?, 'YYYY-MM-DD'),?,?,?) ";

    static final String STCARACTP = "SELECT PrixVentePTTC,DateReceptionP,QuantiteDisponibleP FROM LotProduit where idProduit = ? and ModeConditionnement = ? and PoidsUnitaire = ? and DatePeremption >= TO_DATE(?, 'YYYY-MM-DD') ORDER BY DatePeremption ASC";

    static final String STCARACTPCOMMANDE = "SELECT PrixVentePTTC,DateReceptionP,QuantiteDisponibleP FROM LotProduit where idProduit = ? and ModeConditionnement = ? and PoidsUnitaire = ? ";

    static final String STCARACTC = "SELECT DateReceptionC,QuantiteDisponibleC,PrixVenteCTTC FROM LotContenant where idContenant = ?";
    
    static final String STMODECONDITIONNEMENT = "SELECT DISTINCT ModeConditionnement from LotProduit where idProduit = ?";
    
    static final String STCOMMBOUTIQUE = "INSERT INTO CommandeenBoutique VALUES(?,'En preparation')";
    
    static final String STCOMMLIVRER = "INSERT INTO CommandeaLivrer VALUES(?,'En preparation',?,TO_DATE(?, 'YYYY-MM-DD'),?)";
    
    static final String STLOCKP = "SELECT * FROM LOTPRODUIT where idProduit = ? and ModeConditionnement = ? and PoidsUnitaire = ? FOR UPDATE WAIT 30";

    static final String STLOCKC = "SELECT * FROM LOTCONTENANT where idContenant = ? FOR UPDATE WAIT 30";
    
    
    private Connection conn;

    private Scanner scan;
    
    public StatementCommande(Connection conn,Scanner scan){
        this.conn = conn;
        this.scan = scan;
    }
   
    public double calculePrixProduit(int idProduit, double quantite,String ModeConditionnement){
        // Retourne le prix d'une commande d'un produit
        try{
        PreparedStatement stmt = conn.prepareStatement(STPrixProduit);
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
    public double calculePrixContenant(int idContenant, int quantite){
        // Retourne le prix d'une commande d'un contenant
        try{
            PreparedStatement stmt = conn.prepareStatement(STPrixContenant);
            stmt.setInt(1, idContenant);
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
    public void getModeConditionnement(int idProduit){
        try{
            PreparedStatement stmt = conn.prepareStatement(STMODECONDITIONNEMENT);
            stmt.setInt(1, idProduit);
            ResultSet rset = stmt.executeQuery();
            while (rset.next()){
                String ModeConditonnement = rset.getString(1);
                System.out.println(ModeConditonnement);
            }
            rset.close();
            stmt.close();
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
      }
    }
    public boolean verifieDispoSaison(int idProduit){
        try{
            PreparedStatement stmt1 = conn.prepareStatement(STSaison);
            stmt1.setInt(1, idProduit);
            ResultSet rset1 = stmt1.executeQuery();
            while (rset1.next()){
                java.util.Date debut =  rset1.getDate(1);
                java.util.Date fin =  rset1.getDate(2);
                java.util.Date now = new java.util.Date();
                if((now.after(debut)) && now.before(fin)){
                    rset1.close();
                    stmt1.close();
                    return true;
                } 
            }
            System.out.println("Le produit n'est pas disponible . Pas la bonne saison");
            rset1.close();
            stmt1.close();
            return false;
        
        }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
            return false;
      }
    }
    public boolean getDispo(int idProduit,double quantite,String ModeConditionnement,double poidsUnitaire){
    // Verfie si le produit en stock peut être obtenu
    try{
        java.util.Date now = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateparam = sdf.format(now);
        PreparedStatement stmt2 = conn.prepareStatement(STQteStock);
        stmt2.setInt(1, idProduit);
        stmt2.setString(2,ModeConditionnement);
        stmt2.setDouble(3,poidsUnitaire);
        stmt2.setString(4,dateparam);
        ResultSet rset2 = stmt2.executeQuery();
        rset2.next();
        double qtedispo = rset2.getDouble(1);
        rset2.close();
        stmt2.close();
        if (qtedispo < quantite){
            System.out.println("Quantité insuffisante");
            System.out.println("La quantite disponible est " + qtedispo);
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
    public int getDelaiDispo(int idProduit){
      try{
        PreparedStatement stmt = conn.prepareStatement(STDelai);
        stmt.setInt(1, idProduit);
        ResultSet rset = stmt.executeQuery();
        rset.next();
        int disponibilite =  rset.getInt(1);
        return disponibilite;
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
            return 0;
      }
    }
    public void choisirPoidsUnitaire(int idProduit){
        try{
            PreparedStatement stmt = conn.prepareStatement(STVERIFPOIDS);
            stmt.setInt(1, idProduit);
            ResultSet rset = stmt.executeQuery();
            ArrayList<Double> listePoids = new ArrayList<>();
            while (rset.next()){
                double poids = rset.getDouble(1);
                System.out.println(poids);
            }
            rset.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
        }
    }
    public boolean inCommande(int idProduit){
        // Fonction qui retourne true si le produit est sur commande
        try{
            PreparedStatement stmt = conn.prepareStatement(STCommande);
            stmt.setInt(1, idProduit);
            ResultSet rset = stmt.executeQuery();
            if (rset.next()){
                rset.close();
                stmt.close();
                return true;
            }else{
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
    public boolean verifieIdProduit(int id){
    // Verifie que l'idProduit ou idContenant existe dans la base de données.
        try{
            PreparedStatement stmt = conn.prepareStatement(STVerifieIDProduit);
            stmt.setInt(1, id);
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
    public boolean verifieIdCommande(int idCommande){
    // Verifie que l'idCommande existe dans la base
        try{
            PreparedStatement stmt = conn.prepareStatement(STVERIFIEIDCOMMANDE);
            stmt.setInt(1, idCommande);
            ResultSet rset = stmt.executeQuery();
            if (rset.next()){
                rset.close();
                stmt.close();
                return true;
            }else{
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
    public boolean verfieIdContenant(int idContenant){
    // Verifie que l'idContenant existe dans la base de données.
        try{
            PreparedStatement stmt = conn.prepareStatement(STVerifieIDContenant);
            stmt.setInt(1, idContenant);
            ResultSet rset = stmt.executeQuery();
            if (rset.next()){
                rset.close();
                stmt.close();
                return true;
            }else{
                System.out.println("L'IdContenant est faux");
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
    public boolean verfieIdClient(int idClient){
    // Verifie que l'idContenant existe dans la base de données.
        try{
            PreparedStatement stmt = conn.prepareStatement(STVERIFIEIDCLIENT);
            stmt.setInt(1, idClient);
            ResultSet rset = stmt.executeQuery();
            if (rset.next()){
                rset.close();
                stmt.close();
                return true;
            }else{
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
    public boolean getDispoContenant(int idContenant,int quantite){
    // Verfie si le contenant peut être obtenu
    try{
        PreparedStatement stmt = conn.prepareStatement(STQteContenant);
        stmt.setInt(1, idContenant);
        ResultSet rset = stmt.executeQuery();
        rset.next();
        int qtedispo = rset.getInt(1);
        rset.close();
        stmt.close();
        if (qtedispo < quantite){
            System.out.println("Quantité insuffisante");
            System.out.println("La quatité disponible est " + qtedispo);
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
    public int nbClient(){
    // Retourn le nb d'ID client dans la base de données
        try{
        PreparedStatement stmt = conn.prepareStatement(STNBIDCLIENT);
        ResultSet rset = stmt.executeQuery();
        rset.next();
        int nbIdclient = rset.getInt(1);
        while (this.verfieIdClient(nbIdclient)){
            nbIdclient += 1; // Afin d'éviter les problèmes
        }
        rset.close();
        stmt.close();
        return nbIdclient;
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
            return 0;
      }
    }
    public int nbIdCommande(){
    // Retourn le nb d'ID commande dans la base de données
        try{
        PreparedStatement stmt = conn.prepareStatement(STNBIDCOMMANDE);
        ResultSet rset = stmt.executeQuery();
        rset.next();
        int nbCommande = rset.getInt(1);
        while (this.verifieIdCommande(nbCommande)){
            nbCommande +=1;
        }
        rset.close();
        stmt.close();
        return nbCommande;
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
            return 0;
      }
    }
    public int nbLigneP(){
    // Retourn le nb de lignes de commandeP dans la base de données
        try{
        PreparedStatement stmt = conn.prepareStatement(STNBLIGNEP);
        ResultSet rset = stmt.executeQuery();
        rset.next();
        int nbLigneP = rset.getInt(1);
        rset.close();
        stmt.close();
        return nbLigneP;
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
            return 0;
      }
    }
    public int nbLigneContenant(){
    // Retourn le nb de lignes de commandeContenant dans la base de données
        try{
        PreparedStatement stmt = conn.prepareStatement(STNBLIGNEC);
        ResultSet rset = stmt.executeQuery();
        rset.next();
        int nbLigneC = rset.getInt(1);
        rset.close();
        stmt.close();
        return nbLigneC;
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
            return 0;
      }
    }
    public int getIdClient(String emailClient){
        // Retourne l'Id client à partir de l'email
    try{
        PreparedStatement stmt = conn.prepareStatement(STIDCLIENT);
        stmt.setString(1,emailClient);
        ResultSet rst =  stmt.executeQuery();
        int idClient = 0;
        if (rst.next()){
            idClient = rst.getInt(1);
        }else{
            System.out.println("Ce client n'existe pas");
        }
        rst.close();
        stmt.close();
        return idClient;
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
            return - 400;
      }
    } 
    public void ajouteNovClientAnonyme(int idClient){
    try{
        PreparedStatement stmt = conn.prepareStatement(STNVCLIENTANONYME);
        stmt.setInt(1,idClient);
        int nbAjout = stmt.executeUpdate();
        if (nbAjout == 1){
            System.out.println("Le clientAnonyme a bien été ajouté dans la base de données");
        } else{
            System.out.println("Echec de l'opération le clientAnonyme n'a pas été ajouté ");
        }
        stmt.close();
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
      }
    }
    public void ajouteNovClient(String[] argsClient,int idClient){
    try{
        PreparedStatement stmt = conn.prepareStatement(STNvClient);
        for (int i = 0; i<4;i++){
            stmt.setString(i+1,argsClient[i]);
        }
        stmt.setInt(5,idClient);
        this.ajouteNovClientAnonyme(idClient);
        int nbAjout = stmt.executeUpdate();
        if (nbAjout == 1){
            System.out.println("Le client a bien été ajouté dans la base de données");
        } else{
            System.out.println("Echec de l'opération le client n'a pas été ajouté ");
        }
        stmt.close();
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
      }
    }
    public boolean verifieAdresseExist(String adresse){
    try{
        PreparedStatement stmt = conn.prepareStatement(STVERIFIEADRESSEXISTE);
        stmt.setString(1, adresse);
        ResultSet rst = stmt.executeQuery();
        if (rst.next()){
            rst.close();
            stmt.close();
            return true;
        } else {
            rst.close();
            stmt.close();
            return false;
        }
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
            return false;
      }
    }
    public boolean verifieEmailExist(String emailClient){
    try{
        PreparedStatement stmt = conn.prepareStatement(STVERIFIEEMAILEXIST);
        stmt.setString(1, emailClient);
        ResultSet rst = stmt.executeQuery();
        if (rst.next()){
            rst.close();
            stmt.close();
            return true;
        } else {
            rst.close();
            stmt.close();
            return false;
        }
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
            return false;
      }
    }
    public void ajouteNovAdresseClient(String adresseClient,String emailClient){
    try{
        PreparedStatement stmt = conn.prepareStatement(STNVADRESSE);
        PreparedStatement stmt2 = conn.prepareStatement(STNVADRESSECLIENT);
        int nbAjout = 0;
        if (!verifieAdresseExist(adresseClient)){
            stmt.setString(1,adresseClient);
            nbAjout = stmt.executeUpdate();
            System.out.println("Nouvelle adresse dans la base !");
        } else{
            System.out.println("Cette adresse existe déja dans la base !");
        }
        stmt2.setString(1,emailClient);
        stmt2.setString(2, adresseClient);
        int nbAjout2 = stmt2.executeUpdate();
        if (nbAjout2 + nbAjout == 2){
            System.out.println("L'adresse a bien été ajoutée ");
        }else{
            //System.out.println("Echec de l'ajout");
        }
        stmt.close();
        stmt2.close();
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
      }
    }
    public ArrayList<String> getAdresseClient(String emailClient){
    // Retourne les adressesLivraison d'un client à partir de son idClient
    try{
        PreparedStatement stmt = conn.prepareStatement(STGETADRESS);
        stmt.setString(1,emailClient);
        ResultSet rst = stmt.executeQuery();
        int iAdresse = 0;
        ArrayList<String> adresseArray = new ArrayList<>(); 
        while (rst.next()){
            iAdresse++;
            String adresse = rst.getString(1);
            adresseArray.add(adresse);
            System.out.println(iAdresse +". " + adresse);
        }
        rst.close();
        stmt.close();
        return adresseArray;
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
            return null;
      }
    }
    public void creeCommande(int idCommande,int idClient,String[] argsCommande){
    try{
        PreparedStatement stmt = conn.prepareStatement(STNVCOMMANDE);
        stmt.setInt(1,idCommande);
        java.util.Date now = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        String date = sdf.format(now);
        String time = sdf2.format(now);
        stmt.setString(2,date);
        stmt.setString(3,time);
        stmt.setString(4,argsCommande[0]);
        stmt.setString(5, argsCommande[1]);
        stmt.setInt(6, idClient);
        int nbAjout = stmt.executeUpdate();
        stmt.close();
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
      }
    }
    public void commandeBoutique(int idCommande){
    try{
        PreparedStatement stmt = conn.prepareStatement(STCOMMBOUTIQUE);
        stmt.setInt(1,idCommande);
        int nbAjout = stmt.executeUpdate();
        stmt.close();
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
      }
    }
    public void commandeLivrer(int idCommande,double fraisLivraison,String[] argsLivraison){
    try{
        PreparedStatement stmt = conn.prepareStatement(STCOMMLIVRER);
        stmt.setInt(1,idCommande);
        stmt.setDouble(2, fraisLivraison);
        stmt.setString(3, argsLivraison[0]);
        stmt.setString(4, argsLivraison[1]);
        int nbAjout = stmt.executeUpdate();
        if (nbAjout > 0){
            System.out.println("La création de la CommandeLivrer a bien été réussie");
        }else{
            System.out.println("Echec de la création de la CommandeLivrer");
        }
        stmt.close();
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
      }
    }
    public void ajouteCommandeP(int[] argsCommandeP,String ModeConditionnement,double[] argsDouble,String date,double PoidsUnitaire){
    try{
        PreparedStatement stmt = conn.prepareStatement(STNVLIGNEP);
        for (int i = 0; i<3;i++){
            stmt.setInt(i+1,argsCommandeP[i]);
        }
        stmt.setString(4,ModeConditionnement);
        stmt.setDouble(5,PoidsUnitaire);
        stmt.setString(6,date);
        stmt.setDouble(7, argsDouble[0]);
        stmt.setDouble(8, argsDouble[1]);
        stmt.setDouble(9, argsDouble[2]);
        int nbAjout = stmt.executeUpdate();
        stmt.close();
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
      }
    }
    public void ajouteCommandeC(int[] argsCommandeC,double[] argsDouble,String date,int quantiteC){
    try{
        PreparedStatement stmt = conn.prepareStatement(STNVLIGNEC);
        for (int i = 0; i<3;i++){
            stmt.setInt(i+1,argsCommandeC[i]);
        }
        stmt.setString(4,date);
        stmt.setInt(5,quantiteC);
        stmt.setDouble(6, argsDouble[0]);
        stmt.setDouble(7, argsDouble[1]);
        int nbAjout = stmt.executeUpdate();
        stmt.close();
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
      }
    }
    public void ajouteCommandeGlobalP(int[] argsCommandeP,String ModeConditionnement,double quantiteP,double PoidsUnitaire){
        try{
        PreparedStatement stmt = conn.prepareStatement(STCARACTP);
        stmt.setInt(1,argsCommandeP[2]);
        stmt.setString(2, ModeConditionnement);
        stmt.setDouble(3,PoidsUnitaire);
        java.util.Date now = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateparam = sdf.format(now);
        stmt.setString(4, dateparam);
        ResultSet rset = stmt.executeQuery();
        double prixTotal = 0;
        while(quantiteP > 0 && rset.next()){
            argsCommandeP[0]++;
            double qtedispo = rset.getDouble(3);
            if (qtedispo == 0){
                continue;
            }
            double prix = rset.getDouble(1);
            java.util.Date date = rset.getDate(2);
            String actualDate = sdf.format(date);
            double quantite = Math.min(quantiteP,qtedispo);
            double sousTotal = quantite*prix;
            double[] argsDouble = {quantite,prix,sousTotal};
            ajouteCommandeP(argsCommandeP, ModeConditionnement, argsDouble, actualDate,PoidsUnitaire);
            prixTotal += sousTotal;
            quantiteP -= quantite;
        }
        rset.close();
        stmt.close();
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
      }
    }
    public void ajouteCommandeGlobalCommandeP(int[] argsCommandeP,String ModeConditionnement,double quantiteP,double PoidsUnitaire){
        try{
        PreparedStatement stmt = conn.prepareStatement(STCARACTPCOMMANDE);
        stmt.setInt(1,argsCommandeP[2]);
        stmt.setString(2, ModeConditionnement);
        stmt.setDouble(3,PoidsUnitaire);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ResultSet rset = stmt.executeQuery();
        rset.next();
        argsCommandeP[0]++;
        double prix = rset.getDouble(1);
        java.util.Date date = rset.getDate(2);
        String actualDate = sdf.format(date);
        double prixTotal = quantiteP*prix;
        double[] argsDouble = {quantiteP,prix,prixTotal};
        ajouteCommandeP(argsCommandeP, ModeConditionnement, argsDouble, actualDate,PoidsUnitaire);
        rset.close();
        stmt.close();
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
      }
    }
    public double retourneprixGlobalCommandeP(int[] argsCommandeP,String ModeConditionnement,double quantiteP,double PoidsUnitaire){
        try{
        PreparedStatement stmt = conn.prepareStatement(STCARACTPCOMMANDE);
        stmt.setInt(1,argsCommandeP[2]);
        stmt.setString(2, ModeConditionnement);
        stmt.setDouble(3,PoidsUnitaire);
        ResultSet rset = stmt.executeQuery();
        rset.next();
        double prix = rset.getDouble(1);
        double prixTotal = quantiteP*prix;
        rset.close();
        stmt.close();
        return prixTotal;
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
            return -400.0;
      }
    }
    public double retournePrixCommandeP(int[] argsCommandeP,String ModeConditionnement,double quantiteP,double PoidsUnitaire){
        try{
        PreparedStatement stmt = conn.prepareStatement(STCARACTP);
        stmt.setInt(1,argsCommandeP[2]);
        stmt.setString(2, ModeConditionnement);
        stmt.setDouble(3,PoidsUnitaire);
        java.util.Date now = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateparam = sdf.format(now);
        stmt.setString(4, dateparam);
        ResultSet rset = stmt.executeQuery();
        double prixTotal = 0;
        while(quantiteP > 0 && rset.next() ){
            double qtedispo = rset.getDouble(3);
            double prix = rset.getDouble(1);
            java.util.Date date = rset.getDate(2);
            String actual_date = sdf.format(date);
            double quantite = Math.min(quantiteP,qtedispo);
            double sousTotal = quantite*prix;
            double[] argsDouble = {quantite,prix,sousTotal};
            prixTotal += sousTotal;
            quantiteP -= quantite;
        }
        rset.close();
        stmt.close();
        return prixTotal;
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
            return -400;
      }
    }
    public double retournePrixCommandeC(int[] argsCommandeC,int quantiteC){
        try{
        PreparedStatement stmt = conn.prepareStatement(STCARACTC);
        stmt.setInt(1,argsCommandeC[2]);
        ResultSet rset = stmt.executeQuery();
        double prixTotal = 0;
        while(quantiteC > 0 && rset.next()){
            int qtedispo = rset.getInt(2);
            double prix = rset.getDouble(3);
            String date = rset.getString(1);
            double sousTotal = quantiteC*prix;
            int quantite = Math.min(quantiteC,qtedispo);
            double[] argsDouble = {prix,sousTotal};
            prixTotal += sousTotal;
            quantiteC -= quantite;
        }
        rset.close();
        stmt.close();
        return prixTotal;
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
            return 0;
      }
    }       
    public void ajouteCommandeGlobalC(int[] argsCommandeC,int quantiteC){
        try{
        PreparedStatement stmt = conn.prepareStatement(STCARACTC);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        stmt.setInt(1,argsCommandeC[2]);
        ResultSet rset = stmt.executeQuery();
        double prixTotal = 0;
        while(quantiteC > 0 && rset.next()){
            argsCommandeC[0]++;
            int qtedispo = rset.getInt(2);
            if (qtedispo == 0){
                continue;
            }
            double prix = rset.getDouble(3);
            java.util.Date date = rset.getDate(1);
            String actualDate = sdf.format(date);
            double sousTotal = quantiteC*prix;
            int quantite = Math.min(quantiteC,qtedispo);
            double[] argsDouble = {prix,sousTotal};
            ajouteCommandeC(argsCommandeC,argsDouble,actualDate,quantite);
            prixTotal += sousTotal;
            quantiteC -= quantite;
        }
        rset.close();
        stmt.close();
      }catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
      }
    }
public boolean lockP(int idProduit, String ModeConditionnement, double poidsUnitaire) {
    try {
        PreparedStatement stmtLock = conn.prepareStatement(STLOCKP);
        stmtLock.setInt(1, idProduit);
        stmtLock.setString(2, ModeConditionnement);
        stmtLock.setDouble(3, poidsUnitaire);

        System.out.println("====================================");
        System.out.println("🔒 Verrouillage du produit en cours…");
        System.out.println("Veuillez patienter.");
        System.out.println("====================================\n");

        ResultSet rs = stmtLock.executeQuery();

        System.out.println(" ✅ Le verrouillage a été effectué avec succès.");
        System.out.println("====================================");

        rs.close();
        stmtLock.close();
        return true;

    } catch (SQLException e) {

        System.out.println("⚠️  Impossible de verrouiller le produit.");
        System.out.println("Détails :");
        System.out.println("- idProduit : " + idProduit);
        System.out.println("- ModeConditionnement : " + ModeConditionnement);
        System.out.println("- PoidsUnitaire : " + poidsUnitaire);
        System.out.println("Le produit est actuellement verrouillé par un autre utilisateur.");

        System.out.println("Que souhaitez-vous faire ?");
        System.out.println("1. Quitter");
        System.out.println("2. Attendre encore 30 secondes");
        System.out.print("Votre choix : ");

        int choix = scan.nextInt();
        scan.nextLine();

        if (choix == 2) {
            System.out.println(" ⏳ Nouvelle tentative en cours...");
            lockP(idProduit, ModeConditionnement, poidsUnitaire);
        }
        System.out.println(); 
        return false;

    }
}
public boolean lockC(int idContenant) {
    try {
        PreparedStatement stmtLock = conn.prepareStatement(STLOCKC);
        stmtLock.setInt(1, idContenant);

        System.out.println("====================================");
        System.out.println("🔒  Verrouillage du contenant en cours…");
        System.out.println("Veuillez patienter.");
        System.out.println("====================================");

        ResultSet rs = stmtLock.executeQuery();

        System.out.println("✅ Le verrouillage a été effectué avec succès.");
        System.out.println("====================================");

        rs.close();
        stmtLock.close();
        return true;

    } catch (SQLException e) {

        System.out.println("⚠️  Impossible de verrouiller le contenant.");
        System.out.println("Détails :");
        System.out.println("- idContenant : " + idContenant);


        System.out.println("Que souhaitez-vous faire ?");
        System.out.println("1. Quitter");
        System.out.println("2. Attendre encore 30 secondes");
        System.out.print("Votre choix : ");

        int choix = scan.nextInt();
        scan.nextLine();

        if (choix == 2) {
            System.out.println(" ⏳ Nouvelle tentative en cours...");
            lockC(idContenant);
        }    
        System.out.println(); 
        return false;
    }
}           
}