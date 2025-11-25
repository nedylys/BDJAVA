package main;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class CloturerCommande {

    private final Connection conn;
    private final Scanner scanner = new Scanner(System.in);

    public CloturerCommande(Connection conn) {
        this.conn = conn;
    }

    public void clotureCommande() {
    try {
        System.out.print("ID commande : ");
        int id = Integer.parseInt(scanner.nextLine());

        // Vérifier statut actuel
        String statutActuel = null;
        try (PreparedStatement ps = conn.prepareStatement(
            "SELECT StatutCommandeL FROM CommandeaLivrer WHERE idCommande = ? FOR UPDATE")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) statutActuel = rs.getString(1);
                else {
                    System.out.println("Commande inexistante.");
                    return;
                }
            }
        }

        if (!statutActuel.equals("EN_PREPARATION") && 
            !statutActuel.equals("PRETE")) {
            System.out.println("Cette commande ne peut plus être clôturée.");
            return;
        }

        // Mode de récupération
        System.out.print("Mode récupération (RETRAIT/LIVRAISON) : ");
        String mode = scanner.nextLine().trim().toUpperCase();

        // Paiement uniquement si en boutique
        System.out.print("Mode paiement (EN_BOUTIQUE/EN_LIGNE) : ");
        String paiement = scanner.nextLine().trim().toUpperCase();

        if (paiement.equals("EN_BOUTIQUE")) {
            try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE Commande SET ModePaiement = 'EN_BOUTIQUE' WHERE idCommande = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        }

        
        if (mode.equals("LIVRAISON")) {
            double frais = 7.50;
            System.out.println("Frais de livraison : " + frais + "€");

            try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE CommandeALivrer SET FraisLivraison = ?, DateLivraisonEstimee = SYSDATE WHERE idCommande = ?")) {
                ps.setDouble(1, frais);
                ps.setInt(2, id);
                ps.executeUpdate();
            }
        }

        // Nouveau statut
        String statutFinal = mode.equals("RETRAIT") ? "RECUPEREE" : "LIVREE";

        try (PreparedStatement ps = conn.prepareStatement(
            "UPDATE Commande SET statut_commande = ? WHERE idCommande = ?")) {
            ps.setString(1, statutFinal);
            ps.setInt(2, id);
            ps.executeUpdate();
        }

        conn.commit();
        System.out.println("Commande clôturée avec succès.");

    } catch (Exception e) {
        System.err.println("Erreur : " + e.getMessage());
        try { conn.rollback(); } catch (Exception ignore) {}
    }
}
}
