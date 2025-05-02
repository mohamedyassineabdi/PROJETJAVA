package com.mycompany.cinema.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ModifierFilmFrame extends JFrame {

    private JTextField champTitre, champGenre, champDuree, champDescription;
    private int idFilm;

    public ModifierFilmFrame(int idFilm, String titre, String genre, int duree, String description, Runnable callbackOnSuccess) {
        this.idFilm = idFilm;
        setTitle("Modifier le Film");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI(titre, genre, duree, description, callbackOnSuccess);
    }

    private void initUI(String titre, String genre, int duree, String description, Runnable callbackOnSuccess) {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel labelTitre = new JLabel("Titre :");
        champTitre = new JTextField(titre);

        JLabel labelGenre = new JLabel("Genre :");
        champGenre = new JTextField(genre);

        JLabel labelDuree = new JLabel("Durée (en minutes) :");
        champDuree = new JTextField(String.valueOf(duree));

        JLabel labelDescription = new JLabel("Description :");
        champDescription = new JTextField(description);

        JButton btnEnregistrer = new JButton("Enregistrer");
        btnEnregistrer.addActionListener(e -> modifierFilm(callbackOnSuccess));

        panel.add(labelTitre);
        panel.add(champTitre);

        panel.add(labelGenre);
        panel.add(champGenre);

        panel.add(labelDuree);
        panel.add(champDuree);

        panel.add(labelDescription);
        panel.add(champDescription);

        panel.add(new JLabel()); // vide
        panel.add(btnEnregistrer);

        add(panel);
    }

    private void modifierFilm(Runnable callbackOnSuccess) {
        String titre = champTitre.getText().trim();
        String genre = champGenre.getText().trim();
        String dureeStr = champDuree.getText().trim();
        String description = champDescription.getText().trim();

        if (titre.isEmpty() || genre.isEmpty() || dureeStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
            return;
        }

        try {
            int duree = Integer.parseInt(dureeStr);
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billetteriecinema", "root", "");
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE films SET titre=?, genre=?, duree=?, description=? WHERE id_film=?");
            ps.setString(1, titre);
            ps.setString(2, genre);
            ps.setInt(3, duree);
            ps.setString(4, description);
            ps.setInt(5, idFilm);
            ps.executeUpdate();
            conn.close();

            JOptionPane.showMessageDialog(this, "Film modifié avec succès !");
            if (callbackOnSuccess != null) callbackOnSuccess.run();
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Durée invalide.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification.");
        }
    }
}
