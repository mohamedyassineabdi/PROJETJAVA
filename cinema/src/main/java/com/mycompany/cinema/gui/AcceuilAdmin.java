package com.mycompany.cinema.gui;

import javax.swing.*;
import java.awt.*;

public class AcceuilAdmin extends JFrame {

    private JButton btnGestionFilms;
    private JButton btnGestionClients;
    private JButton btnGestionSeances;
    private JButton btnGestionSalles;
    private JButton btnGestionReservations;
    private JButton btnLogout;

    public AcceuilAdmin() {
        initComponents();
    }

    private void initComponents() {
        ImageIcon icon = new ImageIcon(getClass().getResource("background_cinema.png"));
        Image img = icon.getImage();
        BackgroundPanel bgPanel = new BackgroundPanel(img);
        setContentPane(bgPanel);
        bgPanel.setLayout(null);

        setTitle("Espace Administrateur");
        setSize(500, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);


        int yStart = 190;
        int spacing = 55;

        btnGestionFilms = new JButton("Consulter les Films");
        styliserBouton(btnGestionFilms);
        btnGestionFilms.setBounds(125, yStart, 250, 40);
        btnGestionFilms.addActionListener(e -> new GestionFilmsFrame().setVisible(true));
        bgPanel.add(btnGestionFilms);

        btnGestionClients = new JButton("Consulter les Clients");
        styliserBouton(btnGestionClients);
        btnGestionClients.setBounds(125, yStart += spacing, 250, 40);
        btnGestionClients.addActionListener(e -> new GestionClientsFrame().setVisible(true));
        bgPanel.add(btnGestionClients);

        btnGestionSalles = new JButton("Consulter les Salles");
        styliserBouton(btnGestionSalles);
        btnGestionSalles.setBounds(125, yStart += spacing, 250, 40);
        btnGestionSalles.addActionListener(e -> new GestionSallesFrame().setVisible(true));
        bgPanel.add(btnGestionSalles);

        btnGestionSeances = new JButton("Consulter les Séances");
        styliserBouton(btnGestionSeances);
        btnGestionSeances.setBounds(125, yStart += spacing, 250, 40);
        btnGestionSeances.addActionListener(e -> new GestionSeancesFrame().setVisible(true));
        bgPanel.add(btnGestionSeances);

        btnGestionReservations = new JButton("Consulter les Réservations");
        styliserBouton(btnGestionReservations);
        btnGestionReservations.setBounds(125, yStart += spacing, 250, 40);
        btnGestionReservations.addActionListener(e -> new GestionReservationsFrame().setVisible(true));
        bgPanel.add(btnGestionReservations);

        btnLogout = new JButton("Retour");
        styliserBoutonBlanc(btnLogout);
        btnLogout.setBounds(175, yStart += spacing + 10, 150, 35);
        btnLogout.addActionListener(e -> {
            dispose();
            new MainFrame2().setVisible(true);
        });
        bgPanel.add(btnLogout);
    }

    private void styliserBouton(JButton btn) {
        btn.setBackground(new Color(180, 0, 0));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AcceuilAdmin().setVisible(true));
    }
}
