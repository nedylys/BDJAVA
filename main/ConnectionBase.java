import java.sql.*;

public class ConnectionBase{
    
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER ="iknem";
    static final String PASSWD = "iknem";

    private String user;
    private String passwd ;
    private Connection conn;




    public ConnectionBase(String user, String passwd) {
        this.user = user;
        this.passwd = passwd;
    }
    public Connection beginConnection(){
        try {
            if (!user.equals(USER) || !passwd.equals(PASSWD)) {
                System.out.println(" Identifiants incorrects. Accès refusé. Réssayez !");
                return null;
            }
            // Clear le terminal
            try {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            } catch (Exception e) {
                System.out.println("[!] Impossible de clear le terminal");
            }

            System.out.println("\n========================================================");
            System.out.println("                 🛒 Moul-Lhanout Market 🛒              ");
            System.out.println("========================================================");

            // Enregistrement du driver Oracle
            System.out.print("\n    ⏳ Loading Oracle driver...  "); 
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            System.out.println("🗃 Loaded successfully");
            // Connection

            // Etablissement de la connection
            System.out.print("\n    🔁Connecting to the database... "); 
            Connection conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
            System.out.println("🤝 Connected!");

            // Fermeture 
            return conn;
        } 
        catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
            return null;
        }
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

