
import java.io.Console;
import java.sql.*;
import java.util.Scanner;


public class main_query {
    public static void main(String args[]){
        ConnectionBase connB = new ConnectionBase();
        Connection conn = connB.beginConnection();


        if (conn != null) {
            MenuPrincipal menu = new MenuPrincipal(conn);
            menu.afficherMenu();
            try {
                conn.close();
                System.out.println("Connexion fermée.");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion !");
                e.printStackTrace();
            }
        } else {
            System.out.println("Connexion échouée !");
        }


    }
     
}

