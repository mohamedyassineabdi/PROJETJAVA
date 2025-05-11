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
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        chargerFilmsDepuisBD();
    }

    private void initUI() {
        ImageIcon icon = new ImageIcon(getClass().getResource("background_cinema.png"));
        Image img = icon.getImage();
        BackgroundPanel bgPanel = new BackgroundPanel(img);
        setContentPane(bgPanel);
        bgPanel.setLayout(null);

        model = new DefaultTableModel(new String[]{"ID", "Titre", "Genre", "Durée", "Description"}, 0);
        tableFilms = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableFilms);
        scrollPane.setBounds(50, 150, 700, 250);
        bgPanel.add(scrollPane);

        JButton btnAjouter = new JButton("Ajouter Film");
        JButton btnModifier = new JButton("Modifier Film");
        JButton btnSupprimer = new JButton("Supprimer Film");
        JButton btnRetour = new JButton("Retour à l'accueil");

        styliserBouton(btnAjouter);
        styliserBouton(btnModifier);
        styliserBouton(btnSupprimer);
        styliserBoutonBlanc(btnRetour);

        btnAjouter.setBounds(80, 420, 160, 40);
        btnModifier.setBounds(250, 420, 160, 40);
        btnSupprimer.setBounds(420, 420, 160, 40);
        btnRetour.setBounds(590, 420, 160, 40);

        bgPanel.add(btnAjouter);
        bgPanel.add(btnModifier);
        bgPanel.add(btnSupprimer);
        bgPanel.add(btnRetour);

        btnAjouter.addActionListener(e -> {
            new AjouterFilmFrame(this::chargerFilmsDepuisBD).setVisible(true);
        });

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

                    chargerFilmsDepuisBD();
                    JOptionPane.showMessageDialog(this, "Film supprimé avec succès.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
                }
            }
        });

        btnRetour.addActionListener(e -> {
            new AcceuilAdmin().setVisible(true);
            dispose();
        });
    }

    private void styliserBouton(JButton btn) {
        btn.setBackground(new Color(180, 0, 0));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void styliserBoutonBlanc(JButton btn) {
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.DARK_GRAY);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
    }

    private void chargerFilmsDepuisBD() {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/billetteriecinema", "root", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM films");

            model.setRowCount(0);

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