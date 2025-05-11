package com.mycompany.cinema.dao;

import com.mycompany.cinema.models.Billet;
import com.mycompany.cinema.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class BilletDAO {

    public void creerBilletsPourReservation(int idReservation, int nbPlaces) {
        String sql = "INSERT INTO billets (numero_billet, id_reservation) VALUES (?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < nbPlaces; i++) {
                String numero = "B-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                stmt.setString(1, numero);
                stmt.setInt(2, idReservation);
                stmt.addBatch(); // Plus rapide pour plusieurs insertions
            }

            stmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
