package com.mycompany.cinema.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class GestionClientsFrame extends JFrame {

    private JTable tableClients;
    private DefaultTableModel model;

    public GestionClientsFrame() {
        setTitle("Gestion des Clients");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        chargerClientsDepuisBD();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());

        // Table
        model = new DefaultTableModel(new String[]{"ID", "Nom", "Prénom", "Email", "Mot de passe"}, 0);
        tableClients = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableClients);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Boutons
        JPanel panelBoutons = new JPanel();
        JButton btnAjouter = new JButton("Ajouter Client");
        JButton btnModifier = new JButton("Modifier Client");
        JButton btnSupprimer = new JButton("Supprimer Client");
        JButton btnRetour = new JButton("Retour à l'accueil");
        

        panelBoutons.add(btnAjouter);

        panelBoutons.add(btnModifier);

        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnRetour);
        panel.add(panelBoutons, BorderLayout.SOUTH);

        add(panel);
        
        btnAjouter.addActionListener(e -> {
    new AjouterClientFrame(this::chargerClientsDepuisBD).setVisible(true);
});

        btnModifier.addActionListener(e -> {
    int selectedRow = tableClients.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client à modifier.");
        return;
    }

    int id = (int) model.getValueAt(selectedRow, 0);
    String nom = (String) model.getValueAt(selectedRow, 1);
    String prenom = (String) model.getValueAt(selectedRow, 2);
    String email = (String) model.getValueAt(selectedRow, 3);
    String mdp = (String) model.getValueAt(selectedRow, 4);

    new ModifierClientFrame(id, nom, prenom, email, mdp, this::chargerClientsDepuisBD).setVisible(true);
});


        // Action : Supprimer
        btnSupprimer.addActionListener(e -> {
            int selectedRow = tableClients.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client à supprimer.");
                return;
            }

            int idClient = (int) model.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Confirmer la suppression du client ?", "Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billetteriecinema", "root", "");
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM clients WHERE id_client = ?");
                    ps.setInt(1, idClient);
                    ps.executeUpdate();
                    conn.close();

                    chargerClientsDepuisBD(); // Refresh
                    JOptionPane.showMessageDialog(this, "Client supprimé avec succès.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
                }
            }
        });

        // Action : Retour à l'accueil
        btnRetour.addActionListener(e -> {
            new AcceuilAdmin().setVisible(true); // remplace si le nom est différent
            dispose();
        });
    }

    private void chargerClientsDepuisBD() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billetteriecinema", "root", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM clients");

            model.setRowCount(0);

            while (rs.next()) {
                int id = rs.getInt("id_client");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String mdp = rs.getString("mot_de_passe");

                model.addRow(new Object[]{id, nom, prenom, email, mdp});
            }

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de chargement des clients.");
        }
    }
}
