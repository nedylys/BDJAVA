package main;

import java.sql.*;
import java.util.*;

public class PassCommande{
    
    private Connection conn;
    private StatementCommande statementcomm;
    private MenuPrincipal menu;
    private Scanner scan;
    private int idCommande;
    
    public PassCommande(Connection conn,Scanner scan,MenuPrincipal menu){
        this.conn = conn;
        this.scan = scan;
        this.statementcomm = new StatementCommande(conn);
        this.menu = menu;
        idCommande = statementcomm.nbIdCommade();
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
    public void commandeProduit(){
        System.out.println(" Entrer l'idProduit : ");
        int idProduit = scan.nextInt();
        scan.nextLine();
        if (!(statementcomm.verifieIdProduit(idProduit))){
            System.out.println("L'idProduit est faux");
            commandeProduit();
        }
        System.out.println(" Entrer le Mode de Conditionnement : ");
        String ModeConditionnement = scan.nextLine();
        int PoidsUnitaire;
        if (ModeConditionnement == "En vrac"){
            PoidsUnitaire = 1;
        }else{
            System.out.println(" Entrer le poids unitaire : ");
            PoidsUnitaire = scan.nextInt();
            scan.nextLine();
        }
        System.out.println(" Entrer la quantité souhaitée : ");
        double qte = scan.nextDouble();
        scan.nextLine();
        boolean commande = statementcomm.inCommande(idProduit);
        if (commande){
            System.out.println("Votre produit est sur commande ");
            int delai = statementcomm.getDelaiDispo(idProduit);
            System.out.println("Votre produit sera disponible dans " + delai);
        }else{
            boolean dispo = statementcomm.getDispo(idProduit, qte, ModeConditionnement, PoidsUnitaire);
            if (!dispo){
                commandeProduit();
            }
        }
        System.out.println("Voulez vous commander le produit ?");
        System.out.println("Taper true or false : ");
        boolean creecommande = scan.nextBoolean(); 
        scan.nextLine();
        if (creecommande){
            int numligneP = statementcomm.nbLigneP();
            int[] argsCommande = {numligneP,idCommande,idProduit};
            double prix = statementcomm.ajouteCommandeGlobalP(argsCommande, ModeConditionnement, qte);
            System.out.println("Cette commande de ce produit vous couteta " + prix); 
        }else{
            System.out.println(" 1 : Recommander un Produit ");
            System.out.println(" 2 : Commander un Contenant");
            System.out.println(" 3 : Annuler la commande ");
            System.out.println(" 4 : Finaliser la commande ");
            System.out.println(" 5 : Retour au menu prinicpal");
            System.out.println("Taper le numéro choisi:");
            beginCommande();
        }
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
            int numligneC = statementcomm.nbLigneContenant();
            int[] argsCommandeC = {numligneC,idCommande,idContenant};
            double prix = statementcomm.ajouteCommandeGlobalC(argsCommandeC, qte);
            System.out.println("Cette commande de ce contenat vous couteta " + prix); 
        } else{
            System.out.println(" 1 : Commander un Produit ");
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
            String[] agrsClient = {emailClient,nom,prenom,numtelephone};
            statementcomm.ajouteNovClient(agrsClient, idClient);
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
            System.out.println("Utiliser une nouvelle adresse de livraison ou non ");
            System.out.println("Taper true or false : ");
            boolean adresselivraison = scan.nextBoolean();
            scan.nextLine();
            if (adresselivraison){
               System.out.println("Entrer l'adresse de livraison : ");
               adresse = scan.nextLine();
               statementcomm.ajouteNovAdresse(adresse, emailClient);
            } else{
                ArrayList<String> adresseArray = statementcomm.getAdresseClient(idClient);
                System.out.println("Choisisez l'addresse de livraison : ");
                int numchoisi = scan.nextInt();
                scan.nextLine();
                adresse = adresseArray.get(numchoisi - 1);
            }
            System.out.println("Entrer les frais de livraison  : ");
            double fraisLivraison = scan.nextDouble();
            scan.nextLine();
            System.out.println("Entrer la date de livraison  : ");
            System.out.println("L'écrire en format YYYY-MM-DD : ");
            String dateLivraison = scan.nextLine();
            String[] argsLivraison = {dateLivraison,adresse};
            statementcomm.commandeLivrer(idCommande, fraisLivraison, argsLivraison);
        } else{
            ModeRecuperation = "Retrait en boutique";
            statementcomm.commandeBoutique(idCommande);
        }
        String [] argsCommande = {ModePaiement,ModeRecuperation};
        statementcomm.creeCommande(idCommande, idClient, argsCommande);
        System.out.println(" 1 : Commander un Produit ");
        System.out.println(" 2 : Commander un Contenant");
        System.out.println(" 3 : Annuler la commande ");
        System.out.println(" 5 : Retour au menu prinicpal");
        System.out.println("Taper le numéro choisi:");
        try {
            conn.commit();
        } catch (SQLException e) {
        }
        beginCommande();
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
}