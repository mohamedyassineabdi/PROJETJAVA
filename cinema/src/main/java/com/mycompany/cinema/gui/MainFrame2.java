// Version améliorée de MainFrame2 avec redirection vers InscriptionFrame2
package com.mycompany.cinema.gui;

import com.mycompany.cinema.models.Client;
import com.mycompany.cinema.dao.ClientDAO;
import com.mycompany.cinema.models.Administrateur;
import com.mycompany.cinema.dao.AdministrateurDAO;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class MainFrame2 extends JFrame {

    private JTextField champEmail;
    private JPasswordField champMotDePasse;
    private JComboBox<String> comboBoxRole;
    private JCheckBox showPasswordCheckBox;
    private JButton boutonConnexion, boutonInscription, boutonQuitter;

    public MainFrame2() {
        setTitle("Billetterie Cinéma");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        ImageIcon icon = new ImageIcon(getClass().getResource("background_cinema.png"));
        Image img = icon.getImage();

        BackgroundPanel bgPanel = new BackgroundPanel(img);
        setContentPane(bgPanel);
        bgPanel.setLayout(null);

        int leftMargin = 80;
        int fieldWidth = 340;
        int y = 160;
        int spacing = 40;

        JLabel labelRole = new JLabel("Rôle:");
        styliserLabel(labelRole);
        labelRole.setBounds(leftMargin, y, 100, 25);
        bgPanel.add(labelRole);

        comboBoxRole = new JComboBox<>(new String[]{"Client", "Adminstrateur"});
        comboBoxRole.setBounds(leftMargin + 100, y, 200, 25);
        bgPanel.add(comboBoxRole);
        y += spacing;

        JLabel labelEmail = new JLabel("Email:");
        styliserLabel(labelEmail);
        labelEmail.setBounds(leftMargin, y, 100, 25);
        bgPanel.add(labelEmail);

        champEmail = new JTextField();
        champEmail.setBounds(leftMargin + 100, y, 200, 25);
        bgPanel.add(champEmail);
        y += spacing;

        JLabel labelPassword = new JLabel("Mot de passe:");
        styliserLabel(labelPassword);
        labelPassword.setBounds(leftMargin, y, 120, 25);
        bgPanel.add(labelPassword);

        champMotDePasse = new JPasswordField();
        champMotDePasse.setBounds(leftMargin + 120, y, 180, 25);
        bgPanel.add(champMotDePasse);
        y += spacing;

        showPasswordCheckBox = new JCheckBox("Afficher le mot de passe");
        showPasswordCheckBox.setForeground(Color.WHITE);
        showPasswordCheckBox.setOpaque(false);
        showPasswordCheckBox.setBounds(leftMargin + 100, y, 200, 20);
        showPasswordCheckBox.addActionListener(e -> {
            champMotDePasse.setEchoChar(showPasswordCheckBox.isSelected() ? (char) 0 : '•');
        });
        bgPanel.add(showPasswordCheckBox);
        y += spacing + 10;

        boutonConnexion = new JButton("Connexion");
        styleBouton(boutonConnexion);
        boutonConnexion.setBounds(50, y, 400, 40);
        boutonConnexion.addActionListener(e -> connecter());
        bgPanel.add(boutonConnexion);
        y += 60;

        boutonInscription = new JButton("Nouveau compte");
        styleBouton(boutonInscription);
        boutonInscription.setBounds(50, y, 400, 40);
        boutonInscription.addActionListener(e -> {
            new InscriptionFrame2().setVisible(true);
            dispose();
        });
        bgPanel.add(boutonInscription);
        y += 60;

        boutonQuitter = new JButton("Fermer l'application");
        boutonQuitter.setBounds(150, y, 200, 30);
        boutonQuitter.addActionListener(e -> quitterApplication());
        bgPanel.add(boutonQuitter);
    }

    private void styliserLabel(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void styleBouton(JButton btn) {
        btn.setBackground(new Color(200, 0, 0));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    }

    private void connecter() {
        String email = champEmail.getText().trim();
        String motDePasse = new String(champMotDePasse.getPassword()).trim();
        String role = comboBoxRole.getSelectedItem().toString();

        if (email.isEmpty() || motDePasse.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir votre email et votre mot de passe.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (role.equals("Client")) {
            ClientDAO clientDAO = new ClientDAO();
            Client client = clientDAO.authentifier(email, motDePasse);

            if (client != null) {
                JOptionPane.showMessageDialog(this, "Bienvenue " + client.getPrenom() + " !", "Connexion réussie", JOptionPane.INFORMATION_MESSAGE);
                new AccueilClient(client).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Email ou mot de passe incorrect.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        } else if (role.equals("Adminstrateur")) {
            AdministrateurDAO adminDAO = new AdministrateurDAO();
            Administrateur admin = adminDAO.authentifier(email, motDePasse);

            if (admin != null) {
                JOptionPane.showMessageDialog(this, "Bienvenue " + admin.getPrenom() + " !", "Connexion administrateur réussie", JOptionPane.INFORMATION_MESSAGE);
                new AcceuilAdmin().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Email ou mot de passe admin incorrect.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void quitterApplication() {
        int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir quitter ?", "Quitter", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new MainFrame2().setVisible(true));
    }
}