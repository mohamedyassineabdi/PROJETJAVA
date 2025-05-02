package com.mycompany.cinema.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ModifierClientFrame extends JFrame {

    private JTextField champNom, champPrenom, champEmail, champMdp;
    private int idClient;

    public ModifierClientFrame(int id, String nom, String prenom, String email, String mdp, Runnable callbackOnSuccess) {
        this.idClient = id;
        setTitle("Modifier Client");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI(nom, prenom, email, mdp, callbackOnSuccess);
    }

    private void initUI(String nom, String prenom, String email, String mdp, Runnable callbackOnSuccess) {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        champNom = new JTextField(nom);
        champPrenom = new JTextField(prenom);
        champEmail = new JTextField(email);
        champMdp = new JTextField(mdp);

        panel.add(new JLabel("Nom :"));
        panel.add(champNom);
        panel.add(new JLabel("Prénom :"));
        panel.add(champPrenom);
        panel.add(new JLabel("Email :"));
        panel.add(champEmail);
        panel.add(new JLabel("Mot de passe :"));
        panel.add(champMdp);

        JButton btnModifier = new JButton("Enregistrer");
        btnModifier.addActionListener(e -> modifierClient(callbackOnSuccess));

        panel.add(new JLabel());
        panel.add(btnModifier);
        add(panel);
    }

    private void modifierClient(Runnable callbackOnSuccess) {
        String nom = champNom.getText().trim();
        String prenom = champPrenom.getText().trim();
        String email = champEmail.getText().trim();
        String mdp = champMdp.getText().trim();

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billetteriecinema", "root", "");
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE clients SET nom=?, prenom=?, email=?, mot_de_passe=? WHERE id_client=?");
            ps.setString(1, nom);
            ps.setString(2, prenom);
            ps.setString(3, email);
            ps.setString(4, mdp);
            ps.setInt(5, idClient);
            ps.executeUpdate();
            conn.close();

            JOptionPane.showMessageDialog(this, "Client modifié avec succès !");
            if (callbackOnSuccess != null) callbackOnSuccess.run();
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification.");
        }
    }
}
