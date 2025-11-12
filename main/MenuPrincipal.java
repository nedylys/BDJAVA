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
                    case 4 -> consulterAlertes(scanner,choix);
                    case 5 -> cloturerCommande();
                    case 6 -> System.out.println("Au revoir !");
                    default -> System.out.println("Choix invalide, veuillez réessayer.");
                }
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
                System.out.println("\n=============================================== Catalogue des produits ===============================================");
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
                    default -> {
                        System.out.println("Choix invalide, veuillez réessayer.🤕");
                        try {
                            Thread.sleep(1000); // 1000 ms = 1 seconde
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }                        
                        afficherCatalogue(scanner, choix);
                    }

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
            System.out.println("\n ==================================== Espace commande à passer ====================================");

            PassCommande commande = new PassCommande(connection,scanner);
            commande.beginCommande();
        }
    
        public void consulterAlertes(Scanner scanner, int choix){
            // Clear le terminal
            try {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            } catch (Exception e) {
                System.out.println("[!] Impossible de clear le terminal");
            }
            try {
                if (connection == null || connection.isClosed()) {
                    System.out.println("⚠️ La connexion à la base est perdue. Retour au menu.");
                    ConnectionBase connB = new ConnectionBase();
                    Connection conn = connB.beginConnection();
                    afficherMenu();
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la vérification de la connexion.");
                e.printStackTrace();
            }
            
            
            try {
                System.out.println("\n==================================== 🚨 Alertes de péremption ====================================\n");
                // Creation de la requete
                PreparedStatement stmt = connection.prepareStatement(Statement.ALERTES_PRE);
                // Execution de la requete
                ResultSet rset = stmt.executeQuery();
                // Affichage du resultat
                // Appeler dumpResultSet qui retourne un boolean
                boolean hasResults = dumpResultSet(rset);
                
                if (!hasResults) {
                    System.out.println("");
                    System.out.println("Aucune alerte de péremption pour le moment. 🤗");
                    System.out.println("");
                }
                
                System.out.println(" 0 : Retour au menu principal");
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
                        
                        default -> {
                            System.out.println("Choix invalide, veuillez réessayer.🤕");
                            try {
                                Thread.sleep(1000); // 1000 ms = 1 seconde
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }                        
                            consulterAlertes(scanner, choix);
                        }
                    }
                    scanner.close();
            }
            

            catch (Exception e) {
                System.err.println("Erreur lors de la consultation des alertes de péremption.");
                e.printStackTrace(System.err);
            }


        }
    
        public void cloturerCommande(){
            System.out.println("\n=== Clôture d'une commande ===");
            // TODO : Implémenter la transaction de clôture (paiement, etc...)
        }
    
        public void suiviCommandes(){
            System.out.println("\n=== Suivi des commandes ===");
            // TODO :Implémenter la transaction pour consulter les commandes en cours
        }
        private boolean dumpResultSet(ResultSet rset) throws SQLException {
            ResultSetMetaData rsetmd = rset.getMetaData();
            int columnCount = rsetmd.getColumnCount();

            int[] widths = {12, 15, 18, 20, 15, 15, 15};
            
            for (int i = 1; i <= columnCount; i++) {
                int width = i <= widths.length ? widths[i - 1] : 20;
                System.out.printf("%-" + width + "s", rsetmd.getColumnName(i));
            }
            System.out.println();
            for (int i = 1; i <= columnCount; i++) {
                int width = i <= widths.length ? widths[i - 1] : 20;
            }

            System.out.println("\n" + "=".repeat(125));
            
            boolean hasResults = false;
            while (rset.next()) {
                hasResults = true;  // Au moins un résultat trouvé
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
            return hasResults;  // Retourne true si des résultats ont été affichés
        }
    }
    
    