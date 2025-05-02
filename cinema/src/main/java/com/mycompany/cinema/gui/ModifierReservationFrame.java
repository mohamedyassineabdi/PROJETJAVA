package com.mycompany.cinema.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;

public class ModifierReservationFrame extends JFrame {

    private JTextField champDate, champStatut;
    private JComboBox<String> comboClients, comboSeances;
    private HashMap<String, Integer> clientMap = new HashMap<>();
    private HashMap<String, Integer> seanceMap = new HashMap<>();
    private int idReservation;

    public ModifierReservationFrame(int id, String date, String statut, int idClient, int idSeance, Runnable callback) {
        this.idReservation = id;
        setTitle("Modifier Réservation");
        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI(date, statut, idClient, idSeance, callback);
    }

    private void initUI(String date, String statut, int idClient, int idSeance, Runnable callback) {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        champDate = new JTextField(date);
        champStatut = new JTextField(statut);

        comboClients = new JComboBox<>();
        comboSeances = new JComboBox<>();
        chargerClientsEtSeances(idClient, idSeance);

        panel.add(new JLabel("Date :"));
        panel.add(champDate);
        panel.add(new JLabel("Statut :"));
        panel.add(champStatut);
        panel.add(new JLabel("Client :"));
        panel.add(comboClients);
        panel.add(new JLabel("Séance :"));
        panel.add(comboSeances);

        JButton btnModifier = new JButton("Enregistrer");
        btnModifier.addActionListener(e -> modifierReservation(callback));
        panel.add(new JLabel());
        panel.add(btnModifier);

        add(panel);
    }

    private void chargerClientsEtSeances(int selectedClientId, int selectedSeanceId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billetteriecinema", "root", "")) {
            Statement stmt = conn.createStatement();

            ResultSet rsClients = stmt.executeQuery("SELECT id_client, nom FROM clients");
            while (rsClients.next()) {
                String nom = rsClients.getString("nom");
                int id = rsClients.getInt("id_client");
                comboClients.addItem(nom);
                clientMap.put(nom, id);
                if (id == selectedClientId) comboClients.setSelectedItem(nom);
            }

            ResultSet rsSeances = stmt.executeQuery("SELECT id_seance, date_seance FROM seances");
            while (rsSeances.next()) {
                String desc = rsSeances.getString("date_seance");
                int id = rsSeances.getInt("id_seance");
                comboSeances.addItem(desc);
                seanceMap.put(desc, id);
                if (id == selectedSeanceId) comboSeances.setSelectedItem(desc);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void modifierReservation(Runnable callback) {
        String date = champDate.getText().trim();
        String statut = champStatut.getText().trim();

        try {
            int idClient = clientMap.get((String) comboClients.getSelectedItem());
            int idSeance = seanceMap.get((String) comboSeances.getSelectedItem());

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billetteriecinema", "root", "");
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE reservations SET date_reservation=?, statut=?, id_client=?, id_seance=? WHERE id_reservation=?");
            ps.setString(1, date);
            ps.setString(2, statut);
            ps.setInt(3, idClient);
            ps.setInt(4, idSeance);
            ps.setInt(5, idReservation);
            ps.executeUpdate();
            conn.close();

            JOptionPane.showMessageDialog(this, "Réservation modifiée.");
            if (callback != null) callback.run();
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification.");
        }
    }
}
