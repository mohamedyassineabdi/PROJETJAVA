package com.mycompany.cinema.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class GestionFilmsFrame extends JFrame {

    private JTable tableFilms;
    private DefaultTableModel model;

    public GestionFilmsFrame() {
        setTitle("Gestion des Films");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        chargerFilmsDepuisBD();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());

        // Table avec toutes les colonnes
        model = new DefaultTableModel(new String[]{"ID", "Titre", "Genre", "Durée", "Description"}, 0);
        tableFilms = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableFilms);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Boutons
        JPanel panelBoutons = new JPanel();
        JButton btnAjouter = new JButton("Ajouter Film");
        JButton btnModifier = new JButton("Modifier Film");
        JButton btnSupprimer = new JButton("Supprimer Film");
        JButton btnRetour = new JButton("Retour à l'accueil");

        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnRetour);
        panel.add(panelBoutons, BorderLayout.SOUTH);
        add(panel);

        // Action : Ajouter
        btnAjouter.addActionListener(e -> {
            new AjouterFilmFrame(this::chargerFilmsDepuisBD).setVisible(true);
        });

        // Action : Modifier
        btnModifier.addActionListener(e -> {
            int selectedRow = tableFilms.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un film à modifier.");
                return;
            }

            int idFilm = (int) model.getValueAt(selectedRow, 0);
            String titre = (String) model.getValueAt(selectedRow, 1);
            String genre = (String) model.getValueAt(selectedRow, 2);
            int duree = (int) model.getValueAt(selectedRow, 3);
            String description = (String) model.getValueAt(selectedRow, 4);

            new ModifierFilmFrame(idFilm, titre, genre, duree, description, this::chargerFilmsDepuisBD).setVisible(true);
        });

        // Action : Supprimer
        btnSupprimer.addActionListener(e -> {
            int selectedRow = tableFilms.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un film à supprimer.");
                return;
            }

            int idFilm = (int) model.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Voulez-vous vraiment supprimer ce film ?", "Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billetteriecinema", "root", "");
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM films WHERE id_film = ?");
                    ps.setInt(1, idFilm);
                    ps.executeUpdate();
                    conn.close();

                    chargerFilmsDepuisBD(); // Mise à jour de la table
                    JOptionPane.showMessageDialog(this, "Film supprimé avec succès.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
                }
            }
        });

        // Action : Retour à l'accueil
        btnRetour.addActionListener(e -> {
            new AcceuilAdmin().setVisible(true); // Remplace si ta classe a un nom différent
            dispose(); // Fermer la fenêtre actuelle
        });
    }

    private void chargerFilmsDepuisBD() {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/billetteriecinema", "root", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM films");

            model.setRowCount(0); // vider la table

            while (rs.next()) {
                int id = rs.getInt("id_film");
                String titre = rs.getString("titre");
                String genre = rs.getString("genre");
                int duree = rs.getInt("duree");
                String description = rs.getString("description");

                model.addRow(new Object[]{id, titre, genre, duree, description});
            }

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données.");
        }
    }
}
