package com.mycompany.cinema.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;

public class AjouterReservationFrame extends JFrame {

    private JTextField champDate, champStatut;
    private JComboBox<String> comboClients, comboSeances;
    private HashMap<String, Integer> clientMap = new HashMap<>();
    private HashMap<String, Integer> seanceMap = new HashMap<>();

    public AjouterReservationFrame(Runnable callback) {
        setTitle("Ajouter une Réservation");
        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI(callback);
    }

    private void initUI(Runnable callback) {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        champDate = new JTextField();
        champStatut = new JTextField();
        comboClients = new JComboBox<>();
        comboSeances = new JComboBox<>();

        chargerClientsEtSeances();

        panel.add(new JLabel("Date (YYYY-MM-DD) :"));
        panel.add(champDate);
        panel.add(new JLabel("Statut :"));
        panel.add(champStatut);
        panel.add(new JLabel("Client :"));
        panel.add(comboClients);
        panel.add(new JLabel("Séance :"));
        panel.add(comboSeances);

        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.addActionListener(e -> ajouterReservation(callback));
        panel.add(new JLabel());
        panel.add(btnAjouter);

        add(panel);
    }

    private void chargerClientsEtSeances() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billetteriecinema", "root", "")) {
            Statement stmt = conn.createStatement();

            ResultSet rsClients = stmt.executeQuery("SELECT id_client, nom FROM clients");
            while (rsClients.next()) {
                int id = rsClients.getInt("id_client");
                String nom = rsClients.getString("nom");
                comboClients.addItem(nom);
                clientMap.put(nom, id);
            }

            ResultSet rsSeances = stmt.executeQuery("SELECT id_seance, date_seance FROM seances");
            while (rsSeances.next()) {
                int id = rsSeances.getInt("id_seance");
                String date = rsSeances.getString("date_seance");
                comboSeances.addItem("Séance " + id + " - " + date);
                seanceMap.put("Séance " + id + " - " + date, id);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de chargement clients/séances.");
        }
    }

    private void ajouterReservation(Runnable callback) {
        String date = champDate.getText().trim();
        String statut = champStatut.getText().trim();

        if (date.isEmpty() || statut.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires.");
            return;
        }

        try {
            int idClient = clientMap.get((String) comboClients.getSelectedItem());
            int idSeance = seanceMap.get((String) comboSeances.getSelectedItem());

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billetteriecinema", "root", "");
            PreparedStatement ps = conn.prepareStatement("INSERT INTO reservations (date_reservation, statut, id_client, id_seance) VALUES (?, ?, ?, ?)");
            ps.setString(1, date);
            ps.setString(2, statut);
            ps.setInt(3, idClient);
            ps.setInt(4, idSeance);
            ps.executeUpdate();
            conn.close();

            JOptionPane.showMessageDialog(this, "Réservation ajoutée.");
            if (callback != null) callback.run();
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout.");
        }
    }
}
