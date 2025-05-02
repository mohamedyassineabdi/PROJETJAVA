package com.mycompany.cinema.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AjouterSalleFrame extends JFrame {

    private JTextField champNom, champCapacite;

    public AjouterSalleFrame(Runnable callback) {
        setTitle("Ajouter Salle");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI(callback);
    }

    private void initUI(Runnable callback) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        champNom = new JTextField();
        champCapacite = new JTextField();

        panel.add(new JLabel("Nom :"));
        panel.add(champNom);
        panel.add(new JLabel("Capacité :"));
        panel.add(champCapacite);

        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.addActionListener(e -> ajouterSalle(callback));

        panel.add(new JLabel());
        panel.add(btnAjouter);
        add(panel);
    }

    private void ajouterSalle(Runnable callback) {
        String nom = champNom.getText().trim();
        String capaciteStr = champCapacite.getText().trim();

        if (nom.isEmpty() || capaciteStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont requis.");
            return;
        }

        try {
            int capacite = Integer.parseInt(capaciteStr);
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billetteriecinema", "root", "");
            PreparedStatement ps = conn.prepareStatement("INSERT INTO salles (nom, capacite) VALUES (?, ?)");
            ps.setString(1, nom);
            ps.setInt(2, capacite);
            ps.executeUpdate();
            conn.close();

            JOptionPane.showMessageDialog(this, "Salle ajoutée.");
            if (callback != null) callback.run();
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout.");
        }
    }
}
