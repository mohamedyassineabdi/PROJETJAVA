package com.mycompany.cinema.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class GestionReservationsFrame extends JFrame {

    private JTable tableReservations;
    private DefaultTableModel model;

    public GestionReservationsFrame() {
        setTitle("Gestion des Réservations");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        chargerReservationsDepuisBD();
    }

    private void initUI() {
        ImageIcon icon = new ImageIcon(getClass().getResource("background_cinema.png"));
        Image img = icon.getImage();
        BackgroundPanel bgPanel = new BackgroundPanel(img);
        setContentPane(bgPanel);
        bgPanel.setLayout(null);

        model = new DefaultTableModel(new String[]{"ID", "Date", "Statut", "ID Client", "ID Séance"}, 0);
        tableReservations = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableReservations);
        scrollPane.setBounds(50, 150, 700, 250);
        bgPanel.add(scrollPane);

        JButton btnAjouter = new JButton("Ajouter Réservation");
        JButton btnModifier = new JButton("Modifier Réservation");
        JButton btnSupprimer = new JButton("Supprimer Réservation");
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

        btnAjouter.addActionListener(e -> new AjouterReservationFrame(this::chargerReservationsDepuisBD).setVisible(true));

        btnModifier.addActionListener(e -> {
            int row = tableReservations.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez une réservation à modifier.");
                return;
            }

            int id = (int) model.getValueAt(row, 0);
            String date = (String) model.getValueAt(row, 1);
            String statut = (String) model.getValueAt(row, 2);
            int idClient = (int) model.getValueAt(row, 3);
            int idSeance = (int) model.getValueAt(row, 4);

            new ModifierReservationFrame(id, date, statut, idClient, idSeance, this::chargerReservationsDepuisBD).setVisible(true);
        });

        btnSupprimer.addActionListener(e -> {
            int selectedRow = tableReservations.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une réservation à supprimer.");
                return;
            }

            int idReservation = (int) model.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billetteriecinema", "root", "")) {
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM reservations WHERE id_reservation = ?");
                    ps.setInt(1, idReservation);
                    ps.executeUpdate();
                    chargerReservationsDepuisBD();
                    JOptionPane.showMessageDialog(this, "Réservation supprimée.");
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

    private void chargerReservationsDepuisBD() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billetteriecinema", "root", "")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM reservations");

            model.setRowCount(0);
            while (rs.next()) {
                int id = rs.getInt("id_reservation");
                String date = rs.getString("date_reservation");
                String statut = rs.getString("statut");
                int idClient = rs.getInt("id_client");
                int idSeance = rs.getInt("id_seance");

                model.addRow(new Object[]{id, date, statut, idClient, idSeance});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de chargement des réservations.");
        }
    }
}