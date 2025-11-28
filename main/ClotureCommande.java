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
        System.out.println("\n=== ClÃƒÂ´ture d'une commande ===");
        try {
            connection.rollback();
            Scanner scanner = new Scanner(System.in);

            System.out.print("ID commande : ");
            int id = Integer.parseInt(scanner.nextLine());

            

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
                System.out.println("Commande non trouvÃƒÂ©e.");
                retour();
                return;
            }

            
            String statut ; 
            String requete ; 
            if ( modeRecup.equals("Retrait") ) {
                requete = "Update Commandeenboutique set StatutCommandeB = ? where idCommande = ? " ; 
                statut = "Prete" ;
                PreparedStatement psRetrait = connection.prepareStatement(requete);
                psRetrait.setString(1, statut);
                psRetrait.setInt(2, id);
                psRetrait.executeUpdate();
            } else if( modeRecup.equals("Livraison") ) {
                
                statut = "En livraison" ;
                System.out.print("Distance de livraison (en km) : ");
                int distance = Integer.parseInt(scanner.nextLine());
                int Pays = 1;
                int FraisLivraison = (distance < 10) ? 5 : (10 + 10*((int)distance/100) + Pays);
                System.out.print("Date estimée de livraison (YYYY-MM-DD) : ");
                String dateEstime = scanner.nextLine();
                requete = " Insert into CommandeaLivrer (idCommande, StatutcommandeL, FraisLivraison, DateLivraisonEstimee, adresseLivraison) values (?,?,?,?,?) " ;
                PreparedStatement psLivraison = connection.prepareStatement(requete);
                psLivraison.setString(1, statut);
                psLivraison.setInt(2, FraisLivraison);
                psLivraison.setDate(3, Date.valueOf(dateEstime));
                psLivraison.setInt(4, id);
                psLivraison.executeUpdate();   
            }
        }    
        catch (SQLException e) {
            System.out.println("Erreur lors de la clÃƒÂ´ture de la commande : " + e.getMessage());
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
                System.out.println("Choix invalide, veuillez rÃƒÂ©essayer !");
            }
        }
    }

    
}