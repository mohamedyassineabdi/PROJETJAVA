/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema.dao;

import com.mycompany.cinema.database.Database;
import java.sql.*;

public class PaiementDAO {

    public void ajouterPaiement(double montant, String statut, int idReservation) {
        String sql = "INSERT INTO paiements (montant, statut, id_reservation) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, montant);
            stmt.setString(2, statut);
            stmt.setInt(3, idReservation);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void validerPaiement(int idReservation) {
    String sql = "UPDATE paiements SET statut = 'payé' WHERE id_reservation = ?";

    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idReservation);
        stmt.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}
