package main;

public class Commande{
    private int[] argsCommande; 
    private double qte;
    private double prix;

    public Commande(int[] argsCommande,double qte,double prix){
        this.argsCommande = argsCommande;
        this.qte = qte;
        this.prix = prix;
    }
    public int[] getArgsCommande(){
        return this.argsCommande;
    }
    public double getQte(){
        return this.qte;
    }
    public double getPrix(){
        return prix;
    }
    
}