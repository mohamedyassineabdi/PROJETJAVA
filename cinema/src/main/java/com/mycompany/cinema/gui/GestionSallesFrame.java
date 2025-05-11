package com.mycompany.cinema.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class GestionSallesFrame extends JFrame {

    private JTable tableSalles;
    private DefaultTableModel model;

    public GestionSallesFrame() {
        setTitle("Gestion des Salles");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        chargerSallesDepuisBD();
    }

    private void initUI() {
        ImageIcon icon = new ImageIcon(getClass().getResource("background_cinema.png"));
        Image img = icon.getImage();
        BackgroundPanel bgPanel = new BackgroundPanel(img);
        setContentPane(bgPanel);
        bgPanel.setLayout(null);

        model = new DefaultTableModel(new String[]{"ID", "Nom", "Capacité"}, 0);
        tableSalles = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableSalles);
        scrollPane.setBounds(50, 150, 700, 250);
        bgPanel.add(scrollPane);

        JButton btnAjouter = new JButton("Ajouter Salle");
        JButton btnModifier = new JButton("Modifier Salle");
        JButton btnSupprimer = new JButton("Supprimer Salle");
        JButton btnRetour = new JButton("Retour à l'accueil");

        styliserBouton(btnAjouter);
        styliserBouton(btnModifier);
        styliserBouton(btnSupprimer);
        styliserBoutonBlanc(btnRetour);

        btnAjouter.setBounds(80, 420, 160, 40);
        btnModifier.setBounds(250, 420, 160, 40);
        btnSupprimer.setBounds(420, 420, 160, 40);
        btnRetour.setBounds(590, 420, 160, 40);

        bgPanel.add(btnAjouter);
        bgPanel.add(btnModifier);
        bgPanel.add(btnSupprimer);
        bgPanel.add(btnRetour);

        btnAjouter.addActionListener(e -> new AjouterSalleFrame(this::chargerSallesDepuisBD).setVisible(true));

        btnModifier.addActionListener(e -> {
            int row = tableSalles.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez une salle à modifier.");
                return;
            }
            int id = (int) model.getValueAt(row, 0);
            String nom = (String) model.getValueAt(row, 1);
            int capacite = (int) model.getValueAt(row, 2);
            new ModifierSalleFrame(id, nom, capacite, this::chargerSallesDepuisBD).setVisible(true);
        });

        btnSupprimer.addActionListener(e -> {
            int row = tableSalles.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez une salle à supprimer.");
                return;
            }

            int id = (int) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billetteriecinema", "root", "");
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM salles WHERE id_salle = ?");
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    conn.close();
                    chargerSallesDepuisBD();
                    JOptionPane.showMessageDialog(this, "Salle supprimée.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
                }
            }
        });

        btnRetour.addActionListener(e -> {
            new AcceuilAdmin().setVisible(true);
            dispose();
        });
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

    private void chargerSallesDepuisBD() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billetteriecinema", "root", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM salles");

            model.setRowCount(0);
            while (rs.next()) {
                int id = rs.getInt("id_salle");
                String nom = rs.getString("nom");
                int capacite = rs.getInt("capacite");
                model.addRow(new Object[]{id, nom, capacite});
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de chargement des salles.");
        }
    }
}