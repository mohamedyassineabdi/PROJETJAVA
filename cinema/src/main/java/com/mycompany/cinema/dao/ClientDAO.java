/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema.dao;

/**
 *
 * @author yassi
 */


import com.mycompany.cinema.models.Client;
import com.mycompany.cinema.database.Database;
import java.sql.*;
import com.mycompany.cinema.utils.HashUtil;



public class ClientDAO {

    public boolean insert(Client client) {
    String sql = "INSERT INTO clients (nom, prenom, email, mot_de_passe) VALUES (?, ?, ?, ?)";

    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, client.getNom());
        stmt.setString(2, client.getPrenom());
        stmt.setString(3, client.getEmail());
        stmt.setString(4, client.getMotDePasse());

        int rowsInserted = stmt.executeUpdate();
        return rowsInserted > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    

    public Client authentifier(String email, String motDePasse) {
        String sql = "SELECT * FROM clients WHERE email = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String motDePasseHashEnBase = rs.getString("mot_de_passe");
                String motDePasseHashSaisi = HashUtil.hashPassword(motDePasse);

                if (motDePasseHashEnBase.equals(motDePasseHashSaisi)) {
                    Client client = new Client(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        motDePasse
                    );
                    client.setIdClient(rs.getInt("id_client"));
                    return client;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Si non trouvé ou mot de passe incorrect
    }
}
