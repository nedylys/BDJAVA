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
        System.out.print("Entrez l'ID de la commande : ");
        int idCommande = scanner.nextInt();
        scanner.nextLine();

        String requete =
            "SELECT * from CommandeàLivrer WHERE idCommande = ?";

        try (PreparedStatement ps = conn.prepareStatement(requete)) {
            ps.setInt(1, idCommande);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    System.out.println("Aucune commande trouvée.");
                    return;
                }

                System.out.println("\n===== Détails de la commande =====");
                System.out.println("ID commande      : " + rs.getInt("idCommande"));
                System.out.println("Statut commande  : " + rs.getString("StatutCommandeL,"));
                System.out.println("Date de livraison estimée     : " + rs.getString("DateLivraisonEstimée"));
                System.out.println("===================================\n");
            }

            conn.commit();
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
            try { conn.rollback(); } catch (SQLException ignore) {}
        }
    }
}
