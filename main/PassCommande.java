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
        idCommande = statementcomm.nbIdCommade(); // ou + 1 ??
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
        while (!scan.hasNextInt()) {
            System.out.println("Choix invalide, veuillez réessayer. 🤕");
            scan.next(); 

        }
        int idProduit = scan.nextInt();
        scan.nextLine();
        if (!(statementcomm.verifieIdProduit(idProduit))){
            System.out.println("IdPoduit invalide, veuillez réessayer. 🤕");
            try {
                Thread.sleep(5000); // 1000 ms = 1 seconde
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } 
            commandeProduit();
        }

        System.out.println("Modes de Conditionnement disponibles : ");
        statementcomm.getModeConditionnement(idProduit);
        System.out.println("Entrer le Mode de Conditionnement (v ou p) : ");
        String ModeConditionnement = scan.nextLine();
        double PoidsUnitaire;
        while (!(ModeConditionnement.equals("v")) && !(ModeConditionnement.equals("p"))){
            System.out.println("Mauvaise saisie ");
            System.out.println("Entrer le Mode de Conditionnement : ");
            ModeConditionnement = scan.nextLine();
        }
        if (ModeConditionnement.equals("v")){
            ModeConditionnement = "vrac";
            PoidsUnitaire = 1.0;
        }else{
            ModeConditionnement = "preconditionne";
            System.out.println("Les poidsUnitaire disponibles : ");
            statementcomm.choisirPoidsUnitaire(idProduit);
            System.out.println("Entrer le poids unitaire : ");
            PoidsUnitaire = scan.nextDouble();
            scan.nextLine();
        }
        //
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
                beginCommande();
            }
        }
        System.out.println(" 1 : Confirmer la commande  ");
        System.out.println(" 0 : Revenir en arrière ");
        //System.out.println("Taper le numéro : ");
        int creecommande = scan.nextInt(); 
        scan.nextLine();
        if (creecommande == 1){
            int numligneP = 0;
            int[] argsCommandeP = {numligneP,idCommande,idProduit};
            try{
<<<<<<< HEAD
                // transaction pour une ligne de commande :
                conn.setAutoCommit(false);
                double prixLigne = statementcomm.ajouteCommandeGlobalP(
                    argsCommande,
                    ModeConditionnement,
                    qte,
                    PoidsUnitaire
                );
                conn.commit();
                System.out.println("Produit ajouté au panier ! Coût estimé : " + prixLigne);
                this.prixCommande += prixLigne;
=======
            // !!!!!!!!!!!!!!!!! il n'a pas payé ni fini sa commande
            // double prix = statementcomm.ajouteCommandeGlobalP(argsCommande, ModeConditionnement, qte,PoidsUnitaire);
            // System.out.println("Cette commande de ce produit vous couteta " + prix); 
                panierProduits.add(new ProduitPanier(idProduit, ModeConditionnement, PoidsUnitaire, qte));
                double prixEstime = statementcomm.retournePrixCommandeP(argsCommandeP,ModeConditionnement,qte,PoidsUnitaire);
                System.out.println("Produit ajouté au panier ! Coût estimé : " + prixEstime);
                this.prixCommande += prixEstime;
>>>>>>> 04677a82a07c8b055315c209796efd4623bf1ee6
            } catch (SQLException e) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                System.out.println("Erreur lors de l'ajout du produit à la commande: " + e.getMessage());
            } finally {
                try {
                    conn.setAutoCommit(true); // remise en mode auto-commit
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(" 1 : Recommander un Produit ");
        System.out.println(" 2 : Commander un Contenant");
        System.out.println(" 3 : Annuler la commande ");
        System.out.println(" 4 : Finaliser la commande ");
        System.out.println(" 5 : Retour au menu prinicpal");
        beginCommande();
    }
    public void commandeContenant(){
        System.out.println(" Entrer l'idContenant : ");
        int idContenant = scan.nextInt();
        scan.nextLine();
        if (!(statementcomm.verfieIdContenant(idContenant))){
            System.out.println("L'idContenant est faux");
            commandeProduit();
            return;
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
<<<<<<< HEAD
                conn.setAutoCommit(false);
                double prixLigne = statementcomm.ajouteCommandeGlobalC(
                    argsCommandeC, 
                    qte
                );
                conn.commit(); // COMMIT valide la ligne et la mise à jour du stock
                System.out.println("Contenant ajouté au panier ! Coût estimé : " + prixLigne);
                this.prixCommande += prixLigne;
=======
                panierContenants.add(new ContenantPanier(idContenant, qte));
                double prixEstime = statementcomm.retournePrixCommandeC(argsCommandeC, qte);
                System.out.println("Contenant ajouté au panier ! Coût estimé : " + prixEstime);
                this.prixCommande += prixEstime;
>>>>>>> 04677a82a07c8b055315c209796efd4623bf1ee6
            } catch (SQLException e) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                System.out.println("Erreur lors de l'ajout du contenant à la commande: " + e.getMessage());
            } finally {
                try {
                    conn.setAutoCommit(true); // remise en mode auto-commit
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            } else{
            System.out.println("1 : Commander un Produit ");
            System.out.println(" 2 : Recommander un Contenant");
            System.out.println(" 3 : Annuler la commande ");
            System.out.println(" 4 : Finaliser la commande ");
            System.out.println(" 5 : Retour au menu prinicpal");
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
        
        String ModeRecuperation = "Retrait en boutique"; 
        String adresse = null;
        double fraisLivraison = 0;
        String dateLivraison = null;
        boolean nouvelleAdresse = true;
        
        if (livraison){
            if (!nvclient){ // 
                ModeRecuperation = "Livraison";
                System.out.println("Utiliser une nouvelle adresse de livraison ? (true/false) : ");
                nouvelleAdresse = scan.nextBoolean();
                scan.nextLine();
            }
            
            if (nouvelleAdresse){
               System.out.println("Entrer l'adresse de livraison : ");
               adresse = scan.nextLine();
            //    statementcomm.ajouteNovAdresse(adresse, emailClient);
            } else{
                ArrayList<String> adresseArray = statementcomm.getAdresseClient(emailClient);
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

               statementcomm.ajouteCommandeGlobalP(
                    argsProd, 
                    art.modeConditionnement, 
                    art.quantite, 
                    art.poidsUnitaire
                );
                
            }

            // --- D. TRAITEMENT DU PANIER CONTENANTS ---
            int[] argsCont = {0, idCommande, 0};

            for (ContenantPanier cont : panierContenants) {
                argsCont[2] = cont.idContenant;

              statementcomm.ajouteCommandeGlobalC(
                    argsCont, 
                    cont.quantite
                );
                
            }
                        // Validation de la transaction
            conn.commit();
            System.out.println("Commande finalisée avec succès.");
            System.out.println("Coût total : " + this.prixCommande);
            panierProduits.clear();
            panierContenants.clear();


        } catch (SQLException e) {
            System.out.println("Erreur lors du démarrage de la transaction : " + e.getMessage());
            try {
            System.out.println(" Annulation de la transaction (Rollback)...");
            conn.rollback(); // le client, l'adresse et la commande ne seront pas créés.
            System.out.println(" Annulation des lignes de commande déjà enregistrées...");
            annuleCommande();
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
            double prixCommandeFinal = this.prixCommande; // stocker le prix avant de le réinitialiser 
            this.prixCommande = 0;
            System.err.println("La commande globale vous coutera " + prixCommandeFinal);
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
            conn.setAutoCommit(false);

            statementcomm.supprimerLignesCommande(idCommande);
            statementcomm.supprimerLignesContenant(idCommande); 

            conn.commit();
            System.out.println("Commande annulée. Toutes les lignes ont été supprimées et le stock a été restauré.");
            this.prixCommande = 0;
        } catch (SQLException e) {
             try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("Erreur lors de l'annulation de la commande : " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
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