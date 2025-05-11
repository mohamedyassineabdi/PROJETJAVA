package com.mycompany.cinema.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.mycompany.cinema.models.*;
import com.mycompany.cinema.dao.*;

public class VoirFilmsFrame extends JFrame {

    private Client clientConnecte;
    private DefaultTableModel model;
    private JButton boutonReserver;
    private JButton boutonRetour;
    private JButton boutonRechercher;
    private JTextField champRecherche;
    private JLabel labelRecherche;
    private JComboBox<String> jComboBox1;
    private JLabel jLabel1;
    private JTable tableFilms;

    public VoirFilmsFrame(Client client) {
        this.clientConnecte = client;
        initComponents();
        initialiserTable();
        chargerFilmsDepuisBD();
        ajouterListeners();
    }

    private void initComponents() {
        ImageIcon icon = new ImageIcon(getClass().getResource("background_cinema.png"));
        Image img = icon.getImage();
        BackgroundPanel bgPanel = new BackgroundPanel(img);
        setContentPane(bgPanel);
        bgPanel.setLayout(null);

        setTitle("Liste des Films");
        setSize(700, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // === Composants alignés sous le titre/logo du fond ===
        int baseY = 250; // Décalé davantage sous le titre/logo

        labelRecherche = new JLabel("Rechercher un film:");
        labelRecherche.setForeground(Color.WHITE);
        labelRecherche.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelRecherche.setBounds(80, baseY, 160, 25);
        bgPanel.add(labelRecherche);

        champRecherche = new JTextField();
        champRecherche.setBounds(250, baseY, 200, 25);
        bgPanel.add(champRecherche);

        boutonRechercher = new JButton("Rechercher");
        styleBouton(boutonRechercher);
        boutonRechercher.setBounds(470, baseY, 120, 25);
        bgPanel.add(boutonRechercher);

        baseY += 40;

        jLabel1 = new JLabel("Filtrer par:");
        jLabel1.setForeground(Color.WHITE);
        jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jLabel1.setBounds(80, baseY, 100, 25);
        bgPanel.add(jLabel1);

        jComboBox1 = new JComboBox<>(new String[]{"Nom", "Genre"});
        jComboBox1.setBounds(250, baseY, 200, 25);
        bgPanel.add(jComboBox1);

        baseY += 50;

        tableFilms = new JTable();
        tableFilms.setRowHeight(28);
        tableFilms.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableFilms.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        JScrollPane scrollPane = new JScrollPane(tableFilms);
        scrollPane.setBounds(80, baseY, 540, 330);
        bgPanel.add(scrollPane);

        baseY += 360;

        boutonReserver = new JButton("Réserver");
        styleBouton(boutonReserver);
        boutonReserver.setBounds(150, baseY, 160, 40);
        bgPanel.add(boutonReserver);

        boutonRetour = new JButton("Retour Accueil");
        styleBouton(boutonRetour);
        boutonRetour.setBounds(370, baseY, 160, 40);
        bgPanel.add(boutonRetour);

        boutonReserver.addActionListener(e -> reserverFilm());
    }

    private void styleBouton(JButton btn) {
        btn.setBackground(new Color(180, 0, 0));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
    }

    private void initialiserTable() {
        model = new DefaultTableModel(new Object[][]{}, new String[]{"Sélectionner", "Titre du film", "Genre", "Durée"}) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int columnIndex) {
                return columnIndex == 0;
            }
        };
        tableFilms.setModel(model);
        tableFilms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableFilms.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 0) {
                int changedRow = e.getFirstRow();
                Boolean checked = (Boolean) tableFilms.getValueAt(changedRow, 0);
                if (checked) {
                    for (int i = 0; i < tableFilms.getRowCount(); i++) {
                        if (i != changedRow) {
                            tableFilms.setValueAt(false, i, 0);
                        }
                    }
                }
            }
        });
    }

    private void chargerFilmsDepuisBD() {
        FilmDAO filmDAO = new FilmDAO();
        List<Film> films = filmDAO.getAllFilms();
        model.setRowCount(0);
        for (Film film : films) {
            model.addRow(new Object[]{false, film.getTitre(), film.getGenre(), film.getDuree() + " min"});
        }
    }

    private void ajouterListeners() {
        boutonRetour.addActionListener(e -> retournerAccueil());
        boutonRechercher.addActionListener(e -> rechercherFilm());
        jComboBox1.addActionListener(e -> trierFilms());
    }

    private void trierFilms() {
        String critere = (String) jComboBox1.getSelectedItem();
        FilmDAO filmDAO = new FilmDAO();
        List<Film> films = critere.equals("Nom") ? filmDAO.getAllFilmsOrderBy("titre") : filmDAO.getAllFilmsOrderBy("genre");
        model.setRowCount(0);
        for (Film film : films) {
            model.addRow(new Object[]{false, film.getTitre(), film.getGenre(), film.getDuree() + " min"});
        }
    }

    private void rechercherFilm() {
        String motCle = champRecherche.getText().trim();
        if (motCle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❗ Veuillez entrer un titre pour rechercher.");
            return;
        }
        FilmDAO filmDAO = new FilmDAO();
        List<Film> films = filmDAO.getFilmsParTitre(motCle);
        model.setRowCount(0);
        if (films.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Aucun film trouvé pour: " + motCle);
        } else {
            for (Film film : films) {
                model.addRow(new Object[]{false, film.getTitre(), film.getGenre(), film.getDuree() + " min"});
            }
        }
    }

    private void reserverFilm() {
        int selectedRow = -1;
        for (int i = 0; i < tableFilms.getRowCount(); i++) {
            if ((Boolean) tableFilms.getValueAt(i, 0)) {
                selectedRow = i;
                break;
            }
        }
        if (selectedRow != -1) {
            String titreFilm = (String) tableFilms.getValueAt(selectedRow, 1);
            new ReservationSeanceFrame(titreFilm, clientConnecte).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Veuillez sélectionner un film avant de réserver.");
        }
    }

    private void retournerAccueil() {
        this.dispose();
        new AccueilClient(clientConnecte).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientDAO clientDAO = new ClientDAO();
            Client client = clientDAO.authentifier("ahmed@mail.com", "azerty");
            if (client != null) {
                new VoirFilmsFrame(client).setVisible(true);
            } else {
                System.out.println("Client introuvable. Vérifiez l'email ou le mot de passe.");
            }
        });
    }
}