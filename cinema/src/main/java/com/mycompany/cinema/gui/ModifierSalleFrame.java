package com.mycompany.cinema.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ModifierSalleFrame extends JFrame {

    private JTextField champNom, champCapacite;
    private int idSalle;

    public ModifierSalleFrame(int id, String nom, int capacite, Runnable callback) {
        this.idSalle = id;
        setTitle("Modifier Salle");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI(nom, capacite, callback);
    }

    private void initUI(String nom, int capacite, Runnable callback) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        champNom = new JTextField(nom);
        champCapacite = new JTextField(String.valueOf(capacite));

        panel.add(new JLabel("Nom :"));
        panel.add(champNom);
        panel.add(new JLabel("Capacité :"));
        panel.add(champCapacite);

        JButton btnModifier = new JButton("Enregistrer");
        btnModifier.addActionListener(e -> modifierSalle(callback));

        panel.add(new JLabel());
        panel.add(btnModifier);
        add(panel);
    }

    private void modifierSalle(Runnable callback) {
        String nom = champNom.getText().trim();
        String capaciteStr = champCapacite.getText().trim();

        try {
            int capacite = Integer.parseInt(capaciteStr);
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billetteriecinema", "root", "");
            PreparedStatement ps = conn.prepareStatement("UPDATE salles SET nom=?, capacite=? WHERE id_salle=?");
            ps.setString(1, nom);
            ps.setInt(2, capacite);
            ps.setInt(3, idSalle);
            ps.executeUpdate();
            conn.close();

            JOptionPane.showMessageDialog(this, "Salle modifiée.");
            if (callback != null) callback.run();
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification.");
        }
    }
}
