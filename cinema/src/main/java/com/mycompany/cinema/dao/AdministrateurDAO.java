package com.mycompany.cinema.dao;

import com.mycompany.cinema.database.Database;
import com.mycompany.cinema.models.Administrateur;
import com.mycompany.cinema.utils.HashUtil;

import java.sql.*;

public class AdministrateurDAO {

    public boolean insert(Administrateur administrateur) {
        String sql = "INSERT INTO administrateurs (nom, prenom, email, mot_de_passe) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, administrateur.getNom());
            stmt.setString(2, administrateur.getPrenom());
            stmt.setString(3, administrateur.getEmail());

            // Hash du mot de passe avant insertion
            String motDePasseHash = HashUtil.hashPassword(administrateur.getMotDePasse());
            stmt.setString(4, motDePasseHash);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Administrateur authentifier(String email, String motDePasseClair) {
        String sql = "SELECT * FROM administrateurs WHERE email = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String motDePasseStocke = rs.getString("mot_de_passe");
                String motDePasseHashe = HashUtil.hashPassword(motDePasseClair);

                if (motDePasseStocke.equals(motDePasseHashe)) {
                    Administrateur admin = new Administrateur();
                    admin.setId(rs.getInt("id_admin"));
                    admin.setNom(rs.getString("nom"));
                    admin.setPrenom(rs.getString("prenom"));
                    admin.setEmail(rs.getString("email"));
                    admin.setMotDePasse(motDePasseStocke); // le mot de passe reste hashé
                    return admin;
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
