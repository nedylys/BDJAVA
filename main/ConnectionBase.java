package main;

import java.io.Console;
import java.sql.*;
import java.util.Scanner;

public class ConnectionBase{
    
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER ="haninih";
    static final String PASSWD = "haninih";

    private String user;
    private String passwd ;
    private Connection conn;


    public void checkConnection(){
            Scanner scanner = new Scanner(System.in);
            System.out.println("\n👤Entrez votre identifiant Épicerie-Moul-Lhanout :)  ");
            this.user = scanner.nextLine();                
            Console console = System.console();
            String passwd;
            char[] password =console.readPassword("\n🔒Entrez votre mode de passe Épicerie-Moul-Lhanout :) ");
            this.passwd = new String(password);
    }
    public Connection beginConnection() {
        int tries = 0;
        while (tries < 3) {
            checkConnection();
            if (user.equals(USER) && passwd.equals(PASSWD)) {
                try {
                    ProcessBuilder builder = new ProcessBuilder();
                    new ProcessBuilder("clear").inheritIO().start().waitFor(); // voir documentation java Class ProcessBuilder
                } catch (Exception e) {
                    System.out.println("[!] Impossible de clear le terminal");
                }

                System.out.println("\n========================================================");
                System.out.println("                 🛒 Moul-Lhanout Market 🛒              ");
                System.out.println("========================================================");

                try {
                    System.out.print("\n    ⏳ Loading Oracle driver...  ");
                    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                    System.out.println("🗃 Loaded successfully");

                    System.out.print("\n    🔁 Connecting to the database... ");
                    conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
                    System.out.println("🤝 Connected!");
                    return conn;
                } catch (SQLException e) {
                    System.err.println("❌ Échec de la connexion SQL");
                    e.printStackTrace(System.err);
                    return null;
                }
            } else {
                int k = 2-tries;
                System.out.print(" \nIdentifiants incorrects. Accès refusé. Réessayez !");
                if(! (k ==0) ){
                    System.out.print("(Tentatives restantes : "+k+")✖");
                }
                tries++;
            }
        }

        System.out.println("⛔ Accès temporairement bloqué. Réessayez plus tard (~30 min) !");
        return null;
    }
    public void close(){
        try{
            if(conn != null  && !conn.isClosed()){
                conn.close();   
                System.out.println(" Connexion fermée. ");
            }
        }
        catch (SQLException e) {
            System.err.println("failed to close ");
            e.printStackTrace(System.err);
        }
    }

}

