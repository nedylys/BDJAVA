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
    private ArrayList<Commande> panierCommandeC;
    private ArrayList<CommandeProduit> panierCommandeP;
    private ArrayList<Integer> listP;
    private ArrayList<Integer> listC;
    private ArrayList<CommandeProduit> panierCommandePCommande;
    private ArrayList<Integer> idProduitUtilise;
    private ArrayList<Integer> idContenantUtilise;
    private ArrayList<Savepoint> listSavePoint;

    public PassCommande(Connection conn,Scanner scan,MenuPrincipal menu){
        System.out.println("---------------- ⚠️  ATTENTION ⚠️  ----------------");
        System.out.println("Vous n'avez pas le droit de commander deux fois le même produit dans la même commande globale");
        System.out.println("Si vous voulez changer la quantité, allez dans l'onglet Changer la Quantité d'une Commande");
        System.out.println("---------------- ⚠️  ATTENTION ⚠️  ----------------");
        System.out.println("         ");
        System.out.println("--------------- ⚡ ATTENTION ⚡ ---------------");
        System.out.println("En raison de la concurrence, certains produits peuvent être temporairement verrouillés lorsque vous essayez de finaliser la commande.");
        System.out.println("Veuillez réessayer si nécessaire.");
        System.out.println("--------------- ⚡ ATTENTION ⚡ ---------------");
        this.conn = conn;
        this.scan = scan;
        this.statementcomm = new StatementCommande(conn,scan);
        this.menu = menu;
        this.prixCommande = 0;
        this.panierCommandeC = new ArrayList<Commande>();
        this.panierCommandeP = new ArrayList<CommandeProduit>();
        this.listP = new ArrayList<Integer>();
        this.listC = new ArrayList<Integer>();
        this.listSavePoint = new ArrayList<Savepoint>();
        this.panierCommandePCommande = new ArrayList<CommandeProduit>();
        this.idProduitUtilise = new ArrayList<Integer>();
        this.idContenantUtilise = new ArrayList<Integer>();
        System.out.println(" 1 : Commander un Produit ");
        System.out.println(" 2 : Commander un Contenant");
        System.out.println(" 3 : Annuler la commande ");
        System.out.println(" 4 : Finaliser la commande ");
        System.out.println(" 6 : Changer la Quantité d'une Commande");
        System.out.println(" 7 : Retour au menu prinicipal");
        System.out.println("Taper le numéro choisi:");
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
        }else if (numchoisi == 5){
            viderPanier();
        } else if (numchoisi == 6) {
            changeQtePC();
        }else{
            retour();
        }
    }
    public void enregistrerClient(){

    }
    public void commandeProduit(){
        System.out.println("Entrer l'idProduit : ");
        int idProduit = scan.nextInt();
        scan.nextLine();
        if (idProduitUtilise.contains(idProduit)){
            System.out.println("Vous avez déja commmandé ce produit");
            System.out.println("Vous pouvez changer sa quantité en appuyant sur 6");
            System.out.println(" 1 : Recommander un Produit ");
            System.out.println(" 2 : Commander un Contenant");
            System.out.println(" 3 : Annuler la commande ");
            System.out.println(" 4 : Finaliser la commande ");
            System.out.println(" 5 : Supprimer des produits du panier");
            System.out.println(" 6 : Changer la Quantité d'une Commande");
            System.out.println(" 7 : Retour au menu prinicipal");
            System.out.println("Taper le numéro choisi:");
            beginCommande();
        }
        if (!(statementcomm.verifieIdProduit(idProduit))){
            commandeProduit();
        }
        idProduitUtilise.add(idProduit);
        boolean dispoSaison = statementcomm.verifieDispoSaison(idProduit);
        if (!dispoSaison){
                System.out.println(" 1 : Commander un Produit ");
                System.out.println(" 2 : Commander un Contenant");
                System.out.println(" 3 : Annuler la commande ");
                System.out.println(" 4 : Finaliser la commande ");
                System.out.println(" 5 : Modifier le panier ");
                System.out.println(" 6 : Changer la Quantité d'une Commande");
                System.out.println(" 7 : Retour au menu prinicipal");
                System.out.println("Taper le numéro choisi:");
                beginCommande();
            }
        System.out.println("Modes de Conditionnement disponibles : ");
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
            System.out.println("Entrer le poids unitaire :");
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
            System.out.println("Votre produit sera disponible dans " + delai + " heures");
        }else{
            boolean dispo = statementcomm.getDispo(idProduit, qte, ModeConditionnement, PoidsUnitaire);
            if (!dispo){
                System.out.println(" 1 : Commander un Produit ");
                System.out.println(" 2 : Commander un Contenant");
                System.out.println(" 3 : Annuler la commande ");
                System.out.println(" 4 : Finaliser la commande ");
                System.out.println(" 5 : Modifier le panier ");
                System.out.println(" 6 : Changer la Quantité d'une Commande");
                System.out.println(" 7 : Retour au menu prinicipal");
                System.out.println("Taper le numéro choisi:");
                beginCommande();
            }
        }
        System.out.println("Voulez vous commander le produit ?");
        System.out.println("Taper true or false : ");
        boolean creecommande = scan.nextBoolean(); 
        scan.nextLine();
        listP.add(idProduit);
        //statementcomm.lockP(idProduit);
        if (creecommande && !(commande)){
            int numligneP = -1;
            int[] argsCommande = {numligneP,idCommande,idProduit};
            double prix = statementcomm.retournePrixCommandeP(argsCommande, ModeConditionnement, qte,PoidsUnitaire);
            System.out.println("Cette commande de ce produit vous coutera " + prix); 
            CommandeProduit commandeP = new CommandeProduit(argsCommande, qte,prix,ModeConditionnement,PoidsUnitaire);
            panierCommandeP.add(commandeP);
        } else if (creecommande && commande){
            int numligneP = -1;
            int[] argsCommande = {numligneP,idCommande,idProduit};
            double prix = statementcomm.retourneprixGlobalCommandeP(argsCommande, ModeConditionnement, qte,PoidsUnitaire);
            System.out.println("Cette commande de ce produit vous coutera " + prix); 
            CommandeProduit commandeP = new CommandeProduit(argsCommande, qte,prix,ModeConditionnement,PoidsUnitaire);
            panierCommandePCommande.add(commandeP);
        }
        //try{
            //Savepoint sp = conn.setSavepoint();
            //listSavePoint.add(sp);
        //} catch (SQLException e) {
            //System.err.println("failed");
            //e.printStackTrace(System.err);
      //}
        System.out.println(" 1 : Recommander un Produit ");
        System.out.println(" 2 : Commander un Contenant");
        System.out.println(" 3 : Annuler la commande ");
        System.out.println(" 4 : Finaliser la commande ");
        System.out.println(" 5 : Modifier le panier");
        System.out.println(" 6 : Changer la Quantité d'une Commande");
        System.out.println(" 7 : Retour au menu prinicipal");
        System.out.println("Taper le numéro choisi:");
        beginCommande();
    }
    public void commandeContenant(){
        System.out.println("Entrer l'idContenant : ");
        int idContenant = scan.nextInt();
        scan.nextLine();
        if (idContenantUtilise.contains(idContenant)){
            System.out.println("Vous avez déja commmandé ce contenant");
            System.out.println("Vous pouvez changer sa quantité en appuyant sur 6");
            System.out.println(" 1 : Recommander un Produit ");
            System.out.println(" 2 : Commander un Contenant");
            System.out.println(" 3 : Annuler la commande ");
            System.out.println(" 4 : Finaliser la commande ");
            System.out.println(" 5 : Supprimer des produits du panier");
            System.out.println(" 6 : Modifier la Quantité d'une Commande");
            System.out.println(" 7 : Retour au menu prinicipal");
            System.out.println("Taper le numéro choisi:");
            beginCommande();

        }
        if (!(statementcomm.verfieIdContenant(idContenant))){
            System.out.println("L'idContenant est faux");
            commandeContenant();
        }
        idContenantUtilise.add(idContenant);
        System.out.println("Entrer la quantité souhaitée : ");
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
        listC.add(idContenant);
    
        if (creecommande){
            int numligneC = -1;
            int[] argsCommandeC = {numligneC,idCommande,idContenant};
            double prix = statementcomm.retournePrixCommandeC(argsCommandeC, qte);
            System.out.println("Cette commande de ce contenant vous coutera " + prix); 
            Commande commandeC = new Commande(argsCommandeC, (double) qte,prix);
            panierCommandeC.add(commandeC);
        }
        System.out.println(" 1 : Commander un Produit ");
        System.out.println(" 2 : Recommander un Contenant");
        System.out.println(" 3 : Annuler la commande ");
        System.out.println(" 4 : Finaliser la commande ");
        System.out.println(" 5 : Supprimer des produits du panier");
        System.out.println(" 6 : Changer la Quantité d'une Commande");
        System.out.println(" 7 : Retour au menu prinicipal");
        System.out.println("Taper le numéro choisi:");
        beginCommande();
    }
    public void finalCommande(){
        System.out.println("Entrer l'email du client : ");
        String emailClient = scan.nextLine();
        boolean nvclient = !(statementcomm.verifieEmailExist(emailClient));
        int idClient;
        if (nvclient){
            System.out.println("C'est un nouveau Client");
            idClient = statementcomm.nbClient();
            System.out.println("Entrer le Nom du client : ");
            String nom = scan.nextLine();
            System.out.println("Entrer le Prenom du client : ");
            String prenom = scan.nextLine();
            System.out.println("Entrer le numéro de telephone du client : ");
            String numtelephone = scan.nextLine();
            String[] agrsClient = {emailClient,nom,prenom,numtelephone};
            statementcomm.ajouteNovClient(agrsClient, idClient);
        } else {
            System.out.println("Ce client existe déja dans la base");
            idClient = statementcomm.getIdClient(emailClient);
        }
        try {
            conn.commit();
            }catch(SQLException e) {
                System.err.println("failed");
                e.printStackTrace(System.err);
            }
        System.out.println("Paiement en ligne ou non : ");
        System.out.println("Taper true or false : ");
        boolean paiementligne = scan.nextBoolean();
        scan.nextLine();
        String ModePaiement;
        if (paiementligne){
            ModePaiement = "En ligne";
        }else{
            ModePaiement = "En boutique";
        }
        System.out.println("Commande à livrer ou non ? ");
        System.out.println("Taper true or false : ");
        boolean livraison = scan.nextBoolean();
        scan.nextLine();
        String ModeRecuperation; 
        String adresse;
        if (livraison){
            ModeRecuperation = "Livraison";
            boolean adresselivraison;
            if (!nvclient){
                System.out.println("Utiliser une nouvelle adresse de livraison ou non ");
                System.out.println("Taper true or false : ");
                adresselivraison = scan.nextBoolean();
                scan.nextLine();
            } else {
                adresselivraison = true;
            }
            if (adresselivraison){
               System.out.println("Entrer l'adresse de livraison : ");
               adresse = scan.nextLine();
               statementcomm.ajouteNovAdresseClient(adresse, emailClient);
            } else{
                ArrayList<String> adresseArray = statementcomm.getAdresseClient(emailClient);
                System.out.println("Choisisez l'addresse de livraison : ");
                int numchoisi = scan.nextInt();
                scan.nextLine();
                adresse = adresseArray.get(numchoisi - 1);
            }
            System.out.println("Entrer les frais de livraison éventuels : ");
            double fraisLivraison = scan.nextDouble();
            scan.nextLine();
            System.out.println("Entrer la date de livraison  : ");
            System.out.println("L'écrire en format YYYY-MM-DD : ");
            String dateLivraison = scan.nextLine();
            String[] argsLivraison = {dateLivraison,adresse};
            String [] argsCommande = {ModePaiement,ModeRecuperation};
            idCommande = statementcomm.nbIdCommande();
            statementcomm.creeCommande(idCommande, idClient, argsCommande);
            statementcomm.commandeLivrer(idCommande, fraisLivraison, argsLivraison);
        } else{
            ModeRecuperation = "Retrait";
            String [] argsCommande = {ModePaiement,ModeRecuperation};
            idCommande = statementcomm.nbIdCommande();
            statementcomm.creeCommande(idCommande, idClient, argsCommande);
            statementcomm.commandeBoutique(idCommande);
        }
        try {
                conn.commit();
            }catch(SQLException e) {
                System.err.println("failed");
                e.printStackTrace(System.err);
            }
        int nbLigneP = 0;
        int nbLigneC = 0;
        for (CommandeProduit commandeP : panierCommandeP){
             int[] argsCommandeP = commandeP.getArgsCommande();
             argsCommandeP[0] += nbLigneP;
             argsCommandeP[1] = idCommande;
             double prixP = commandeP.getPrix();
             double qteP = commandeP.getQte();
             String ModedeConditionnement = commandeP.getModeConditionnement();
             double PoidsUnitaire = commandeP.getPoidsUnitaire();
             boolean islockedP = statementcomm.lockP(argsCommandeP[2],ModedeConditionnement,PoidsUnitaire);
             if (islockedP){
                statementcomm.ajouteCommandeGlobalP(argsCommandeP, ModedeConditionnement, qteP, PoidsUnitaire);
                nbLigneP += argsCommandeP[0] + 1;
                this.prixCommande += prixP;
             }
        }
        for (CommandeProduit commandeP : panierCommandePCommande){
             int[] argsCommandeP = commandeP.getArgsCommande();
             argsCommandeP[0] += nbLigneP;
             argsCommandeP[1] = idCommande;
             double prixP = commandeP.getPrix();
             this.prixCommande += prixP;
             double qteP = commandeP.getQte();
             String ModedeConditionnement = commandeP.getModeConditionnement();
             double PoidsUnitaire = commandeP.getPoidsUnitaire();
             statementcomm.ajouteCommandeGlobalCommandeP(argsCommandeP, ModedeConditionnement, qteP, PoidsUnitaire);
             nbLigneP += argsCommandeP[0] + 1;
        }
        for (Commande commandeC : panierCommandeC){
             int[] argsCommandeC = commandeC.getArgsCommande();
             argsCommandeC[0] += nbLigneC;
             argsCommandeC[1] = idCommande;
             double prixC = commandeC.getPrix();
             double qteC = commandeC.getQte();
             boolean islockedC = statementcomm.lockC(argsCommandeC[2]);
             if (islockedC){
                statementcomm.ajouteCommandeGlobalC(argsCommandeC,(int) qteC);
                nbLigneC += argsCommandeC[0] + 1; 
                this.prixCommande+= prixC; 
             }
        }
        panierCommandeP.clear();
        panierCommandePCommande.clear();
        panierCommandeC.clear();
        System.out.println("La création de la commande a été réussie ! ");
        System.out.println("La commande globale vous coutera " + this.prixCommande);
        System.out.println(" 1 : Commander un Produit ");
        System.out.println(" 2 : Commander un Contenant");
        System.out.println(" 7 : Retour au menu prinicipal");
        System.out.println("Taper le numéro choisi:");
        System.out.println("La commande a bien été créee");
        beginCommande();
    }
    public void annuleCommande(){
        panierCommandeP.clear();
        panierCommandeC.clear();
        panierCommandePCommande.clear();
        System.out.println("La commande a bien été annulée !");
        System.out.println(" 1 : Commander un Produit ");
        System.out.println(" 2 : Commander un Contenant");
        System.out.println(" 3 : Annuler la commande ");
        System.out.println(" 4 : Finaliser la commande ");
        System.out.println(" 5 : Supprimer des produits du panier");
        System.out.println(" 6 : Changer la Quantité d'une Commande");
        System.out.println(" 7 : Retour au menu prinicipal");
        System.out.println("Taper le numéro choisi:");
        beginCommande();
    }
    public void viderPanier(){
        System.out.println("Voulez vous modifier le panier Produit");
        System.out.println("Tapez 0(non) ou 1(oui) ");
        int choixPanierP = scan.nextInt();
        scan.nextLine();
        if (choixPanierP == 1){
            int ip = 0;
            for (CommandeProduit p : panierCommandeP){
                ip++;
                int[] argsPanierP = p.getArgsCommande();
                String ModeConditionnement = p.getModeConditionnement();
                double poidsUnitaire = p.getPoidsUnitaire();
                System.out.println(ip+". "+ "idProduit = " + argsPanierP[2] +
                    " ModeConditionnement = " + ModeConditionnement + " poidsUnitaire = " +
                    poidsUnitaire
                );
            }
            for (CommandeProduit p : panierCommandePCommande){
                ip++;
                int[] argsPanierP = p.getArgsCommande();
                String ModeConditionnement = p.getModeConditionnement();
                double poidsUnitaire = p.getPoidsUnitaire();
                System.out.println(ip+". "+ "idProduit = " + argsPanierP[2] +
                    " ModeConditionnement = " + ModeConditionnement + " poidsUnitaire = " +
                    poidsUnitaire
                );
            }
            System.out.println("Combien de produits voulez vous supprimer ?");
            int nP = scan.nextInt();
            System.out.println("Taper successivement le numéro des lignes que vous voulez enlever du panier");
            for (int j = 0; j<nP;j++){
                int iAjeter = scan.nextInt();
                scan.nextLine();
                if (iAjeter > panierCommandeP.size()){
                   panierCommandePCommande.remove(iAjeter - panierCommandeP.size() - 1);
                } else{
                        panierCommandeP.remove(iAjeter-1);
                }
            }
        }
        System.out.println("   ");
        System.out.println("Voulez vous modifier le panier Contenant");
        System.out.println("Tapez 0(non) ou 1(oui) ");
        int choixPanierC = scan.nextInt();
        scan.nextLine();
        if (choixPanierC == 1){
            int iC = 0;
            for (Commande c : panierCommandeC){
                iC++;
                int[] argsPanierC = c.getArgsCommande();
                System.out.println(iC+". "+ "idContenant = " + argsPanierC[2] 
                );
            }
            System.out.println("Combien de Contenants voulez vous supprimer ?");
            int nC = scan.nextInt();
            System.out.println("Taper successivement le numéro des lignes que vous voulez enlever du panier");
            for (int j = 0; j<nC;j++){
                int iAjeterC = scan.nextInt();
                scan.nextLine();
                panierCommandeC.remove(iAjeterC-1);
            }
        }
        System.out.println(" 1 : Commander un Produit ");
        System.out.println(" 2 : Commander un Contenant");
        System.out.println(" 3 : Annuler la commande ");
        System.out.println(" 4 : Finaliser la commande ");
        System.out.println(" 5 : Modifier le panier");
        System.out.println(" 6 : Changer la Quantité d'une Commande");
        System.out.println(" 7 : Retour au menu prinicipal");
        System.out.println("Taper le numéro choisi:");
        beginCommande();
    }
    public void changeQtePC(){
       System.out.println("Voulez vous changer la quatité d'un produit ?");
       System.out.println("Tapez 0(non) ou 1(oui) ");
        int choixP = scan.nextInt();
        scan.nextLine();
        if (choixP == 1){
            int ip = 0;
            for (CommandeProduit p : panierCommandeP){
                ip++;
                int[] argsPanierP = p.getArgsCommande();
                String ModeConditionnement = p.getModeConditionnement();
                double poidsUnitaire = p.getPoidsUnitaire();
                System.out.println(ip+". "+ "idProduit = " + argsPanierP[2] +
                    " ModeConditionnement = " + ModeConditionnement + " poidsUnitaire = " +
                    poidsUnitaire
                );
            }
            for (CommandeProduit p : panierCommandePCommande){
                ip++;
                int[] argsPanierP = p.getArgsCommande();
                String ModeConditionnement = p.getModeConditionnement();
                double poidsUnitaire = p.getPoidsUnitaire();
                System.out.println(ip+". "+ "idProduit = " + argsPanierP[2] +
                    " ModeConditionnement = " + ModeConditionnement + " poidsUnitaire = " +
                    poidsUnitaire
                );
            }
            System.out.println("Combien de commandes Produit voulez vous changer ?");
            int nP = scan.nextInt();
            System.out.println("Tapez successivement le numéro de la ligne que vous voulez changer avec la nouvelle quantité");
            for (int j = 0; j<nP;j++){
                System.out.println("Entrer le numéro de la ligne : ");
                int iAchanger = scan.nextInt();
                scan.nextLine();
                System.out.println("Entrer la nouvelle quantité : ");
                double nvQte = scan.nextDouble();
                scan.nextLine();
                if (iAchanger > panierCommandeP.size()){
                   CommandeProduit commandeP = panierCommandePCommande.get(iAchanger - 1 - panierCommandeP.size());
                   panierCommandePCommande.remove(iAchanger - panierCommandeP.size() - 1);
                   int[] agrsCommandeP = commandeP.getArgsCommande();
                   String ModeConditionnement = commandeP.getModeConditionnement(); 
                   double PoidsUnitaire = commandeP.getPoidsUnitaire();
                   double nwprix = statementcomm.retourneprixGlobalCommandeP(agrsCommandeP, ModeConditionnement, nvQte, PoidsUnitaire);
                   CommandeProduit nwCommandeP = new CommandeProduit(agrsCommandeP, nvQte,nwprix,ModeConditionnement, PoidsUnitaire);
                   panierCommandePCommande.add(iAchanger - panierCommandeP.size() - 1,nwCommandeP);
                   System.out.println("Maintenant cette commande vous coutera " + nwprix);
                } else{
                    CommandeProduit commandeP = panierCommandeP.get(iAchanger - 1);
                    int[] agrsCommandeP = commandeP.getArgsCommande();
                    int idProduit = agrsCommandeP[2];
                    String ModeConditionnement = commandeP.getModeConditionnement(); 
                    double PoidsUnitaire = commandeP.getPoidsUnitaire();
                    boolean dispo = statementcomm.getDispo(idProduit, nvQte, ModeConditionnement, PoidsUnitaire);
                    while (!dispo){
                        System.out.println("╔══════════════════════════════════════╗");
                        System.out.println("║ ⚠️  ATTENTION                         ║");
                        System.out.println("╠══════════════════════════════════════╣");
                        System.out.println("║ Hehe... Vous pensiez qu'on n'allait  ║");
                        System.out.println("║ pas revérifier la quantité ?         ║");
                        System.out.println("║                                      ║");
                        System.out.println("║ Merci d'indiquer une quantité valide ║");
                        System.out.println("║ dans la limite du stock disponible   ║");
                        System.out.println("║ ou de supprimer ce produit.          ║");
                        System.out.println("╚══════════════════════════════════════╝");
                        System.out.println("  ");
                        System.out.println(" 1 : Changer la quantité ");
                        System.out.println(" 2 : Supprimer le produit du panier");
                        int choixQte = scan.nextInt();
                        scan.nextLine();
                        if (choixQte == 1){
                            System.out.println("Entrer la nouvelle quantité : ");
                            nvQte = scan.nextDouble();
                            scan.nextLine();
                            dispo = statementcomm.getDispo(idProduit, nvQte, ModeConditionnement, PoidsUnitaire);
                        } else{
                            dispo = true;
                            panierCommandeP.remove(iAchanger - 1);
                        }
                    } 
                    if (panierCommandeP.contains(commandeP)){
                        System.out.println("Nouvelle quantité validée !");
                        panierCommandeP.remove(iAchanger - 1);
                        double nwprix = statementcomm.retournePrixCommandeP(agrsCommandeP, ModeConditionnement, nvQte, PoidsUnitaire);
                        CommandeProduit nwCommandeP = new CommandeProduit(agrsCommandeP, nvQte, nwprix,ModeConditionnement, PoidsUnitaire);
                        panierCommandeP.add(iAchanger-1,nwCommandeP);
                        System.out.println("Maintenant cette commande vous coutera " + nwprix);
                    }
                }
            }
        }
        System.out.println("Voulez vous changer la quatité d'un contenant ?");
        System.out.println("Tapez 0(non) ou 1(oui) ");
        int choixC = scan.nextInt();
        scan.nextLine();
        if (choixC == 1){
            int ic = 0;
            for (Commande c : panierCommandeC){
                ic++;
                int[] argsPanierC = c.getArgsCommande();
                System.out.println(ic+". "+ "idContenant = " + argsPanierC[2] );
            }
            System.out.println("Combien de commandes Contenant voulez vous changer ?");
            int nC = scan.nextInt();
            System.out.println("Tapez successivement le numéro de la ligne que vous voulez changer avec la nouvelle quantité");
            for (int j = 0; j<nC;j++){
                System.out.println("Entrer le numéro de la ligne : ");
                int iAchangerC = scan.nextInt();
                scan.nextLine();
                System.out.println("Entrer la nouvelle quantité : ");
                int nvQteC = scan.nextInt();
                scan.nextLine(); 
                Commande commandeC = panierCommandeC.get(iAchangerC - 1);
                int[] agrsCommandeC = commandeC.getArgsCommande();
                int idContenant = agrsCommandeC[2];
                boolean dispo = statementcomm.getDispoContenant(idContenant, nvQteC);
                while (!dispo){
                        System.out.println("╔══════════════════════════════════════╗");
                        System.out.println("║ ⚠️  ATTENTION                         ║");
                        System.out.println("╠══════════════════════════════════════╣");
                        System.out.println("║ Hehe... Vous pensiez qu'on n'allait  ║");
                        System.out.println("║ pas revérifier la quantité ?         ║");
                        System.out.println("║                                      ║");
                        System.out.println("║ Merci d'indiquer une quantité valide ║");
                        System.out.println("║ dans la limite du stock disponible   ║");
                        System.out.println("║ ou de supprimer ce contenant.        ║");
                        System.out.println("╚══════════════════════════════════════╝");
                        System.out.println("  ");
                        System.out.println(" 1 : Changer la quantité ");
                        System.out.println(" 2 : Supprimer le produit du panier");
                        int choixQteC = scan.nextInt();
                        scan.nextLine();
                        if (choixQteC == 1){
                            nvQteC = scan.nextInt();
                            scan.nextLine();
                            dispo = statementcomm.getDispoContenant(idContenant, nvQteC);
                        } else{
                            dispo = true;
                            panierCommandeC.remove(iAchangerC - 1);
                        }
                    } 
                    if (panierCommandeC.contains(commandeC)){
                        System.out.println("Nouvelle quantité validée !");
                        panierCommandeC.remove(iAchangerC - 1);
                        double nwprixC = statementcomm.retournePrixCommandeC(agrsCommandeC, nvQteC);
                        System.out.println("Maintenant cette commande vous coutera " + nwprixC);
                        Commande nwCommandeC = new Commande(agrsCommandeC, nvQteC,nwprixC);
                        panierCommandeC.add(iAchangerC -1,nwCommandeC);
                    } 
            }      
    
        }
        System.out.println(" 1 : Commander un Produit ");
        System.out.println(" 2 : Commander un Contenant");
        System.out.println(" 3 : Annuler la commande ");
        System.out.println(" 4 : Finaliser la commande ");
        System.out.println(" 5 : Supprimer des produits du panier");
        System.out.println(" 6 : Modifier la quantité d'une commande");
        System.out.println(" 7 : Retour au menu prinicipal");
        System.out.println("Taper le numéro choisi:");
        beginCommande();
    }
    
    public void retour(){
        menu.afficherMenu();
    }

    private void afficherResumePanier() {
        System.out.println("--- PANIER ACTUEL ---");
        System.out.println(panierCommandeP.size() + " Produits | " + panierCommandeC.size() + " Contenants");
        System.out.println("Total estimé : " + String.format("%.2f", prixCommande) );
        System.out.println("---------------------");
    }
}