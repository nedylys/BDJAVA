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
            System.out.print("\n🏪 Bonjour Moul-Lhanout "); 

            // Enregistrement du driver Oracle
            System.out.print("Loading Oracle driver... "); 
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            System.out.println("loaded");
            // Connection

            // Etablissement de la connection
            System.out.print("Connecting to the database... "); 
            Connection conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
            System.out.println("connected");

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

