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
        System.out.println("\n=== Clôture d'une commande ===");
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("ID commande : ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Mode récupération (RETRAIT / LIVRAISON) : ");
            String modeSaisi = scanner.nextLine().trim();

            String modeRecup = "";
            if (modeSaisi.equalsIgnoreCase("RETRAIT")) {
                modeRecup = "Retrait";
            } else if (modeSaisi.equalsIgnoreCase("LIVRAISON")) {
                modeRecup = "Livraison";
            } else {
                System.out.println("Mode de récupération invalide.");
                retour();
                return;
            }

            
            String statut ; 
            String requete ; 
            if ( modeRecup.equals("Retrait") ) {
                requete = "insert into commandeEnboutique (idCommande, StatutCommandeB) values (?,?) " ; 
                statut = "Prete" ;
                PreparedStatement psRetrait = connection.prepareStatement(requete);
                psRetrait.setInt(1, id);
                psRetrait.setString(2, statut);
                psRetrait.executeUpdate();
            } else if( modeRecup.equals("Livraison") ) {
                
                statut = "En livraison" ;
                System.out.print("Adresse de livraison : ");
                String adresseLivraison = scanner.nextLine();
                System.out.print("Distance de livraison (en km) : ");
                int distance = Integer.parseInt(scanner.nextLine());
                int FraisLivraison = (distance < 10) ? 5 : (10 + 10*((int)distance/100));
                System.out.print("Date estimée de livraison (YYYY-MM-DD) : ");
                String dateEstime = scanner.nextLine();
                requete = " Insert into CommandeaLivrer (idCommande, StatutcommandeL, FraisLivraison, DateLivraisonEstimee, adresseLivraison) values (?,?,?,?,?) " ;
                PreparedStatement psLivraison = connection.prepareStatement(requete);
                psLivraison.setInt(1, id);
                psLivraison.setString(2, statut);
                psLivraison.setInt(3, FraisLivraison);
                psLivraison.setDate(4, Date.valueOf(dateEstime));
                psLivraison.setString(5, adresseLivraison);
                psLivraison.executeUpdate();   
            }
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
        System.out.println( " Taper 0 pour revenir au Menu principal");
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