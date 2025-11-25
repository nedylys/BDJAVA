package main;


public class CommandeProduit extends Commande {
    String ModeConditionnement;
    double PoidsUnitaire;

    public CommandeProduit(int[] argsCommande,double qte,String ModeConditionnement,double PoidsUnitaire){
        super(argsCommande,qte);
        this.ModeConditionnement = ModeConditionnement;
        this.PoidsUnitaire = PoidsUnitaire;
    }
    public String getModeConditionnement(){
        return this.ModeConditionnement;
    }
    public double getPoidsUnitaire(){
        return this.PoidsUnitaire;
    }
}