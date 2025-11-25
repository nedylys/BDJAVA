package main;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class MenuPrincipal {
    private Connection connection;

    public MenuPrincipal(Connection conn){
        this.connection = conn;
    }
    /////////////////////////////////////////////////////////////////////////////////////
    public void afficherMenu() {
            Scanner scanner = new Scanner(System.in);
            int choix = 0;
    
            try {
                connection.setAutoCommit(false);
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
                    case 5 -> cloturerUneCommande();
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
                System.out.println("\n============================================ 📝🛒 Catalogue des produits ========================================");
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

            PassCommande commande = new PassCommande(connection,scanner,this);
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
                System.out.println("\n===========================================================  🚨 Alertes de péremption ================================================================\n");
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
                else{
                    PreparedStatement stmtp = connection.prepareStatement(Statement.Price_reduce);
                    int updated = stmtp.executeUpdate();   
                    if (updated > 0) {
                        System.out.println("Réduction appliquée sur " + updated + " lot(s).");
                        connection.commit(); // Validation des changements
                        System.out.println("Modifications validées dans la base de données.");
                    }
                    // else {
                    //     System.out.println("Aucune réduction appliquée. Réduction déjà appliquée");
                    // }
                stmtp.close();
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
            }
            

            catch (Exception e) {
                System.err.println("Erreur lors de la consultation des alertes de péremption.");
                e.printStackTrace(System.err);
            }

        }
   
    public void cloturerUneCommande(){
            System.out.println("\n ==================================== Espace clôture de commande ====================================");

            ClotureCommande cloture = new ClotureCommande(connection,this);
            cloture.cloturerCommande();
        }
    public void suiviCommandes(){
        System.out.println("\n=== Suivi des commandes ===");
        System.out.print("Entrez l'ID de la commande : ");
        Scanner scanner = new Scanner(System.in); 
        System.out.print("ID commande : ");
        int id = Integer.parseInt(scanner.nextLine());

        String requete = ""; 

        System.out.print("Mode de récupération (ex: livraison, retrait) : ");
        String moderecuperation = scanner.nextLine();

        if (moderecuperation.equals("livraison")) {
            requete =
            "SELECT * from CommandeaLivrer WHERE idCommande = ?";
 
        } else if (moderecuperation.equals("retrait")) {
            requete = " select * from commandeenboutique where idCommande = ? ";
        }
        else {
            System.out.println("Mode de récupération invalide. Veuillez réessayer.");
            return;
        }

        try (PreparedStatement ps = connection.prepareStatement(requete)) {
            ps.setInt(1, id );

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    System.out.println("Aucune commande trouvée.");
                    return;
                }

                System.out.println("\n===== Détails de la commande =====");
                System.out.println("ID commande      : " + rs.getInt("idCommande"));
                System.out.println("Statut commande  : " + rs.getString("StatutCommandeL"));
                System.out.println("Date de livraison estimée     : " + rs.getString("DateLivraisonEstimee"));
                System.out.println("===================================\n");
            }

            System.out.println( " Taper 0 pour revenir au Menu principal");
            int choix = scanner.nextInt();

            switch (choix) {
                case 0 -> {
        
                    afficherMenu();
                }    
                
                default -> {
                    System.out.println("Choix invalide, veuillez réessayer !");
                }
            }
            scanner.close();

            
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
            try { connection.rollback(); } catch (SQLException ignore) {}
        }
    }
        private boolean dumpResultSet(ResultSet rset) throws SQLException {
            ResultSetMetaData rsetmd = rset.getMetaData();
            int columnCount = rsetmd.getColumnCount();
            int padding = 2; // espace entre les colonnes
        
            // Calculer la largeur maximale pour chaque colonne
            int[] widths = new int[columnCount];
            List<String[]> rows = new ArrayList<>();
            boolean hasResults = false;
        
            // Parcourir le ResultSet pour déterminer la largeur max des données
            while (rset.next()) {
                hasResults = true;
                String[] row = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    String value = rset.getString(i);
                    if (value == null) value = "";
                    row[i - 1] = value;
                    widths[i - 1] = Math.max(widths[i - 1], Math.max(value.length(), rsetmd.getColumnName(i).length()));
                }
                rows.add(row);
            }
        
            // Affichage des noms de colonnes
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-" + (widths[i - 1] + padding) + "s", rsetmd.getColumnName(i));
            }
            System.out.println();
        
            // Ligne de séparation
            int totalWidth = Arrays.stream(widths).sum() + columnCount * padding;
            System.out.println("=".repeat(totalWidth));
        
            // Affichage des données
            for (String[] row : rows) {
                for (int i = 0; i < columnCount; i++) {
                    String value = row[i];
                    if (value.length() > widths[i]) value = value.substring(0, widths[i] - 1) + "…";
                    System.out.printf("%-" + (widths[i] + padding) + "s", value);
                }
                System.out.println();
            }
        
            System.out.println("=".repeat(totalWidth));
            return hasResults;
        }
    }
    
    