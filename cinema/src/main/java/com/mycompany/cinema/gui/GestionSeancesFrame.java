package com.mycompany.cinema.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class GestionSeancesFrame extends JFrame {

    private JTable tableSeances;
    private DefaultTableModel model;

    public GestionSeancesFrame() {
        setTitle("Gestion des Séances");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        chargerSeancesDepuisBD();
    }

    private void initUI() {
        ImageIcon icon = new ImageIcon(getClass().getResource("background_cinema.png"));
        Image img = icon.getImage();
        BackgroundPanel bgPanel = new BackgroundPanel(img);
        setContentPane(bgPanel);
        bgPanel.setLayout(null);

        model = new DefaultTableModel(new String[]{
                "ID", "Date", "Heure", "Langue", "ID Film", "ID Salle", "Places disponibles"}, 0);
        tableSeances = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableSeances);
        scrollPane.setBounds(50, 150, 700, 250);
        bgPanel.add(scrollPane);

        JButton btnAjouter = new JButton("Ajouter Séance");
        JButton btnModifier = new JButton("Modifier Séance");
        JButton btnSupprimer = new JButton("Supprimer Séance");
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

        btnAjouter.addActionListener(e ->
                new AjouterSeanceFrame(this::chargerSeancesDepuisBD).setVisible(true)
        );

        btnModifier.addActionListener(e -> {
            int row = tableSeances.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez une séance à modifier.");
                return;
            }

            int id = (int) model.getValueAt(row, 0);
            String date = (String) model.getValueAt(row, 1);
            String heure = (String) model.getValueAt(row, 2);
            String langue = (String) model.getValueAt(row, 3);
            int idFilm = (int) model.getValueAt(row, 4);
            int idSalle = (int) model.getValueAt(row, 5);
            int places = (int) model.getValueAt(row, 6);

            new ModifierSeanceFrame(id, date, heure, langue, idFilm, idSalle, places,
                    this::chargerSeancesDepuisBD).setVisible(true);
        });

        btnSupprimer.addActionListener(e -> {
            int row = tableSeances.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez une séance à supprimer.");
                return;
            }

            int id = (int) model.getValueAt(row, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "Supprimer cette séance ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billetteriecinema", "root", "");
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM seances WHERE id_seance = ?");
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    conn.close();
                    chargerSeancesDepuisBD();
                    JOptionPane.showMessageDialog(this, "Séance supprimée.");
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

    private void chargerSeancesDepuisBD() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billetteriecinema", "root", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM seances");

            model.setRowCount(0);
            while (rs.next()) {
                int id = rs.getInt("id_seance");
                String date = rs.getString("date_seance");
                String heure = rs.getString("heure");
                String langue = rs.getString("langue");
                int idFilm = rs.getInt("id_film");
                int idSalle = rs.getInt("id_salle");
                int places = rs.getInt("places_disponibles");

                model.addRow(new Object[]{id, date, heure, langue, idFilm, idSalle, places});
            }

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de chargement des séances.");
        }
    }
}