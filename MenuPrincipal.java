import java.util.Scanner;

public class MenuPrincipal {

    public void afficherCatalogue() {
        System.out.println("\n=== Catalogue des produits ===");
        // TODO : Implémenter la requête SQL SELECT * FROM PRODUIT
    }

    public void passerCommande(){
        System.out.println("\n === Espace commande à passer ===");
        // TODO : Implémenter la transaction de création de commande
    }

    public void consulterAlertes(){
        System.out.println("\n=== Alertes de péremption ===");
        // TODO : Implémenter la requête pour produits proches de la date limite
    }

    public void cloturerCommande(){
        System.out.println("\n=== Clôture d'une commande ===");
        // TODO : Implémenter la transaction de clôture (paiement, etc...)
    }

    public void suiviCommandes(){
        System.out.println("\n=== Suivi des commandes ===");
        // TODO :Implémenter la transaction pour consulter les commandes en cours
    }
    /////////////////////////////////////////////////////////////////////////////////////
    public void afficherMenu() {
            Scanner scanner = new Scanner(System.in);
            int choix = 0;
    
            do {
                System.out.println("\n========================================");
                System.out.println("        GESTION DES COMMANDES          ");
                System.out.println("========================================");
                System.out.println("1. Consulter le catalogue");
                System.out.println("2. Passer une commande");
                System.out.println("3. Suivi des commandes");
                System.out.println("4. Consulter les alertes de péremption");
                System.out.println("5. Clôturer une commande");
                System.out.println("6. Quitter");
                System.out.println("========================================");
                System.out.println("Saisissez un choix s'il vous plaît ");
    
                /// Vérifie que l’entrée est bien un nombre
                while (!scanner.hasNextInt()) {
                    System.out.print("Veuillez entrer un nombre valide :) ");
                    scanner.next();
                }
    
                choix = scanner.nextInt();
    
                // Gestion des choix
                switch (choix) {
                    case 1 -> afficherCatalogue();
                    case 2 -> passerCommande();
                    case 3 -> suiviCommandes();
                    case 4 -> consulterAlertes();
                    case 5 -> cloturerCommande();
                    case 6 -> System.out.println("Au revoir !");
                    default -> System.out.println("Choix invalide, veuillez réessayer.");
                }
    
            } while (choix != 6);
    
            scanner.close();
        }
    
}