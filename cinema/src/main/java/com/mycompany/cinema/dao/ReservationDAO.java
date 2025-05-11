package com.mycompany.cinema.dao;

import com.mycompany.cinema.database.Database;
import com.mycompany.cinema.models.Reservation;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;


public class ReservationDAO {

    private Connection getConnection() throws SQLException {
        return Database.getConnection();
    }

    // ➕ Ajouter une nouvelle réservation
public Reservation ajouterReservation(int idClient, int idSeance, int nbPlaces, String statut) {
    Reservation reservation = null;
    String sql = "INSERT INTO reservations (id_client, id_seance, nb_places, statut, date_reservation) VALUES (?, ?, ?, ?, NOW())";

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        stmt.setInt(1, idClient);
        stmt.setInt(2, idSeance);
        stmt.setInt(3, nbPlaces);
        stmt.setString(4, statut);

        int rows = stmt.executeUpdate();

        if (rows > 0) {
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idReservation = rs.getInt(1);

                // Récupérer la date de réservation actuelle depuis la BDD
                String sqlSelect = "SELECT date_reservation FROM reservations WHERE id_reservation = ?";
                PreparedStatement stmt2 = conn.prepareStatement(sqlSelect);
                stmt2.setInt(1, idReservation);
                ResultSet rs2 = stmt2.executeQuery();

                Timestamp dateRes = null;
                if (rs2.next()) {
                    dateRes = rs2.getTimestamp("date_reservation");
                }

                reservation = new Reservation(idReservation, idClient, idSeance, nbPlaces, statut, dateRes);
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return reservation;
}



    // ✅ Valider le paiement
    public void validerPaiement(int idReservation) {
        String sql = "UPDATE reservations SET statut = 'payé' WHERE id_reservation = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idReservation);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔍 Récupérer une réservation (si besoin)
public Reservation getReservationParId(int idReservation) {
    String sql = "SELECT * FROM reservations WHERE id_reservation = ?";
    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idReservation);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new Reservation(
                rs.getInt("id_reservation"),
                rs.getInt("id_client"),
                rs.getInt("id_seance"),
                rs.getInt("nb_places"), // ✅ IMPORTANT !
                rs.getString("statut"),
                rs.getTimestamp("date_reservation")
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
    
    public List<Reservation> getReservationsParClient(int idClient) {
    List<Reservation> reservations = new ArrayList<>();
    String sql = "SELECT * FROM reservations WHERE id_client = ?";

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idClient);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            reservations.add(new Reservation(
                rs.getInt("id_reservation"),
                rs.getInt("id_client"),
                rs.getInt("id_seance"),
                rs.getInt("nb_places"),
                rs.getString("statut"),
                rs.getTimestamp("date_reservation")
            ));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return reservations;
}
public boolean supprimerReservation(int idReservation) {
    String getSeancePlacesSQL = "SELECT id_seance, nb_places FROM reservations WHERE id_reservation = ?";
    String incrementPlacesSQL = "UPDATE seances SET places_disponibles = places_disponibles + ? WHERE id_seance = ?";
    String deleteBilletsSQL = "DELETE FROM billets WHERE id_reservation = ?";
    String deletePaiementSQL = "DELETE FROM paiements WHERE id_reservation = ?";
    String deleteReservationSQL = "DELETE FROM reservations WHERE id_reservation = ?";

    try (Connection conn = Database.getConnection()) {
        conn.setAutoCommit(false); // ⚠️ DÉBUT TRANSACTION

        try (
            PreparedStatement getSeanceStmt = conn.prepareStatement(getSeancePlacesSQL);
            PreparedStatement incrementPlacesStmt = conn.prepareStatement(incrementPlacesSQL);
            PreparedStatement deleteBilletsStmt = conn.prepareStatement(deleteBilletsSQL);
            PreparedStatement deletePaiementStmt = conn.prepareStatement(deletePaiementSQL);
            PreparedStatement deleteReservationStmt = conn.prepareStatement(deleteReservationSQL)
        ) {
            // 1. Récupérer id_seance et nb_places
            getSeanceStmt.setInt(1, idReservation);
            ResultSet rs = getSeanceStmt.executeQuery();

            int idSeance = -1;
            int nbPlaces = 0;

            if (rs.next()) {
                idSeance = rs.getInt("id_seance");
                nbPlaces = rs.getInt("nb_places");
            }

            // 2. Ré-incrémenter le nombre de places si trouvé
            if (idSeance != -1 && nbPlaces > 0) {
                incrementPlacesStmt.setInt(1, nbPlaces);
                incrementPlacesStmt.setInt(2, idSeance);
                incrementPlacesStmt.executeUpdate();
            }

            // 3. Supprimer les billets liés
            deleteBilletsStmt.setInt(1, idReservation);
            deleteBilletsStmt.executeUpdate();

            // 4. Supprimer le paiement
            deletePaiementStmt.setInt(1, idReservation);
            deletePaiementStmt.executeUpdate();

            // 5. Supprimer la réservation
            deleteReservationStmt.setInt(1, idReservation);
            int rowsDeleted = deleteReservationStmt.executeUpdate();

            conn.commit(); // ✅ tout est bon
            return rowsDeleted > 0;

        } catch (SQLException ex) {
            conn.rollback(); // ❌ erreur → rollback
            ex.printStackTrace();
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return false;
}

}
