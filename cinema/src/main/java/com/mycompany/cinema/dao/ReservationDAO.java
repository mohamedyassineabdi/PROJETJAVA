package com.mycompany.cinema.dao;

import com.mycompany.cinema.database.Database;
import com.mycompany.cinema.models.Reservation;

import java.sql.*;

public class ReservationDAO {

    private Connection getConnection() throws SQLException {
        return Database.getConnection();
    }

    // ➕ Ajouter une nouvelle réservation
    public int ajouterReservation(Reservation r) {
    String sql = "INSERT INTO reservations (id_client, id_seance, nb_places, statut) VALUES (?, ?, ?, ?)";
    int generatedId = -1;

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        stmt.setInt(1, r.getIdClient());
        stmt.setInt(2, r.getIdSeance());
        stmt.setInt(3, r.getNbPlaces());
        stmt.setString(4, r.getStatut());

        int affectedRows = stmt.executeUpdate();
        if (affectedRows == 0) throw new SQLException("Insertion échouée.");

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
                r.setIdReservation(generatedId);
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return generatedId;
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

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idReservation);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Reservation(
                    rs.getInt("id_reservation"),
                    rs.getInt("id_client"),
                    rs.getInt("id_seance"),
                    rs.getString("statut"),
                    rs.getTimestamp("date_reservation")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
