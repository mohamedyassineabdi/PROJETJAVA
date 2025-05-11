package com.mycompany.cinema.gui;

import com.mycompany.cinema.models.Client;

import javax.swing.*;
import java.awt.*;

public class AccueilClient extends JFrame {

    private Client clientConnecte;

    private JButton btnReservations;
    private JButton jButton1;
    private JButton jButton3;
    private JLabel labelBienvenue;

    public AccueilClient(Client client) {
        this.clientConnecte = client;
        initComponents();
        labelBienvenue.setText("Bienvenue " + clientConnecte.getPrenom() + " !");
    }

    public AccueilClient() {
        initComponents();
    }

    private void initComponents() {
        ImageIcon icon = new ImageIcon(getClass().getResource("background_cinema.png"));
        Image img = icon.getImage();
        BackgroundPanel bgPanel = new BackgroundPanel(img);
        setContentPane(bgPanel);
        bgPanel.setLayout(null);

        setTitle("Espace Client");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Bienvenue
        labelBienvenue = new JLabel("Bienvenue, [Nom Client] !");
        labelBienvenue.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelBienvenue.setForeground(Color.WHITE);
        labelBienvenue.setHorizontalAlignment(SwingConstants.CENTER);
        labelBienvenue.setBounds(100, 150, 300, 30); // Aligné sous le logo du fond
        bgPanel.add(labelBienvenue);

        // Bouton 1 : Voir tous les films
        jButton1 = new JButton("Voir tous les Films");
        styliserBouton(jButton1);
        jButton1.setBounds(125, 210, 250, 45);
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));
        bgPanel.add(jButton1);

        // Bouton 2 : Mes réservations
        btnReservations = new JButton("Mes réservations");
        styliserBouton(btnReservations);
        btnReservations.setBounds(125, 270, 250, 45);
        btnReservations.addActionListener(evt -> btnReservationsActionPerformed(evt));
        bgPanel.add(btnReservations);

        // Bouton 3 : Déconnexion
        jButton3 = new JButton("Déconnexion");
        styliserBoutonBlanc(jButton3);
        jButton3.setBounds(175, 350, 150, 35);
        jButton3.addActionListener(evt -> jButton3ActionPerformed(evt));
        bgPanel.add(jButton3);
    }

    private void styliserBouton(JButton btn) {
        btn.setBackground(new Color(180, 0, 0));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        new VoirFilmsFrame(clientConnecte).setVisible(true);
        this.dispose();
    }

    private void btnReservationsActionPerformed(java.awt.event.ActionEvent evt) {
        new MesReservationsFrame(clientConnecte).setVisible(true);
        this.dispose();
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment vous déconnecter ?",
                "Déconnexion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmation == JOptionPane.YES_OPTION) {
            this.dispose();
            new MainFrame2().setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AccueilClient().setVisible(true));
    }
}
