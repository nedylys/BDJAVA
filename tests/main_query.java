
import java.io.Console;
import java.sql.*;
import java.util.Scanner;


public class main_query {
    public static void main(String args[]){
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n👤Entrez votre identifiant Épicerie-Moul-Lhanout :)  ");
        String user = scanner.nextLine();                
        Console console = System.console();
        String passwd;
        char[] password =console.readPassword("\n🔒Entrez votre mode de passe Épicerie-Moul-Lhanout :) ");
        passwd = new String(password);
        ConnectionBase connect = new ConnectionBase(user,passwd);
        Connection conn = connect.beginConnection();
        MenuPrincipal menu = new MenuPrincipal(conn);
        menu.afficherMenu();

        if (conn != null) {
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

