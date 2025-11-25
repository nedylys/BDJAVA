package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SupprimerCompte {
    private String emailClient;
    private Connection conn;

    public SupprimerCompte(String emailClient, Connection conn) {
        this.emailClient = emailClient;
        this.conn = conn;
    }

    public void supprimerCompte() {
        String deleteQuery = "DELETE FROM Client WHERE emailClient = ?";
        try (PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
            ps.setString(1, emailClient);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Compte supprimé avec succès.");
            } else {
                System.out.println("Aucun compte trouvé avec cet email.");
            }
            conn.commit();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du compte : " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Erreur lors du rollback : " + ex.getMessage());
            }
        }
    }

    

}
