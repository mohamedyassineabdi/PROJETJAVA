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
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        chargerReservationsDepuisBD();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());

        model = new DefaultTableModel(new String[]{"ID", "Date", "Statut", "ID Client", "ID Séance"}, 0);
        tableReservations = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableReservations);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel panelBoutons = new JPanel();
        JButton btnSupprimer = new JButton("Supprimer Réservation");
        JButton btnAjouter = new JButton("Ajouter Réservation");
        JButton btnModifier = new JButton("Modifier Réservation");
panelBoutons.add(btnAjouter);
panelBoutons.add(btnModifier);

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

        JButton btnRetour = new JButton("Retour à l'accueil");
        

        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnRetour);
        panel.add(panelBoutons, BorderLayout.SOUTH);

        add(panel);
        
        btnAjouter.addActionListener(e -> {
    new AjouterReservationFrame(this::chargerReservationsDepuisBD).setVisible(true);
});


        // Action : Supprimer
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

        // Action : Retour
        btnRetour.addActionListener(e -> {
            new AcceuilAdmin().setVisible(true);
            dispose();
        });
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
