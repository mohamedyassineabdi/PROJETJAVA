/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AjouterFilmFrame extends JFrame {

    private JTextField champTitre, champGenre, champDuree;
    private JTextArea champDescription;

    public AjouterFilmFrame(Runnable callbackOnSuccess) {
        setTitle("Ajouter un Film");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI(callbackOnSuccess);
    }

    private void initUI(Runnable callbackOnSuccess) {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel labelTitre = new JLabel("Titre :");
        champTitre = new JTextField();

        JLabel labelGenre = new JLabel("Genre :");
        champGenre = new JTextField();

        JLabel labelDuree = new JLabel("Durée (en minutes) :");
        champDuree = new JTextField();

        JLabel labelDescription = new JLabel("Description :");
        champDescription = new JTextArea(3, 20);
        JScrollPane scroll = new JScrollPane(champDescription);

        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.addActionListener(e -> ajouterFilm(callbackOnSuccess));

        panel.add(labelTitre);
        panel.add(champTitre);

        panel.add(labelGenre);
        panel.add(champGenre);

        panel.add(labelDuree);
        panel.add(champDuree);

        panel.add(labelDescription);
        panel.add(scroll);

        panel.add(new JLabel()); // vide
        panel.add(btnAjouter);

        add(panel);
    }

    private void ajouterFilm(Runnable callbackOnSuccess) {
        String titre = champTitre.getText().trim();
        String genre = champGenre.getText().trim();
        String dureeStr = champDuree.getText().trim();
        String description = champDescription.getText().trim();

        if (titre.isEmpty() || genre.isEmpty() || dureeStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        try {
            int duree = Integer.parseInt(dureeStr);
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billetteriecinema", "root", "");
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO films (titre, genre, duree, description) VALUES (?, ?, ?, ?)");
            ps.setString(1, titre);
            ps.setString(2, genre);
            ps.setInt(3, duree);
            ps.setString(4, description);
            ps.executeUpdate();
            conn.close();

            JOptionPane.showMessageDialog(this, "Film ajouté avec succès !");
            if (callbackOnSuccess != null) callbackOnSuccess.run();
            dispose(); // Ferme la fenêtre
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Durée invalide.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du film.");
        }
    }
}
