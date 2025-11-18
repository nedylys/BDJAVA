package main;

import java.sql.*;
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
    
    public void cloturerCommande() {
        System.out.println("\n=== Clôture d'une commande ===");
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("ID commande : ");
            int id = Integer.parseInt(scanner.nextLine());

            // 1) Lire le statut actuel
            String statutActuel = null;
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT StatutCommandeL FROM CommandeaLivrer WHERE idCommande = ? FOR UPDATE")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        statutActuel = rs.getString(1); // 'En preparation', 'Prete', ...
                    } else {
                        System.out.println("Commande inexistante.");
                        return;
                    }
                }
            }

            
            if (!"En preparation".equals(statutActuel) &&
                !"Prete".equals(statutActuel)) {
                System.out.println("Cette commande ne peut plus être clôturée.");
                return;
            }

            
            System.out.print("Mode récupération (RETRAIT / LIVRAISON) : ");
            String modeSaisi = scanner.nextLine().trim();

            String modeRecup;
            if (modeSaisi.equalsIgnoreCase("RETRAIT")) {
                modeRecup = "Retrait";
            } else if (modeSaisi.equalsIgnoreCase("LIVRAISON")) {
                modeRecup = "Livraison";
            } else {
                System.out.println("Mode de récupération invalide.");
                return;
            }

            
            System.out.print("Mode paiement (En ligne / En boutique) : ");
            String paiementSaisi = scanner.nextLine().trim();

            String modePaiement;
            if (paiementSaisi.equalsIgnoreCase("En ligne")) {
                modePaiement = "En ligne";
            } else if (paiementSaisi.equalsIgnoreCase("En boutique")) {
                modePaiement = "En boutique";
            } else {
                System.out.println("Mode de paiement invalide.");
                return;
            }

            
            try (PreparedStatement ps = connection.prepareStatement(
                    "UPDATE Commande SET ModePaiement = ?, ModeRecuperation = ? WHERE idCommande = ?")) {
                ps.setString(1, modePaiement);  
                ps.setString(2, modeRecup);      
                ps.setInt(3, id);
                ps.executeUpdate();
            }

            
            if (modeRecup.equals("Livraison")) {
                double frais = 7.5;
                System.out.println("Frais de livraison : " + frais + " €");

                try (PreparedStatement ps = connection.prepareStatement(
                        "UPDATE CommandeaLivrer " +
                        "SET FraisLivraison = ?, DateLivraisonEstimee = SYSDATE " +
                        "WHERE idCommande = ?")) {
                    ps.setDouble(1, frais);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                }
            }

            
            String statutFinal = "Livree"; // on considère que la clôture = Livree

            try (PreparedStatement ps = connection.prepareStatement(
                    "UPDATE CommandeaLivrer SET StatutCommandeL = ? WHERE idCommande = ?")) {
                ps.setString(1, statutFinal);
                ps.setInt(2, id);
                ps.executeUpdate();
            }

            connection.commit();
            System.out.println("Commande clôturée avec succès.");

        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            try { connection.rollback(); } catch (Exception ignore) {}
        }
    }

    
    public void suiviCommandes(){
            System.out.println("\n=== Suivi des commandes ===");
            System.out.print("Entrez l'ID de la commande : ");
            

            String requete =
                "SELECT * from CommandeaLivrer WHERE idCommande = ?";

            try (PreparedStatement ps = connection.prepareStatement(requete)) {
                ps.setInt(1, 1001);

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

                connection.commit();
                
            } catch (SQLException e) {
                System.err.println("Erreur SQL : " + e.getMessage());
                try { connection.rollback(); } catch (SQLException ignore) {}
            }
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
    
    