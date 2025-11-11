import java.sql.*;
import java.util.*;
public class PassCommande{
    
    private Connection conn;
    
    private Scanner scan;
    
    public PassCommande(Connection conn,Scanner scan){
        this.conn = conn;
        this.scan = scan;
    }
    public void beginCommande(){
        System.out.println(" 1 : Commander un Produit ");
        System.out.println(" 2 : Commander un Contenant");
        System.out.println(" 3 : Annuler la commande ");
        System.out.println(" 4 : Finaliser la commande ");
        System.out.println(" 5 : Retour au menu prinicpal");
        System.out.println("Taper le numéro choisi:");
        int numchoisi = scan.nextInt();
    }
}