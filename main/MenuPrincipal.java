import java.util.Scanner;
import java.sql.*;

public class MenuPrincipal {
    Connection connection;
    public MenuPrincipal(Connection conn){
        this.connection = conn;
    }
    /////////////////////////////////////////////////////////////////////////////////////
    public void afficherMenu() {
            Scanner scanner = new Scanner(System.in);
            int choix = 0;
    
            try {
                System.out.println("\n========================================================");
                System.out.println("                      ☰ MAIN MENU                       ");
                System.out.println("========================================================");
                System.out.println("1. Consulter le catalogue");
                System.out.println("2. Passer une commande");
                System.out.println("3. Suivi des commandes");
                System.out.println("4. Consulter les alertes de péremption");
                System.out.println("5. Clôturer une commande");
                System.out.println("6. Quitter");
                System.out.println("========================================================");
                System.out.println("Saisissez un choix s'il vous plaît ");
    
                /// Vérifie que l’entrée est bien un nombre
                while (!scanner.hasNextInt()) {
                    System.out.print("Veuillez entrer un nombre valide : ");
                    scanner.next();
                }
    
                choix = scanner.nextInt();
    
                // Gestion des choix
                switch (choix) {
                    case 1 -> afficherCatalogue(scanner,choix);
                    case 2 -> passerCommande(scanner);
                    case 3 -> suiviCommandes();
                    case 4 -> consulterAlertes();
                    case 5 -> cloturerCommande();
                    case 6 -> System.out.println("Au revoir !");
                    default -> System.out.println("Choix invalide, veuillez réessayer.");
                }
                scanner.close();
            }
            catch (Exception e) {
                System.err.println("Failed !.");
                e.printStackTrace(System.err);
            }
        }
        public void afficherCatalogue(Scanner scanner, int choix) {
            // Clear le terminal
            try {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            } catch (Exception e) {
                System.out.println("[!] Impossible de clear le terminal");
            }
            try {
                System.out.println("\n==================================== Catalogue des produits ====================================");
                System.out.println("\n");

                // Creation de la requete
                PreparedStatement stmt = connection.prepareStatement(Statement.PRE_STMT);
                // Execution de la requete
                ResultSet rset = stmt.executeQuery();
                // Affichage du resultat
                dumpResultSet(rset);
                System.out.println("");
                System.out.println(" 0 : Retour au menu prinicpal");
                choix = scanner.nextInt();
                // Gestion des choix
                switch (choix) {
                    case 0 -> {
                        // Clear le terminal
                        try {
                            new ProcessBuilder("clear").inheritIO().start().waitFor();
                        } catch (Exception e) {
                            System.out.println("[!] Impossible de clear le terminal");
                        }
                        afficherMenu();
                    }    
                    default -> System.out.println("Choix invalide, veuillez réessayer.");
                }
                scanner.close();

            } catch (Exception e) {
                System.err.println("Erreur lors de l'affichage du catalogue des produits.");
                e.printStackTrace(System.err);
            }
            
        }
    
        public void passerCommande(Scanner scanner){
            // Clear le terminal
            try {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            } catch (Exception e) {
                System.out.println("[!] Impossible de clear le terminal");
            }
            System.out.println("\n ====== Espace commande à passer ======");

            PassCommande commande = new PassCommande(connection,scanner);
            commande.beginCommande();
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
        private void dumpResultSet(ResultSet rset) throws SQLException {
            ResultSetMetaData rsetmd = rset.getMetaData();
            int columnCount = rsetmd.getColumnCount();

            // Largeur personnalisée par colonne
            int[] widths = {13, 20, 25, 38, 15, 15};
            for (int i = 1; i <= columnCount; i++) {
                int width = i <= widths.length ? widths[i - 1] : 20;
                System.out.printf("%-" + width + "s", rsetmd.getColumnName(i));
            }
            System.out.println();
            for (int i = 1; i <= columnCount; i++) {
                int width = i <= widths.length ? widths[i - 1] : 20;
            }

            System.out.println("\n" + "=".repeat(125));
            
            while (rset.next()) {
                for (int j = 1; j <= columnCount; j++) {
                    int width = j <= widths.length ? widths[j - 1] : 20;
                    String value = rset.getString(j);
                    if (value == null) value = "";
                    if (value.length() > width - 1) value = value.substring(0, width - 2) + "…"; 
                    System.out.printf("%-" + width + "s", value);
                }
                System.out.println();
            }
        
            System.out.println("=".repeat(125));

            }
    }
    
    