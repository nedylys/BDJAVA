import java.sql.*;

public class ConnectionBase{
    
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER;
    static final String PASSWD;

    public ConnectionBase(String USER, String PASSWD) {
        this.USER = USER;
        this.PASSWD = PASSWD;
    
    public Connection beginConnection(){
        try {
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
        } catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);

        }
    }
    public void close(){
        rset.close();
        conn.close();   
    }
    }

}