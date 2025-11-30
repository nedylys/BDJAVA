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
                System.out.println("6. Gestion des pertes");
                System.out.println("7. Quitter");
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
                    case 6 -> gestionPertes();
                    case 7 -> System.out.println("Au revoir !");
                    default -> System.out.println("Choix invalide, veuillez réessayer.");
                }
            }
            catch (Exception e) {
                System.err.println("Failed !.");
                e.printStackTrace(System.err);
            }
        }

        public void afficherProduits(Scanner scanner){
            // Clear le terminal
            try {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            } catch (Exception e) {
                System.out.println("[!] Impossible de clear le terminal");
            }
            try {
                System.out.println("\n============================================ 📝🛒 Produits disponible  ========================================");
                System.out.println("\n");
    
                // Creation de la requete
                PreparedStatement stmt = connection.prepareStatement(Statement.PRE_STMT_COMMANDE);
                // Execution de la requete
                ResultSet rset = stmt.executeQuery();
                // Affichage du resultat
                dumpResultSet(rset);
            } catch (Exception e) {
                System.err.println("Erreur lors de l'affichage des produits.");
                e.printStackTrace(System.err);
                
            /// 
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
                            Thread.sleep(5000); // 1000 ms = 1 seconde
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
                int oldIsolation = connection.getTransactionIsolation();
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                System.out.println("\n===========================================================  🚨 Alertes de péremption ================================================================\n");
                PreparedStatement stmtp = connection.prepareStatement(Statement.Price_reduce);
                int updated = stmtp.executeUpdate();
                PreparedStatement stmt = connection.prepareStatement(Statement.ALERTES_PRE);
                ResultSet rset = stmt.executeQuery();
                boolean hasResults = dumpResultSet(rset);
                if (!hasResults) {
                    System.out.println("\nAucune alerte de péremption pour le moment. 🤗\n");
                } else {
                    // On affiche le message de réduction après le tableau
                    if (updated > 0) {
                        System.out.println("\n💸 Réduction appliquée et prix mis à jour sur " + updated + " lot(s).");
                    } else {
                        System.out.println("\n✅ Les prix affichés incluent déjà les réductions.");
                    }
                }
                connection.commit();
                // 6. On remet le niveau d'isolation par défaut pour ne pas impacter le reste de l'app
                connection.setTransactionIsolation(oldIsolation);
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
                                Thread.sleep(5000); // 1000 ms = 1 seconde
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
                String statut = moderecuperation.equals("retrait") ? rs.getString("StatutCommandeB") : rs.getString("StatutCommandeL");
                System.out.println("\n===== Détails de la commande =====");
                System.out.println("ID commande      : " + rs.getInt("idCommande"));
                System.out.println("Statut commande  : " + statut );
                if (moderecuperation.equals("livraison")) {
                    
                    System.out.println("Date de livraison estimée     : " + rs.getString("DateLivraisonEstimee"));}
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
    public void gestionPertes(){
        System.out.println("\n ==================================== Espace gestion des pertes ====================================");
        System.out.println("1 : Déclarer une perte Produit");
        System.out.println("2 : Déclarer une perte Contenant");
        System.out.println("0 : Retour");
        System.out.print("Votre choix : ");
        Scanner scan = new Scanner(System.in); 
        int choix = scan.nextInt();
        scan.nextLine();

        if (choix == 1) {
            perteProduit();
        } else if (choix == 2) {
            perteContenant();
        } else {
            return;
        }
    }
    private void perteProduit() {
    try {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n--- Déclarer une perte PRODUIT ---");

        System.out.print("IdProduit : ");
        int idProduit = Integer.parseInt(scanner.nextLine());

        System.out.print("Date de Réception (YYYY-MM-DD) : ");
        java.sql.Date dateRec = java.sql.Date.valueOf(scanner.nextLine());

        System.out.print("Mode Conditionnement (vrac/preconditionne) : ");
        String mode = scanner.nextLine();

        System.out.print("Poids unitaire : ");
        float poids = Float.parseFloat(scanner.nextLine());

        PreparedStatement check = connection.prepareStatement(
            "SELECT QuantiteDisponibleP FROM LotProduit " +
            "WHERE idProduit=? AND DateReceptionP=? AND ModeConditionnement=? AND PoidsUnitaire=?"
        );
        check.setInt(1, idProduit);
        check.setDate(2, dateRec);
        check.setString(3, mode);
        check.setFloat(4, poids);
        ResultSet rs = check.executeQuery();

        if (!rs.next()) {
            System.out.println("Lot introuvable.");
            retour();
            return;
        }

        float dispo = rs.getFloat(1);
        System.out.println("Quantité disponible dans ce lot : " + dispo);

        System.out.print("Quantité perdue : ");
        int qte = Integer.parseInt(scanner.nextLine());

        if (qte > dispo) {
            System.out.println("Erreur : quantité perdue supérieure au stock disponible.");
            retour();
            return;
        }

        System.out.print("Nature (vol/casse) : ");
        String nature = scanner.nextLine();

        PreparedStatement psMax = connection.prepareStatement(
            "SELECT NVL(MAX(idPerteP),0) FROM PerteProduit"
        );
        ResultSet rmax = psMax.executeQuery();
        rmax.next();
        int idPerte = rmax.getInt(1) + 1;


        PreparedStatement insert = connection.prepareStatement(
            "INSERT INTO PerteProduit VALUES (?, ?, ?, ?, ?, SYSDATE, ?, ?)"
        );
        insert.setInt(1, idPerte);
        insert.setInt(2, idProduit);
        insert.setString(3, mode);
        insert.setFloat(4, poids);
        insert.setDate(5, dateRec);
        insert.setInt(6, qte);
        insert.setString(7, nature);
        insert.executeUpdate();

  
        PreparedStatement upd = connection.prepareStatement(
            "UPDATE LotProduit SET QuantiteDisponibleP = QuantiteDisponibleP - ? " +
            "WHERE idProduit=? AND DateReceptionP=? AND ModeConditionnement=? AND PoidsUnitaire=?"
        );
        upd.setInt(1, qte);
        upd.setInt(2, idProduit);
        upd.setDate(3, dateRec);
        upd.setString(4, mode);
        upd.setFloat(5, poids);
        upd.executeUpdate();

        connection.commit();
        System.out.println("Perte PRODUIT enregistrée et stock mis à jour.");
        retour();

    } catch (Exception e) {
        try { connection.rollback(); } catch(Exception ignored) {}
        System.out.println("Erreur : " + e.getMessage());
    }
}


    private void perteContenant() {
    try {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n--- Déclarer une perte CONTENANT ---");

        // Identifiant du contenant
        System.out.print("IdContenant : ");
        int idContenant = Integer.parseInt(scanner.nextLine());

      
        System.out.print("Date de Réception (YYYY-MM-DD) : ");
        java.sql.Date dateRec = java.sql.Date.valueOf(scanner.nextLine());

       
        PreparedStatement check = connection.prepareStatement(
            "SELECT QuantiteDisponibleC FROM LotContenant " +
            "WHERE idContenant=? AND DateReceptionC=?"
        );
        check.setInt(1, idContenant);
        check.setDate(2, dateRec);
        ResultSet rs = check.executeQuery();

        if (!rs.next()) {
            System.out.println("Lot introuvable.");
            retour();
            return;
        }

        int dispo = rs.getInt(1);
        System.out.println("Quantité disponible dans ce lot : " + dispo);

        // Quantité perdue
        System.out.print("Quantité perdue : ");
        int qte = Integer.parseInt(scanner.nextLine());

        if (qte > dispo) {
            System.out.println("Erreur : quantité perdue supérieure au stock disponible.");
            retour();
            return;
        }

        // Nature
        System.out.print("Nature (vol/casse) : ");
        String nature = scanner.nextLine();

        PreparedStatement psMax = connection.prepareStatement(
            "SELECT NVL(MAX(idPerteC),0) FROM PerteContenant"
        );
        ResultSet rmax = psMax.executeQuery();
        rmax.next();
        int idPerte = rmax.getInt(1) + 1;

        
        PreparedStatement insert = connection.prepareStatement(
            "INSERT INTO PerteContenant VALUES (?, ?, ?, SYSDATE, ?, ?)"
        );
        insert.setInt(1, idPerte);
        insert.setInt(2, idContenant);
        insert.setDate(3, dateRec);
        insert.setInt(4, qte);
        insert.setString(5, nature);
        insert.executeUpdate();

        // Mise à jour du stock du lot
        PreparedStatement upd = connection.prepareStatement(
            "UPDATE LotContenant SET QuantiteDisponibleC = QuantiteDisponibleC - ? " +
            "WHERE idContenant=? AND DateReceptionC=?"
        );
        upd.setInt(1, qte);
        upd.setInt(2, idContenant);
        upd.setDate(3, dateRec);
        upd.executeUpdate();

        connection.commit();
        System.out.println("Perte CONTENANT enregistrée et stock mis à jour.");
        retour();

    } catch (Exception e) {
        try { connection.rollback(); } catch(Exception ignored) {}
        System.out.println("Erreur : " + e.getMessage());
    }
}




    public void retour(){
        Scanner scanner = new Scanner(System.in);
        System.out.println( "Taper 0 pour revenir au Menu principal");
        int choix = scanner.nextInt();

        switch (choix) {
            case 0 -> {
    
                afficherMenu();
            }    
            
            default -> {
                System.out.println("Choix invalide, veuillez réessayer !");
            }
        }
    }


        private boolean dumpResultSet(ResultSet rset) throws SQLException {
            ResultSetMetaData rsetmd = rset.getMetaData();
            int columnCount = rsetmd.getColumnCount();
            int padding = 2; // espace entre les colonnes
        

            int[] widths = new int[columnCount];
            List<String[]> rows = new ArrayList<>();
            boolean hasResults = false;
        
    
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
    
    