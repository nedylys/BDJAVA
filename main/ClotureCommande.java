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
            // String requete1 = "SELECT * FROM commande";
            // PreparedStatement commandes = connection.prepareStatement(requete1);
            // ResultSet rs = commandes.executeQuery();

            // boolean vide = true;
            // System.out.println("\n===== Commandes =====");

            // while (rs.next()) {
            //     vide = false;
            //     System.out.println("ID commande       : " + rs.getInt("idCommande"));
            //     System.out.println("ID client         : " + rs.getInt("idClient"));
            //     System.out.println("Mode récupération : " + rs.getString("ModeRecuperation"));
            //     System.out.println("------------------------------");
            // }

            // if (vide) {
            //     System.out.println("Aucune commande trouvée.");
            // }



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
                int FraisLivraison = (distance < 10) ? 5 : (10 + 10*((int)distance/100));
                System.out.print("Date estimée de livraison (YYYY-MM-DD) : ");
                String dateEstime = scanner.nextLine();
                System.out.print("Adresse de livraison : Votre adresse Principale ? (true / false  ) ");
                boolean adressePrincipale = Boolean.parseBoolean(scanner.nextLine());
                if (!adressePrincipale){
                    System.out.print("Renseignez l'adresse de livraison : ");
                    String adresseLivraison = scanner.nextLine();
                    String requeteAdresse = " Update CommandeaLivrer set adresseLivraison = ? where idCommande = ? " ;
                    PreparedStatement psAdresse = connection.prepareStatement(requeteAdresse);
                    psAdresse.setString(1, adresseLivraison);
                    psAdresse.setInt(2, id);
                    psAdresse.executeUpdate();
                }
                requete = " Update CommandeaLivrer set StatutCommandeL = ? , FraisLivraison = ? , DateLivraisonEstimee = TO_DATE(?, 'YYYY-MM-DD') where idCommande = ? " ;
                PreparedStatement psLivraison = connection.prepareStatement(requete);
                psLivraison.setString(1, statut);
                psLivraison.setInt(2, FraisLivraison);
                psLivraison.setDate(3, Date.valueOf(dateEstime));
                psLivraison.setInt(4, id);
                psLivraison.executeUpdate();   
            }
            System.out.print(
            " === La commande est en cours de clôture : \n "+
            " === Tapez 0 pour retour \n " +
            " === Valider l'achat : R = Récupérée, L = Livrée, A = Annulée .\n");
            String key = scanner.nextLine().trim().toUpperCase();

            if (key.equals("R")) {
                String req = "UPDATE Commandeenboutique SET StatutCommandeB = ? WHERE idCommande = ?";
                PreparedStatement ps = connection.prepareStatement(req);
                ps.setString(1, "Recuperee");
                ps.setInt(2, id);
                ps.executeUpdate();

                System.out.println("Commande marquée comme Récupérée !");
                
            }

            else if  (key.equals("L")) {
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
                ps1.setString(1, "Annulée");
                ps1.setInt(2, id);
                ps1.executeUpdate();

                PreparedStatement ps2 = connection.prepareStatement(req2);
                ps2.setString(1, "Annulée");
                ps2.setInt(2, id);
                ps2.executeUpdate();

                System.out.println("Commande annulée !");
                retour();
                return;
            }
            else if ( key.equals("0") ) {
                retour();
                return ;    }
            else {
                System.out.println("Choix invalide. La commande n'a pas été clôturée.");
                retour();
                return; }

        
            
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