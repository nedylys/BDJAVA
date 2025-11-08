import java.sql.*;

public class ConnectionBase{
    
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    private static  String user;
    private static String passwd;
    private Connection conn;

    public ConnectionBase(String user, String passwd) {
        this.user = user;
        this.passwd = passwd;
    }
    public Connection getConnection(){
        return conn;
    }
    public Connection beginConnection(){
        try {
	    // Enregistrement du driver Oracle
	    System.out.print("Loading Oracle driver... "); 
	    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        System.out.println("loaded");
        // Connection

	    // Etablissement de la connection
	    System.out.print("Connecting to the database... "); 
	    conn = DriverManager.getConnection(CONN_URL, user, passwd);
        System.out.println("connected");
        System.out.println("Fonctionnalités : ");
        System.out.println("1 : Passage d une commande par un client");
        System.out.println("2 : Alertes de peremption et ajustement des prix");
        System.out.println("3 : Cloture d une commande (retrait ou livraison");
        return conn;
	    // Fermeture 
        } catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
            return null;
        }
    }
    public void close(){
    try {
        if (conn != null && !conn.isClosed()) {
            conn.close();
            System.out.println("Connection closed.");
        }
    } catch (SQLException e) {
        System.err.println("Failed to close connection.");
        e.printStackTrace(System.err);
    }
    }
}  
