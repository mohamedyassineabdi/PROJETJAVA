package com.mycompany.cinema.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AjouterClientFrame extends JFrame {

    private JTextField champNom, champPrenom, champEmail, champMdp;

    public AjouterClientFrame(Runnable callbackOnSuccess) {
        setTitle("Ajouter un Client");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI(callbackOnSuccess);
    }

    private void initUI(Runnable callbackOnSuccess) {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        champNom = new JTextField();
        champPrenom = new JTextField();
        champEmail = new JTextField();
        champMdp = new JTextField();

        panel.add(new JLabel("Nom :"));
        panel.add(champNom);
        panel.add(new JLabel("Prénom :"));
        panel.add(champPrenom);
        panel.add(new JLabel("Email :"));
        panel.add(champEmail);
        panel.add(new JLabel("Mot de passe :"));
        panel.add(champMdp);

        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.addActionListener(e -> ajouterClient(callbackOnSuccess));

        panel.add(new JLabel());
        panel.add(btnAjouter);
        add(panel);
    }

    private void ajouterClient(Runnable callbackOnSuccess) {
        String nom = champNom.getText().trim();
        String prenom = champPrenom.getText().trim();
        String email = champEmail.getText().trim();
        String mdp = champMdp.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires.");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billetteriecinema", "root", "");
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO clients (nom, prenom, email, mot_de_passe) VALUES (?, ?, ?, ?)");
            ps.setString(1, nom);
            ps.setString(2, prenom);
            ps.setString(3, email);
            ps.setString(4, mdp);
            ps.executeUpdate();
            conn.close();

            JOptionPane.showMessageDialog(this, "Client ajouté avec succès !");
            if (callbackOnSuccess != null) callbackOnSuccess.run();
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout.");
        }
    }
}
