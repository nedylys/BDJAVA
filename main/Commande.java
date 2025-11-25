package main;

public class Commande{
    private int[] argsCommande; 
    private double qte;

    public Commande(int[] argsCommande,double qte){
        this.argsCommande = argsCommande;
        this.qte = qte;
    }
    public int[] getArgsCommande(){
        return this.argsCommande;
    }
    public double getQte(){
        return this.qte;
    }
    
}