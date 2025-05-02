/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema.dao;

import com.mycompany.cinema.database.Database;
import com.mycompany.cinema.models.Salle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalleDAO {

    private Connection getConnection() throws SQLException {
        return Database.getConnection();
    }

    public List<Salle> getAllSalles() {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT * FROM salles";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Salle salle = new Salle(
                    rs.getInt("id_salle"),
                    rs.getString("nom"),
                    rs.getInt("capacite")
                );
                salles.add(salle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salles;
    }

    public Salle getSalleParNom(String nom) {
        Salle salle = null;
        String sql = "SELECT * FROM salles WHERE nom = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                salle = new Salle(
                    rs.getInt("id_salle"),
                    rs.getString("nom"),
                    rs.getInt("capacite")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salle;
    }
}

