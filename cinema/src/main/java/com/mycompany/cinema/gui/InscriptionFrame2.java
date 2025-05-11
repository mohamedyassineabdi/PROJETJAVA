/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// Classe optimisée : InscriptionFrame2
package com.mycompany.cinema.gui;

import com.mycompany.cinema.models.Client;
import com.mycompany.cinema.dao.ClientDAO;

import javax.swing.*;
import java.awt.*;

public class InscriptionFrame2 extends JFrame {

    private JTextField champNom, champPrenom, champEmail;
    private JPasswordField champMotDePasse, champConfirmerMotDePasse;

    public InscriptionFrame2() {
        setTitle("Inscription Client");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initUI();
    }

    private void styleBouton(JButton btn) {
        btn.setBackground(new Color(180, 0, 0));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
    }

    private void initUI() {
        ImageIcon icon = new ImageIcon(getClass().getResource("background_cinema.png"));
        Image img = icon.getImage();
        BackgroundPanel bgPanel = new BackgroundPanel(img);
        setContentPane(bgPanel);
        bgPanel.setLayout(null);

        JLabel titre = new JLabel("Créer un compte client");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titre.setForeground(Color.WHITE);
        titre.setBounds(180, 170, 300, 30);
        bgPanel.add(titre);

        int labelX = 100;
        int fieldX = 270;
        int yStart = 220;
        int lineHeight = 40;

        champNom = addChamp(bgPanel, "Nom :", labelX, fieldX, yStart);
        champPrenom = addChamp(bgPanel, "Prénom :", labelX, fieldX, yStart += lineHeight);
        champEmail = addChamp(bgPanel, "Email :", labelX, fieldX, yStart += lineHeight);

        JLabel labelPass = new JLabel("Mot de passe :");
        labelPass.setForeground(Color.WHITE);
        labelPass.setBounds(labelX, yStart += lineHeight, 150, 25);
        bgPanel.add(labelPass);
        champMotDePasse = new JPasswordField();
        champMotDePasse.setBounds(fieldX, yStart, 200, 25);
        bgPanel.add(champMotDePasse);

        JLabel labelConf = new JLabel("Confirmer le mot de passe :");
        labelConf.setForeground(Color.WHITE);
        labelConf.setBounds(labelX, yStart += lineHeight, 200, 25);
        bgPanel.add(labelConf);
        champConfirmerMotDePasse = new JPasswordField();
        champConfirmerMotDePasse.setBounds(fieldX, yStart, 200, 25);
        bgPanel.add(champConfirmerMotDePasse);

        JButton boutonValider = new JButton("Valider");
        styleBouton(boutonValider);
        boutonValider.setBounds(150, yStart + 60, 120, 40);
        boutonValider.addActionListener(e -> validerInscription());
        bgPanel.add(boutonValider);

        JButton boutonQuitter = new JButton("Annuler");
        styleBouton(boutonQuitter);
        boutonQuitter.setBounds(300, yStart + 60, 120, 40);
        boutonQuitter.addActionListener(e -> {
            this.dispose();
            new MainFrame2().setVisible(true);
        });
        bgPanel.add(boutonQuitter);
    }

    private JTextField addChamp(JPanel panel, String labelText, int labelX, int fieldX, int y) {
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        label.setBounds(labelX, y, 150, 25);
        panel.add(label);
        JTextField champ = new JTextField();
        champ.setBounds(fieldX, y, 200, 25);
        panel.add(champ);
        return champ;
    }

    private void validerInscription() {
        String nom = champNom.getText().trim();
        String prenom = champPrenom.getText().trim();
        String email = champEmail.getText().trim();
        String motDePasse = new String(champMotDePasse.getPassword()).trim();
        String confirmerMotDePasse = new String(champConfirmerMotDePasse.getPassword()).trim();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || motDePasse.isEmpty() || confirmerMotDePasse.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!motDePasse.equals(confirmerMotDePasse)) {
            JOptionPane.showMessageDialog(this, "Les mots de passe ne correspondent pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Client client = new Client(nom, prenom, email, motDePasse);
        ClientDAO clientDAO = new ClientDAO();

        if (clientDAO.insert(client)) {
            JOptionPane.showMessageDialog(this, "Inscription réussie !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            new MainFrame2().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'inscription.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InscriptionFrame2().setVisible(true));
    }
}
