package main;

import java.sql.*;
import java.util.*;

public class PassCommande{
    
    private Connection conn;
    private StatementCommande statementcomm;
    private MenuPrincipal menu;
    private Scanner scan;
    private int idCommande;
    private double prixCommande;
    private List<ProduitPanier> panierProduits;
    private List<ContenantPanier> panierContenants;
    
    public PassCommande(Connection conn,Scanner scan,MenuPrincipal menu){
        this.conn = conn;
        this.scan = scan;
        this.statementcomm = new StatementCommande(conn);
        this.menu = menu;
        prixCommande = 0;
        idCommande = statementcomm.nbIdCommade();
        panierProduits = new ArrayList<>();
        panierContenants = new ArrayList<>();
        System.out.println(" 1 : Commander un Produit ");
        System.out.println(" 2 : Commander un Contenant");
        System.out.println(" 3 : Annuler une commande ");
        System.out.println(" 4 : Finaliser une commande ");
        System.out.println(" 5 : Retour au menu prinicpal");
    }
    public void beginCommande(){
        int numchoisi = scan.nextInt();
        scan.nextLine();
        if (numchoisi == 1 ){
            commandeProduit();
        }
        else if(numchoisi == 2){
            commandeContenant();
        }
        else if(numchoisi == 3){
            annuleCommande();
        }else if(numchoisi == 4){
            finalCommande();
        } else {
            retour();
        }
    }
    public void commandeProduit(){
        ///
        menu.afficherProduits(scan, idCommande);
        /// 
        System.out.println("Entrer l'idProduit : ");
        int idProduit = scan.nextInt();
        scan.nextLine();
        if (!(statementcomm.verifieIdProduit(idProduit))){
            commandeProduit();
        }
        System.err.println("Modes de Conditionnement disponibles : ");
        statementcomm.getModeConditionnement(idProduit);
        System.out.println("Entrer le Mode de Conditionnement : ");
        String ModeConditionnement = scan.nextLine();
        double PoidsUnitaire;
        while (!(ModeConditionnement.equals("vrac")) && !(ModeConditionnement.equals("preconditionne"))){
            System.out.println("Mauvaise saisie ");
            System.out.println("Entrer le Mode de Conditionnement : ");
            ModeConditionnement = scan.nextLine();
        }
        if (ModeConditionnement.equals("vrac")){
            PoidsUnitaire = 1.0;
        }else{
            System.out.println("Les poidsUnitaire disponibles : ");
            statementcomm.choisirPoidsUnitaire(idProduit);
            System.out.println("Entrer le poids unitaire : ");
            PoidsUnitaire = scan.nextDouble();
            scan.nextLine();
        }
        System.out.println("Entrer la quantité souhaitée : ");
        double qte = scan.nextDouble();
        scan.nextLine();
        boolean commande = statementcomm.inCommande(idProduit);
        if (commande){
            System.out.println("Votre produit est sur commande ");
            int delai = statementcomm.getDelaiDispo(idProduit);
            System.out.println("Votre produit sera disponible dans " + delai);
        }else{
            System.out.println("Votre produit est en stock ");
            boolean dispo = statementcomm.getDispo(idProduit, qte, ModeConditionnement, PoidsUnitaire);
            if (!dispo){
                System.out.println(" 1 : Recommander un Produit ");
                System.out.println(" 2 : Commander un Contenant");
                System.out.println(" 3 : Annuler la commande ");
                System.out.println(" 4 : Finaliser la commande ");
                System.out.println(" 5 : Retour au menu prinicpal");
                System.out.println("Taper le numéro choisi:");
                beginCommande();
            }
        }
        System.out.println("Voulez vous commander le produit ?");
        System.out.println("Taper true or false : ");
        boolean creecommande = scan.nextBoolean(); 
        scan.nextLine();
        if (creecommande){
            int numligneP = 0;
            int[] argsCommande = {numligneP,idCommande,idProduit};
            try{
            // !!!!!!!!!!!!!!!!! il n'a pas payé ni fini sa commande
            // double prix = statementcomm.ajouteCommandeGlobalP(argsCommande, ModeConditionnement, qte,PoidsUnitaire);
            // System.out.println("Cette commande de ce produit vous couteta " + prix); 
                panierProduits.add(new ProduitPanier(idProduit, ModeConditionnement, PoidsUnitaire, qte));
                double prixEstime = statementcomm.calculePrixProduit(idProduit, qte, ModeConditionnement);
                System.out.println("Produit ajouté au panier ! Coût estimé : " + prixEstime);
                this.prixCommande += prixEstime;
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'ajout du produit à la commande." + e.getMessage());
            }
        }
        System.out.println(" 1 : Recommander un Produit ");
        System.out.println(" 2 : Commander un Contenant");
        System.out.println(" 3 : Annuler la commande ");
        System.out.println(" 4 : Finaliser la commande ");
        System.out.println(" 5 : Retour au menu prinicpal");
        System.out.println("Taper le numéro choisi:");
        beginCommande();
    }
    public void commandeContenant(){
        System.out.println(" Entrer l'idContenant : ");
        int idContenant = scan.nextInt();
        scan.nextLine();
        if (!(statementcomm.verfieIdContenant(idContenant))){
            System.out.println("L'idContenant est faux");
            commandeProduit();
        }
        System.out.println(" Entrer la quantité souhaitée : ");
        int qte = scan.nextInt();
        scan.nextLine();
        boolean dispo = statementcomm.getDispoContenant(idContenant, qte);
        if (!dispo){
            commandeContenant();
        }
        System.out.println("Voulez vous commander le contenant ?");
        System.out.println("Taper true or false : ");
        boolean creecommande = scan.nextBoolean(); 
        scan.nextLine();
        if (creecommande){
            int numligneC = 0;
            int[] argsCommandeC = {numligneC,idCommande,idContenant};
            try {
                // sameee !!
                // double prix = statementcomm.ajouteCommandeGlobalC(argsCommandeC, qte);
                // this.prixCommande += prix;
                // System.out.println("Cette commande de ce contenat vous couteta " + prix); 
                panierContenants.add(new ContenantPanier(idContenant, qte));
                double prixEstime = statementcomm.calculePrixContenant(idContenant, qte);
                System.out.println("Contenant ajouté au panier ! Coût estimé : " + prixEstime);
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'ajout du contenant à la commande." + e.getMessage());
            }
            } else{
            System.out.println("1 : Commander un Produit ");
            System.out.println(" 2 : Recommander un Contenant");
            System.out.println(" 3 : Annuler la commande ");
            System.out.println(" 4 : Finaliser la commande ");
            System.out.println(" 5 : Retour au menu prinicpal");
            System.out.println("Taper le numéro choisi:");
            beginCommande();
        }
    }
    public void finalCommande(){
        System.out.println("C'est un nouveau client ? ");
        System.out.println("Taper true or false : ");
        boolean nvclient = scan.nextBoolean();
        scan.nextLine();
        String emailClient;
        int idClient;

        String[] argsClient = null;

        // avant la transaction = recup les donnees
        if (nvclient){
            idClient = statementcomm.nbClient();
            System.out.println("Entrer l'email du client : ");
            emailClient = scan.nextLine();
            System.out.println("Entrer le Nom du client : ");
            String nom = scan.nextLine();
            System.out.println("Entrer le Prenom du client : ");
            String prenom = scan.nextLine();
            System.out.println("Entrer le numéro de telephone du client : ");
            String numtelephone = scan.nextLine();
            argsClient = new String[]{emailClient, nom, prenom, numtelephone};
            // statementcomm.ajouteNovClient(agrsClient, idClient);
        } else {
            System.out.println("Entrer l'email du client : ");
            emailClient = scan.nextLine();
            idClient = statementcomm.getIdClient(emailClient);
            if (idClient == 0) {
               finalCommande();
            }
        }
        System.out.println("Paiement en ligne ou non : ");
        System.out.println("Taper true or false : ");
        boolean paiementligne = scan.nextBoolean();
        scan.nextLine();

        String ModePaiement = paiementligne ? "En ligne" : "En boutique";        
        
        System.out.println("Commande à livrer ou non ? ");
        System.out.println("Taper true or false : ");
        boolean livraison = scan.nextBoolean();
        scan.nextLine();
        
        String ModeRecuperation;
        String adresse = null;
        double fraisLivraison = 0;
        String dateLivraison = null;
        boolean nouvelleAdresse = false;
        
        if (livraison){
            ModeRecuperation = "Livraison";
            System.out.println("Utiliser une nouvelle adresse de livraison ? (true/false) : ");
            nouvelleAdresse = scan.nextBoolean();
            scan.nextLine();
            
            if (nouvelleAdresse){
               System.out.println("Entrer l'adresse de livraison : ");
               adresse = scan.nextLine();
            //    statementcomm.ajouteNovAdresse(adresse, emailClient);
            } else{
                ArrayList<String> adresseArray = statementcomm.getAdresseClient(idClient);
                if(adresseArray != null && !adresseArray.isEmpty()) {
                    System.out.println("Choisissez l'adresse de livraison (numéro) : ");
                    int numchoisi = scan.nextInt();
                    scan.nextLine();
                    adresse = adresseArray.get(numchoisi - 1);
                } else {
                    System.out.println("Aucune adresse trouvée, veuillez en saisir une : ");
                    adresse = scan.nextLine();
                    nouvelleAdresse = true;
                }
            }
            System.out.println("Entrer les frais de livraison éventuels : ");
            fraisLivraison = scan.nextDouble();
            scan.nextLine();
            System.out.println("Entrer la date de livraison  : ");
            System.out.println("L'écrire en format YYYY-MM-DD : ");
            dateLivraison = scan.nextLine();
            
            // String[] argsLivraison = {dateLivraison,adresse};
            // statementcomm.commandeLivrer(idCommande, fraisLivraison, argsLivraison);
        } else{
            ModeRecuperation = "Retrait en boutique";
            // statementcomm.commandeBoutique(idCommande);
        }

        System.out.println("Validation de la commande en cours...");

        // Debut de la transaction
        try {
            conn.setAutoCommit(false);
            // Je cree le client si nouveau
            if (nvclient && argsClient != null) {
                statementcomm.ajouteNovClient(argsClient, idClient);
            }

            // J'ajoute l'adresse si nouvelle
            if (livraison && nouvelleAdresse && adresse != null) {
                statementcomm.ajouteNovAdresse(adresse, emailClient);
            }

            // Je cree la commande PRINCIPALE (table commande)
            String [] argsCommande = {ModePaiement,ModeRecuperation};
            statementcomm.creeCommande(idCommande, idClient, argsCommande);
            if (livraison) {
            String[] argsLivraison = {dateLivraison, adresse};
            statementcomm.commandeLivrer(idCommande, fraisLivraison, argsLivraison);
            this.prixCommande += fraisLivraison;
            } else {
            statementcomm.commandeBoutique(idCommande);
            }

            // panier produits :
// {numLigne (auto), idCommande, idProduit (changera dans la boucle)}
            int[] argsProd = {0, idCommande, 0}; 
            
            for (ProduitPanier art : panierProduits) {
                argsProd[2] = art.idProduit; // On met l'ID du produit courant

                double prixLigne = statementcomm.ajouteCommandeGlobalP(
                    argsProd, 
                    art.modeConditionnement, 
                    art.quantite, 
                    art.poidsUnitaire
                );
                
                this.prixCommande += prixLigne;
            }

            // --- D. TRAITEMENT DU PANIER CONTENANTS ---
            int[] argsCont = {0, idCommande, 0};

            for (ContenantPanier cont : panierContenants) {
                argsCont[2] = cont.idContenant;

                double prixLigne = statementcomm.ajouteCommandeGlobalC(
                    argsCont, 
                    cont.quantite
                );
                
                this.prixCommande += prixLigne;
            }
                        // Validation de la transaction
            conn.commit();
            System.out.println("Commande finalisée avec succès.");
            System.out.println("Coût total : " + this.prixCommande);
            panierProduits.clear();
            panierContenants.clear();
            this.prixCommande = 0;

        } catch (SQLException e) {
            System.out.println("Erreur lors du démarrage de la transaction : " + e.getMessage());
            try {
            System.out.println(" Annulation de la transaction (Rollback)...");
            conn.rollback(); // le client, l'adresse et la commande ne seront pas créés.
            } catch (SQLException ex) {
            ex.printStackTrace();
            } 
        } finally {
            try {
            conn.setAutoCommit(true); // remise en mode auto-commit
            } catch (SQLException e) {
            e.printStackTrace();
            }
            System.out.println("\nRetour au menu principal...");
            try { Thread.sleep(1000); } catch (InterruptedException ie) {}
            System.err.println("La commande globale vous coutera " + this.prixCommande);
            System.out.println(" 1 : Commander un Produit ");
            System.out.println(" 2 : Commander un Contenant");
            System.out.println(" 3 : Annuler la commande ");
            System.out.println(" 5 : Retour au menu prinicpal");
            System.out.println("Taper le numéro choisi:");
            beginCommande();
        }
    }
    public void annuleCommande(){
        try {
            conn.rollback();
        } catch (SQLException e) {
        }
    }
    
    public void retour(){
        menu.afficherMenu();
    }


    class ContenantPanier {
        int idContenant;
        int quantite;

        public ContenantPanier(int idContenant, int quantite) {
            this.idContenant = idContenant;
            this.quantite = quantite;
        }
    }

    class ProduitPanier {
        int idProduit;
        String modeConditionnement;
        double poidsUnitaire;
        double quantite;

        public ProduitPanier(int idProduit, String modeConditionnement, double poidsUnitaire, double quantite) {
            this.idProduit = idProduit;
            this.modeConditionnement = modeConditionnement;
            this.poidsUnitaire = poidsUnitaire;
            this.quantite = quantite;
        }
    }
}