package com.mycompany.cinema.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;

public class AjouterSeanceFrame extends JFrame {

    private JTextField champDate, champHeure, champLangue, champPlaces;
    private JComboBox<String> comboFilms, comboSalles;
    private HashMap<String, Integer> filmMap = new HashMap<>();
    private HashMap<String, Integer> salleMap = new HashMap<>();

    public AjouterSeanceFrame(Runnable callback) {
        setTitle("Ajouter une Séance");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI(callback);
    }

    private void initUI(Runnable callback) {
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        champDate = new JTextField();
        champHeure = new JTextField();
        champLangue = new JTextField();
        champPlaces = new JTextField();

        comboFilms = new JComboBox<>();
        comboSalles = new JComboBox<>();
        chargerFilmsEtSalles();

        panel.add(new JLabel("Date (YYYY-MM-DD) :"));
        panel.add(champDate);
        panel.add(new JLabel("Heure (HH:MM) :"));
        panel.add(champHeure);
        panel.add(new JLabel("Langue :"));
        panel.add(champLangue);
        panel.add(new JLabel("Film :"));
        panel.add(comboFilms);
        panel.add(new JLabel("Salle :"));
        panel.add(comboSalles);
        panel.add(new JLabel("Places disponibles :"));
        panel.add(champPlaces);

        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.addActionListener(e -> ajouterSeance(callback));
        panel.add(new JLabel());
        panel.add(btnAjouter);

        add(panel);
    }

    private void chargerFilmsEtSalles() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billetteriecinema", "root", "")) {
            Statement stmt = conn.createStatement();

            ResultSet rsFilms = stmt.executeQuery("SELECT id_film, titre FROM films");
            while (rsFilms.next()) {
                String titre = rsFilms.getString("titre");
                int id = rsFilms.getInt("id_film");
                comboFilms.addItem(titre);
                filmMap.put(titre, id);
            }

            ResultSet rsSalles = stmt.executeQuery("SELECT id_salle, nom FROM salles");
            while (rsSalles.next()) {
                String nom = rsSalles.getString("nom");
                int id = rsSalles.getInt("id_salle");
                comboSalles.addItem(nom);
                salleMap.put(nom, id);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de chargement des films ou salles.");
        }
    }

    private void ajouterSeance(Runnable callback) {
        String date = champDate.getText().trim();
        String heure = champHeure.getText().trim();
        String langue = champLangue.getText().trim();
        String placesStr = champPlaces.getText().trim();

        if (date.isEmpty() || heure.isEmpty() || langue.isEmpty() || placesStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont requis.");
            return;
        }

        try {
            int places = Integer.parseInt(placesStr);
            int idFilm = filmMap.get((String) comboFilms.getSelectedItem());
            int idSalle = salleMap.get((String) comboSalles.getSelectedItem());

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billetteriecinema", "root", "");
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO seances (date_seance, heure, langue, id_film, id_salle, places_disponibles) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, date);
            ps.setString(2, heure);
            ps.setString(3, langue);
            ps.setInt(4, idFilm);
            ps.setInt(5, idSalle);
            ps.setInt(6, places);
            ps.executeUpdate();
            conn.close();

            JOptionPane.showMessageDialog(this, "Séance ajoutée.");
            if (callback != null) callback.run();
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout.");
        }
    }
}
