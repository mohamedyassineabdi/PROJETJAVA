package com.mycompany.cinema.gui;

import com.mycompany.cinema.models.Client;
import com.mycompany.cinema.models.Reservation;
import com.mycompany.cinema.dao.ReservationDAO;
import com.mycompany.cinema.dao.PaiementDAO;

import javax.swing.*;
import java.awt.*;

public class PaiementFrame extends JFrame {

    private Client client;
    private Reservation reservation;
    private JLabel labelTotal;
    private JTextField champNomCarte, champNumeroCarte, champDateExpiration, champCVV;

    public PaiementFrame(Client client, Reservation reservation) {
        this.client = client;
        this.reservation = reservation;

        setTitle("Paiement");
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
        int total = reservation.getNbPlaces() * 18;

        ImageIcon icon = new ImageIcon(getClass().getResource("background_cinema.png"));
        Image img = icon.getImage();
        BackgroundPanel bgPanel = new BackgroundPanel(img);
        setContentPane(bgPanel);
        bgPanel.setLayout(null);

        JLabel labelTitre = new JLabel("Paiement de la réservation");
        labelTitre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelTitre.setForeground(Color.WHITE);
        labelTitre.setBounds(180, 180, 300, 30);
        bgPanel.add(labelTitre);

        labelTotal = new JLabel("Total à payer : " + total + " Dinars");
        labelTotal.setForeground(Color.WHITE);
        labelTotal.setBounds(220, 220, 250, 25);
        bgPanel.add(labelTotal);

        int labelX = 120;
        int fieldX = 280;
        int yStart = 270;
        int lineHeight = 40;

        JLabel labelNom = new JLabel("Nom sur la carte :");
        labelNom.setForeground(Color.WHITE);
        labelNom.setBounds(labelX, yStart, 150, 25);
        bgPanel.add(labelNom);

        champNomCarte = new JTextField();
        champNomCarte.setBounds(fieldX, yStart, 200, 25);
        bgPanel.add(champNomCarte);

        yStart += lineHeight;
        JLabel labelNum = new JLabel("Numéro de carte :");
        labelNum.setForeground(Color.WHITE);
        labelNum.setBounds(labelX, yStart, 150, 25);
        bgPanel.add(labelNum);

        champNumeroCarte = new JTextField();
        champNumeroCarte.setBounds(fieldX, yStart, 200, 25);
        bgPanel.add(champNumeroCarte);

        yStart += lineHeight;
        JLabel labelDate = new JLabel("Date d'expiration :");
        labelDate.setForeground(Color.WHITE);
        labelDate.setBounds(labelX, yStart, 150, 25);
        bgPanel.add(labelDate);

        champDateExpiration = new JTextField();
        champDateExpiration.setBounds(fieldX, yStart, 200, 25);
        bgPanel.add(champDateExpiration);

        yStart += lineHeight;
        JLabel labelCVV = new JLabel("Code CVV :");
        labelCVV.setForeground(Color.WHITE);
        labelCVV.setBounds(labelX, yStart, 150, 25);
        bgPanel.add(labelCVV);

        champCVV = new JTextField();
        champCVV.setBounds(fieldX, yStart, 200, 25);
        bgPanel.add(champCVV);

        yStart += lineHeight + 20;

        JButton boutonPayer = new JButton("Payer");
        styleBouton(boutonPayer);
        boutonPayer.setBounds(150, yStart, 120, 40);
        bgPanel.add(boutonPayer);

        JButton boutonRetour = new JButton("Retour à l'accueil");
        styleBouton(boutonRetour);
        boutonRetour.setBounds(300, yStart, 180, 40);
        bgPanel.add(boutonRetour);

        boutonPayer.addActionListener(e -> validerPaiement());
        boutonRetour.addActionListener(e -> {
            this.dispose();
            new AccueilClient(client).setVisible(true);
        });
    }

    private void validerPaiement() {
        String nom = champNomCarte.getText().trim();
        String numero = champNumeroCarte.getText().trim();
        String expiration = champDateExpiration.getText().trim();
        String cvv = champCVV.getText().trim();

        if (nom.isEmpty() || numero.isEmpty() || expiration.isEmpty() || cvv.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "❗ Veuillez remplir tous les champs de paiement.",
                    "Champs manquants",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!numero.matches("\\d{16}")) {
            JOptionPane.showMessageDialog(this, "❌ Le numéro de carte doit contenir 16 chiffres.");
            return;
        }
        if (!cvv.matches("\\d{3}")) {
            JOptionPane.showMessageDialog(this, "❌ Le code CVV doit contenir 3 chiffres.");
            return;
        }

        new ReservationDAO().validerPaiement(reservation.getIdReservation());
        new PaiementDAO().validerPaiement(reservation.getIdReservation());

        JOptionPane.showMessageDialog(this,
                "✅ Paiement effectué avec succès !",
                "Confirmation",
                JOptionPane.INFORMATION_MESSAGE);

        this.dispose();
        new VoirFilmsFrame(client).setVisible(true);
    }
}