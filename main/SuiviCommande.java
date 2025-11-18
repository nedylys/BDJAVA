import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class SuiviCommande {

    private final Connection conn;
    private final Scanner scanner = new Scanner(System.in);


    public SuiviCommande(Connection conn) {
        this.conn = conn;
    }
    public void suivreCommandes() {
       
        String requete = "SELECT c.idCommande, c.DateCommande, c.statut_commande, SUM(lcp.SousTotalLigneP) AS montant_total FROM Commande c, LigneCommandeProduit lcp WHERE c.id_commande = lcp.id_commande AND c.idClient = ? GROUP BY c.idCommande, c.DateCommande, c.statut_commande ORDER BY c.DateCommande DESC";
        try (PreparedStatement ps = conn.prepareStatement(requete);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;
            while (rs.next()) {
                found = true;
                int id = rs.getInt("id");
                String email = rs.getString("client_email");
                String date = rs.getString("date_cmd");
                String statut = rs.getString("statut");
                double total = rs.getDouble("total");

                System.out.printf("Commande #%d | %s | %s | %-12s | total = %.2f €%n",
                        id, email, date, statut, total);
            }

            if (!found) System.out.println("Aucune commande en cours.");
            conn.commit();

        } catch (SQLException e) {
            System.err.println("Erreur lors du suivi des commandes : " + e.getMessage());
            try { conn.rollback(); } catch (SQLException ignore) {}
        }

}
}
