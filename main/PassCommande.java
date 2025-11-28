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

    public PassCommande(Connection conn,Scanner scan,MenuPrincipal menu){
        this.conn = conn;
        this.scan = scan;
        this.statementcomm = new StatementCommande(conn);
        this.menu = menu;
        prixCommande = 0;
        idCommande = statementcomm.nbIdCommade();
        this.panierCommandeC = new ArrayList<Commande>();
        this.panierCommandeP = new ArrayList<CommandeProduit>();
        System.out.println(" 1 : Commander un Produit ");
        System.out.println(" 2 : Commander un Contenant");
        System.out.println(" 3 : Annuler la commande ");
        System.out.println(" 4 : Finaliser la commande ");
        System.out.println(" 5 : Retour au menu prinicpal");
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
        } else {
            retour();
        }
    }
    public void enregistrerClient(){

    }
    public void commandeProduit(){
        System.out.println("Entrer l'idProduit : ");
        int idProduit = scan.nextInt();
        scan.nextLine();
        if (!(statementcomm.verifieIdProduit(idProduit))){
            commandeProduit();
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
            System.out.println("Entrer le poids unitaire");
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
            double prix = statementcomm.retournePrixCommandeP(argsCommande, ModeConditionnement, qte,PoidsUnitaire);
            this.prixCommande += prix;
            System.out.println("Cette commande de ce produit vous couteta " + prix); 
            CommandeProduit commandeP = new CommandeProduit(argsCommande, qte,ModeConditionnement,PoidsUnitaire);
            panierCommandeP.add(commandeP);
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
        System.out.println("Entrer l'idContenant : ");
        int idContenant = scan.nextInt();
        scan.nextLine();
        if (!(statementcomm.verfieIdContenant(idContenant))){
            System.out.println("L'idContenant est faux");
            commandeProduit();
        }
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
        if (creecommande){
            int numligneC = 0;
            int[] argsCommandeC = {numligneC,idCommande,idContenant};
            double prix = statementcomm.retournePrixCommandeC(argsCommandeC, qte);
            this.prixCommande += prix;
            System.out.println("Cette commande de ce contenant vous couteta " + prix); 
            Commande commandeC = new Commande(argsCommandeC, (double) qte);
            panierCommandeC.add(commandeC);
        }
        System.out.println(" 1 : Commander un Produit ");
        System.out.println(" 2 : Recommander un Contenant");
        System.out.println(" 3 : Annuler la commande ");
        System.out.println(" 4 : Finaliser la commande ");
        System.out.println(" 5 : Retour au menu prinicpal");
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
            if (idClient == 0) {
               finalCommande();
            }
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
        String ModeRecuperation;// afin d'initiliser argsCommande
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
            statementcomm.creeCommande(idCommande, idClient, argsCommande);
            statementcomm.commandeLivrer(idCommande, fraisLivraison, argsLivraison);
        } else{
            ModeRecuperation = "Retrait en boutique";
            String [] argsCommande = {ModePaiement,ModeRecuperation};
            statementcomm.creeCommande(idCommande, idClient, argsCommande);
            statementcomm.commandeBoutique(idCommande);
        }
        for (CommandeProduit commandeP : panierCommandeP){
             int[] argsCommandeP = commandeP.getArgsCommande();
             double qteP = commandeP.getQte();
             String ModedeConditionnement = commandeP.getModeConditionnement();
             double PoidsUnitaire = commandeP.getPoidsUnitaire();
             statementcomm.ajouteCommandeGlobalP(argsCommandeP, ModedeConditionnement, qteP, PoidsUnitaire);
        }
        for (Commande commandeC : panierCommandeC){
             int[] argsCommandeC = commandeC.getArgsCommande();
             double qteC = commandeC.getQte();
             statementcomm.ajouteCommandeGlobalC(argsCommandeC,(int) qteC);
        }
        System.out.println("La commande globale vous coutera " + this.prixCommande);
        System.out.println(" 1 : Commander un Produit ");
        System.out.println(" 2 : Commander un Contenant");
        System.out.println(" 3 : Annuler la commande ");
        System.out.println(" 5 : Retour au menu prinicpal");
        System.out.println("Taper le numéro choisi:");
        try {
            conn.commit();
            System.out.println("La commande a bien été crée");
        } catch (SQLException e) {
        }
        beginCommande();
    }
    public void annuleCommande(){
        try {
            conn.rollback();
            System.out.println("La commande a bien été annulée");
        } catch (SQLException e) {
        }
    }
    public void retour(){
        menu.afficherMenu();
    }
}