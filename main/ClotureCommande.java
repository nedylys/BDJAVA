package main;

import java.sql.* ;
import java.util.Scanner;

public class ClotureCommande {
    Connection connection ; 
    MenuPrincipal menu ; 

    public ClotureCommande ( Connection connection , MenuPrincipal menu ){
        this.connection = connection  ; 
        this.menu = menu ; 
    }

    public void cloturerCommande() {
        System.out.println("\n=== Cloture d'une commande ===");
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Voulez-vous afficher la liste des commandes en cours ? (1 = Oui / 0 = Non) : ");
            int choix = Integer.parseInt(scanner.nextLine());

            if (choix == 1) {
                String requete1 ="SELECT DISTINCT c.idCommande, c.idClient, c.ModeRecuperation " +
                                "FROM commande c, commandeenboutique, commandealivrer " +
                                "WHERE (c.idCommande = commandeenboutique.idCommande " +
                                "       OR c.idCommande = commandealivrer.idCommande) " + 
                                "  AND (commandeenboutique.statutcommandeb != 'Recuperee' " +
                                "       AND commandeenboutique.statutcommandeb != 'Annulee' " +
                                "       AND commandealivrer.statutcommandel != 'Livree' " +
                                "       AND commandealivrer.statutcommandel != 'Annulee')";
            PreparedStatement commandes = connection.prepareStatement(requete1);
            ResultSet rs = commandes.executeQuery();

            boolean vide = true;
            System.out.println("\n===== Commandes =====");

            while (rs.next()) {
                vide = false;
                System.out.println("ID commande       : " + rs.getInt("idCommande"));
                System.out.println("ID client         : " + rs.getInt("idClient"));
                System.out.println("Mode récupération : " + rs.getString("ModeRecuperation"));
                System.out.println("------------------------------");
            }

            if (vide) {
                System.out.println("Aucune commande trouvée.");
            }
                
            }
            else if ( choix != 0 && choix != 1 ) {
                System.out.println("Choix invalide.");
                retour();
                return ;
            }

           


            System.out.println("\nEntrez l'ID de la commande à clôturer :\n");
            System.out.print("ID commande : ");
            int id = Integer.parseInt(scanner.nextLine());
            try {
                PreparedStatement lock = connection.prepareStatement(
                        "SELECT idCommande FROM Commande WHERE idCommande = ? FOR UPDATE NOWAIT"
                );
                lock.setInt(1, id);
                lock.executeQuery();

                System.out.println("Verrou Oracle obtenu : vous pouvez clôturer la commande.");

            } catch (SQLException e) {
                if (e.getErrorCode() == 54) { // ORA-00054
                    System.out.println("Cette commande est déjà en cours de clôture par un autre gérant.");
                    retour();
                    return;
                } else {
                    throw e;
                }
            }
            

            String modeRecup = "";
            PreparedStatement psMode = connection.prepareStatement(
                "SELECT ModeRecuperation FROM Commande WHERE idCommande = ?"
            );
            psMode.setInt(1, id);
            ResultSet rsMode = psMode.executeQuery();
            if (rsMode.next()) {
                modeRecup = rsMode.getString("ModeRecuperation");
            } else {
                connection.rollback();
                System.out.println("Commande non trouvée.");
                retour();
                return;
            }

            
            String statut ="Prete"; 
            String requete ; 
            if ( modeRecup.equals("Retrait") ) {
                String StatuC = "Select StatutCommandeB from Commandeenboutique where idCommande = ? " ;
                PreparedStatement psStatut = connection.prepareStatement(StatuC);
                psStatut.setInt(1, id);
                ResultSet rsStatut = psStatut.executeQuery();
                if (rsStatut.next()) {
                    String statutActuel = rsStatut.getString("StatutCommandeB");
                    if (!statutActuel.equals("Prete")){
                    requete = "Update Commandeenboutique set StatutCommandeB = ? where idCommande = ? " ; 
                    PreparedStatement psRetrait = connection.prepareStatement(requete);
                    psRetrait.setString(1, statut);
                    psRetrait.setInt(2, id);
                    psRetrait.executeUpdate();

                    String depiterStockp = "UPDATE LotProduit " + 
                                    "       SET QuantiteDisponibleP = QuantiteDisponibleP - ( " + 
                                    "           SELECT QuantiteCommandeeP " + 
                                    "           FROM LigneCommandeProduit " + 
                                    "           WHERE idCommande = ? " + 
                                    "             AND LigneCommandeProduit.idProduit = LotProduit.idProduit " + 
                                    "             AND LigneCommandeProduit.DateReceptionP = LotProduit.DateReceptionP " + 
                                    "             AND LigneCommandeProduit.ModeConditionnement = LotProduit.ModeConditionnement " + 
                                    "             AND LigneCommandeProduit.PoidsUnitaire = LotProduit.PoidsUnitaire " + 
                                    "       ) " +
                                    "       WHERE EXISTS ( " +
                                    "           SELECT 1 " + 
                                    "           FROM LigneCommandeProduit " + 
                                    "           WHERE idCommande = ? " + 
                                    "             AND LigneCommandeProduit.idProduit = LotProduit.idProduit " + 
                                    "             AND LigneCommandeProduit.DateReceptionP = LotProduit.DateReceptionP " + 
                                    "             AND LigneCommandeProduit.ModeConditionnement = LotProduit.ModeConditionnement " + 
                                    "             AND LigneCommandeProduit.PoidsUnitaire = LotProduit.PoidsUnitaire " + 
                                    "       )";

                    String depiterStockc = "UPDATE LotContenant " + 
                                    "       SET QuantiteDisponibleC = QuantiteDisponibleC - ( " + 
                                    "           SELECT QuantiteCommandeeC " + 
                                    "           FROM LigneCommandeContenant " + 
                                    "           WHERE idCommande = ? " + 
                                    "             AND LigneCommandeContenant.idContenant = LotContenant.idContenant " + 
                                    "             AND LigneCommandeContenant.DateReceptionC = LotContenant.DateReceptionC " + 
                                    "       ) " +
                                    "       WHERE EXISTS ( " +
                                    "           SELECT 1 " + 
                                    "           FROM LigneCommandeContenant " + 
                                    "           WHERE idCommande = ? " + 
                                    "             AND LigneCommandeContenant.idContenant = LotContenant.idContenant " + 
                                    "             AND LigneCommandeContenant.DateReceptionC = LotContenant.DateReceptionC " + 
                                    "       )";

                    PreparedStatement psDepiterP = connection.prepareStatement(depiterStockp);
                    PreparedStatement psDepiterC = connection.prepareStatement(depiterStockc);   
                    psDepiterP.setInt(1, id)  ;
                    psDepiterP.setInt(2, id);
                    psDepiterC.setInt(1, id)  ;
                    psDepiterC.setInt(2, id);
                    psDepiterP.executeUpdate()  ;
                    psDepiterC.executeUpdate()  ;
                    connection.commit()  ;
                    }
                }
            } else if( modeRecup.equals("Livraison") ) {
                String StatuL = "Select StatutCommandeL from CommandeaLivrer where idCommande = ? " ;
                PreparedStatement psStatutL = connection.prepareStatement(StatuL);
                psStatutL.setInt(1, id);
                ResultSet rsStatutL = psStatutL.executeQuery();
                if (rsStatutL.next()) { 
                    String statutActuelL = rsStatutL.getString("StatutCommandeL");
                    if (!statutActuelL.equals("Prete")){
 
                        System.out.print("Distance de livraison (en km) : ");
                        int distance = Integer.parseInt(scanner.nextLine());
                        int FraisLivraison = (distance < 10) ? 5 : (10 + 10*((int)distance/100));
                        System.out.print("Date estimée de livraison (YYYY-MM-DD) : ");
                        String dateEstime = scanner.nextLine();
                        requete = " Update CommandeaLivrer set StatutCommandeL = ? , FraisLivraison = ? , DateLivraisonEstimee = TO_DATE(?, 'YYYY-MM-DD') where idCommande = ? " ;
                        PreparedStatement psLivraison = connection.prepareStatement(requete);
                        psLivraison.setString(1, statut);
                        psLivraison.setInt(2, FraisLivraison);
                        psLivraison.setDate(3, Date.valueOf(dateEstime));
                        psLivraison.setInt(4, id);
                        psLivraison.executeUpdate();
                        String depiterStockp = "UPDATE LotProduit " + 
                                        "       SET QuantiteDisponibleP = QuantiteDisponibleP - ( " + 
                                        "           SELECT QuantiteCommandeeP " + 
                                        "           FROM LigneCommandeProduit " + 
                                        "           WHERE idCommande = ? " + 
                                        "             AND LigneCommandeProduit.idProduit = LotProduit.idProduit " + 
                                        "             AND LigneCommandeProduit.DateReceptionP = LotProduit.DateReceptionP " + 
                                        "             AND LigneCommandeProduit.ModeConditionnement = LotProduit.ModeConditionnement " + 
                                        "             AND LigneCommandeProduit.PoidsUnitaire = LotProduit.PoidsUnitaire " + 
                                        "       ) " +
                                        "       WHERE EXISTS ( " +
                                        "           SELECT 1 " + 
                                        "           FROM LigneCommandeProduit " + 
                                        "           WHERE idCommande = ? " + 
                                        "             AND LigneCommandeProduit.idProduit = LotProduit.idProduit " + 
                                        "             AND LigneCommandeProduit.DateReceptionP = LotProduit.DateReceptionP " + 
                                        "             AND LigneCommandeProduit.ModeConditionnement = LotProduit.ModeConditionnement " + 
                                        "             AND LigneCommandeProduit.PoidsUnitaire = LotProduit.PoidsUnitaire " + 
                                        "       )";

                        String depiterStockc = "UPDATE LotContenant " + 
                                        "       SET QuantiteDisponibleC = QuantiteDisponibleC - ( " + 
                                        "           SELECT QuantiteCommandeeC " + 
                                        "           FROM LigneCommandeContenant " + 
                                        "           WHERE idCommande = ? " + 
                                        "             AND LigneCommandeContenant.idContenant = LotContenant.idContenant " + 
                                        "             AND LigneCommandeContenant.DateReceptionC = LotContenant.DateReceptionC " + 
                                        "       ) " +
                                        "       WHERE EXISTS ( " +
                                        "           SELECT 1 " + 
                                        "           FROM LigneCommandeContenant " + 
                                        "           WHERE idCommande = ? " + 
                                        "             AND LigneCommandeContenant.idContenant = LotContenant.idContenant " + 
                                        "             AND LigneCommandeContenant.DateReceptionC = LotContenant.DateReceptionC " + 
                                        "       )";

                        PreparedStatement psDepiterP = connection.prepareStatement(depiterStockp);
                        PreparedStatement psDepiterC = connection.prepareStatement(depiterStockc);   
                        psDepiterP.setInt(1, id)  ;
                        psDepiterP.setInt(2, id);
                        psDepiterC.setInt(1, id)  ;
                        psDepiterC.setInt(2, id);
                        psDepiterP.executeUpdate()  ;
                        psDepiterC.executeUpdate()  ;
                        connection.commit()  ;
                        System.out.println( " === Stock débité " ) ;
                    }
                }
            }
            else {
                System.out.println("Mode de récupération invalide.");
                retour();
                return ;
            }
            
            System.out.print("=== Tapez 0 pour retour \n " +
            "=== Valider l'achat : V = validée, A = Annulée .\n");
            String key = scanner.nextLine().trim().toUpperCase();

            if (key.equals("V") && modeRecup.equals("Retrait")) {
                String req = "UPDATE Commandeenboutique SET StatutCommandeB = ? WHERE idCommande = ?";
                PreparedStatement ps = connection.prepareStatement(req);
                ps.setString(1, "Recuperee");
                ps.setInt(2, id);
                ps.executeUpdate();

                System.out.println("Commande marquée comme Récupérée !");


                
            }

            else if  (key.equals("V") && modeRecup.equals("Livraison")) {
                String req = "UPDATE CommandeaLivrer SET StatutCommandeL = ? WHERE idCommande = ?";
                PreparedStatement ps = connection.prepareStatement(req);
                ps.setString(1, "Livree");
                ps.setInt(2, id);
                ps.executeUpdate();

                System.out.println("Commande marquée comme Livrée !");
                

            }

            else if (key.equals("A")) {
                String req1 = "UPDATE Commandeenboutique SET StatutCommandeB = ? WHERE idCommande = ?";
                String req2 = "UPDATE CommandeaLivrer SET StatutCommandeL = ? WHERE idCommande = ?";

                PreparedStatement ps1 = connection.prepareStatement(req1);
                ps1.setString(1, "Annulee");
                ps1.setInt(2, id);
                ps1.executeUpdate();

                PreparedStatement ps2 = connection.prepareStatement(req2);
                ps2.setString(1, "Annulee");
                ps2.setInt(2, id);
                ps2.executeUpdate();

                String restoreStockP =
                "UPDATE LotProduit " +
                "   SET QuantiteDisponibleP = QuantiteDisponibleP + ( " +
                "       SELECT QuantiteCommandeeP " +
                "       FROM LigneCommandeProduit " +
                "       WHERE idCommande = ? " +
                "         AND LigneCommandeProduit.idProduit = LotProduit.idProduit " +
                "         AND LigneCommandeProduit.DateReceptionP = LotProduit.DateReceptionP " +
                "         AND LigneCommandeProduit.ModeConditionnement = LotProduit.ModeConditionnement " +
                "         AND LigneCommandeProduit.PoidsUnitaire = LotProduit.PoidsUnitaire " +
                "   ) " +
                " WHERE EXISTS ( " +
                "       SELECT 1 FROM LigneCommandeProduit " +
                "       WHERE idCommande = ? " +
                "         AND LigneCommandeProduit.idProduit = LotProduit.idProduit " +
                "         AND LigneCommandeProduit.DateReceptionP = LotProduit.DateReceptionP " +
                "         AND LigneCommandeProduit.ModeConditionnement = LotProduit.ModeConditionnement " +
                "         AND LigneCommandeProduit.PoidsUnitaire = LotProduit.PoidsUnitaire " +
                "   )";

        
                String restoreStockC =
                    "UPDATE LotContenant " +
                    "   SET QuantiteDisponibleC = QuantiteDisponibleC + ( " +
                    "       SELECT QuantiteCommandeeC " +
                    "       FROM LigneCommandeContenant " +
                    "       WHERE idCommande = ? " +
                    "         AND LigneCommandeContenant.idContenant = LotContenant.idContenant " +
                    "         AND LigneCommandeContenant.DateReceptionC = LotContenant.DateReceptionC " +
                    "   ) " +
                    " WHERE EXISTS ( " +
                    "       SELECT 1 FROM LigneCommandeContenant " +
                    "       WHERE idCommande = ? " +
                    "         AND LigneCommandeContenant.idContenant = LotContenant.idContenant " +
                    "         AND LigneCommandeContenant.DateReceptionC = LotContenant.DateReceptionC " +
                    "   )";

                PreparedStatement psRestP = connection.prepareStatement(restoreStockP);
                PreparedStatement psRestC = connection.prepareStatement(restoreStockC);

                psRestP.setInt(1, id);
                psRestP.setInt(2, id);
                psRestC.setInt(1, id);
                psRestC.setInt(2, id);

                psRestP.executeUpdate();
                psRestC.executeUpdate();
                connection.commit();

                System.out.println("Commande annulée !");
                retour();
                return;
            }
            else if ( key.equals("0") ) {
                retour();
                return ;    }
            else {
                connection.rollback();
                System.out.println("Choix invalide. La commande n'a pas été clôturée.");
                retour();
                return; }
            connection.commit();
            
        }    
        catch (SQLException e) {
            System.out.println("Erreur lors de la clôture de la commande : " + e.getMessage());
            retour();
            return;
        }
       
        
        System.out.println("Commande clôturée avec succès !");
        
        retour();
    }



    public void retour(){
        Scanner scanner = new Scanner(System.in);
        System.out.println( "Taper 0 pour revenir au Menu principal");
        int choix = scanner.nextInt();

        switch (choix) {
            case 0 -> {
    
                menu.afficherMenu();
            }    
            
            default -> {
                System.out.println("Choix invalide, veuillez réessayer !");
            }
        }
    }

    
}