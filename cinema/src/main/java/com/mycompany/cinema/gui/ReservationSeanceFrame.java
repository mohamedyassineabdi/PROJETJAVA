// Version optimisée avec background image et layout absolu
package com.mycompany.cinema.gui;

import com.mycompany.cinema.dao.*;
import com.mycompany.cinema.models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReservationSeanceFrame extends JFrame {

    private String titreFilm;
    private Client clientConnecte;
    private JLabel labelTitre, labelSalle, labelDate, labelNbPlaces;
    private JComboBox<String> comboSalle, comboDate;
    private JButton boutonFiltrer, boutonReserver, boutonRetour;
    private JTable tableSeances;
    private JTextField champNbPlaces;
    private JScrollPane jScrollPane1;

    public ReservationSeanceFrame(String titreFilm, Client client) {
        this.titreFilm = titreFilm;
        this.clientConnecte = client;
        initComponents();
        chargerSalles();
        chargerDates();
        chargerSeances();

        boutonFiltrer.addActionListener(e -> chargerSeances());
        boutonRetour.addActionListener(e -> retourner());
        boutonReserver.addActionListener(e -> reserverSeance());
    }

    private void styleBouton(JButton btn) {
        btn.setBackground(new Color(180, 0, 0));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
    }

    private void initComponents() {
        setTitle("Réserver une séance");
        setSize(850, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        ImageIcon icon = new ImageIcon(getClass().getResource("background_cinema.png"));
        Image img = icon.getImage();
        BackgroundPanel bgPanel = new BackgroundPanel(img);
        setContentPane(bgPanel);
        bgPanel.setLayout(null);

        int baseY = 220;

        labelTitre = new JLabel("Séances disponibles pour le film : " + titreFilm);
        labelTitre.setForeground(Color.WHITE);
        labelTitre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelTitre.setBounds(40, baseY, 600, 30);
        bgPanel.add(labelTitre);

        baseY += 40;

        labelSalle = new JLabel("Filtrer par salle :");
        labelSalle.setForeground(Color.WHITE);
        labelSalle.setBounds(40, baseY, 120, 25);
        bgPanel.add(labelSalle);

        comboSalle = new JComboBox<>();
        comboSalle.setBounds(160, baseY, 160, 25);
        bgPanel.add(comboSalle);

        labelDate = new JLabel("Filtrer par date :");
        labelDate.setForeground(Color.WHITE);
        labelDate.setBounds(340, baseY, 120, 25);
        bgPanel.add(labelDate);

        comboDate = new JComboBox<>();
        comboDate.setBounds(460, baseY, 160, 25);
        bgPanel.add(comboDate);

        boutonFiltrer = new JButton("Filtrer");
        styleBouton(boutonFiltrer);
        boutonFiltrer.setBounds(640, baseY, 100, 25);
        bgPanel.add(boutonFiltrer);

        baseY += 40;

        tableSeances = new JTable();
        tableSeances.setRowHeight(28);
        tableSeances.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableSeances.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableSeances.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Date", "Heure", "Salle", "Langue", "Places disponibles", "Sélectionner"}
        ) {
            Class[] types = new Class[]{
                    String.class, String.class, String.class, String.class, Integer.class, Boolean.class
            };
            boolean[] canEdit = new boolean[]{false, false, false, false, false, true};

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        jScrollPane1 = new JScrollPane(tableSeances);
        jScrollPane1.setBounds(40, baseY, 760, 220);
        bgPanel.add(jScrollPane1);

        baseY += 240;

        labelNbPlaces = new JLabel("Nombre de places :");
        labelNbPlaces.setForeground(Color.WHITE);
        labelNbPlaces.setBounds(40, baseY, 140, 25);
        bgPanel.add(labelNbPlaces);

        champNbPlaces = new JTextField();
        champNbPlaces.setBounds(180, baseY, 50, 25);
        bgPanel.add(champNbPlaces);

        baseY += 40;

        boutonReserver = new JButton("Réserver");
        styleBouton(boutonReserver);
        boutonReserver.setBounds(200, baseY, 160, 40);
        bgPanel.add(boutonReserver);

        boutonRetour = new JButton("Retour aux films");
        styleBouton(boutonRetour);
        boutonRetour.setBounds(440, baseY, 160, 40);
        bgPanel.add(boutonRetour);
    }

    private void chargerSalles() {
        SalleDAO dao = new SalleDAO();
        comboSalle.removeAllItems();
        comboSalle.addItem("Toutes les salles");
        for (Salle s : dao.getAllSalles()) {
            comboSalle.addItem(s.getNom());
        }
    }

    private void chargerDates() {
        comboDate.removeAllItems();
        comboDate.addItem("Toutes les dates");
        comboDate.addItem("2024-05-01");
        comboDate.addItem("2024-05-02");
        comboDate.addItem("2024-05-03");
        comboDate.addItem("2024-05-04");
    }

    private void chargerSeances() {
        SeanceDAO dao = new SeanceDAO();
        String date = comboDate.getSelectedItem().toString();
        String salle = comboSalle.getSelectedItem().toString();

        if (date.equals("Toutes les dates")) date = "";
        if (salle.equals("Toutes les salles")) salle = "";

        List<Seance> seances = dao.getSeancesParFilmEtFiltres(titreFilm, date, salle);
        DefaultTableModel model = (DefaultTableModel) tableSeances.getModel();
        model.setRowCount(0);

        for (Seance s : seances) {
            model.addRow(new Object[]{
                    s.getDateSeance(), s.getHeure(), s.getNomSalle(), s.getLangue(), s.getPlacesDisponibles(), false
            });
        }
    }

    private void reserverSeance() {
        DefaultTableModel model = (DefaultTableModel) tableSeances.getModel();
        int selectedRow = -1;

        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean selected = (Boolean) model.getValueAt(i, 5);
            if (Boolean.TRUE.equals(selected)) {
                selectedRow = i;
                break;
            }
        }

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "❗ Veuillez sélectionner une séance.");
            return;
        }

        int nbPlaces;
        try {
            nbPlaces = Integer.parseInt(champNbPlaces.getText().trim());
            if (nbPlaces <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "❗ Veuillez entrer un nombre de places valide (> 0).");
            return;
        }

        int placesDisponibles = (int) model.getValueAt(selectedRow, 4);
        if (nbPlaces > placesDisponibles) {
            JOptionPane.showMessageDialog(this,
                    "❌ Places insuffisantes. Il reste " + placesDisponibles + " place(s) disponible(s).");
            return;
        }

        String date = model.getValueAt(selectedRow, 0).toString();
        String heure = model.getValueAt(selectedRow, 1).toString();
        String salleNom = model.getValueAt(selectedRow, 2).toString();

        SeanceDAO seanceDAO = new SeanceDAO();
        List<Seance> seances = seanceDAO.getSeancesParFilmEtFiltres(titreFilm, date, salleNom);

        Seance seance = seances.stream()
                .filter(s -> s.getHeure().equals(heure))
                .findFirst()
                .orElse(null);

        if (seance == null) {
            JOptionPane.showMessageDialog(this, "❌ Séance introuvable.");
            return;
        }

        seanceDAO.decrementerPlaces(seance.getIdSeance(), nbPlaces);
        model.setValueAt(placesDisponibles - nbPlaces, selectedRow, 4);

        ReservationDAO reservationDAO = new ReservationDAO();
        Reservation reservation = reservationDAO.ajouterReservation(
                clientConnecte.getIdClient(),
                seance.getIdSeance(),
                nbPlaces,
                "non payée"
        );

        if (reservation != null) {
            double montant = nbPlaces * 18;
            new PaiementDAO().ajouterPaiement(montant, "en attente", reservation.getIdReservation());
            new BilletDAO().creerBilletsPourReservation(reservation.getIdReservation(), nbPlaces);

            JOptionPane.showMessageDialog(this, "Réservation enregistrée. Passez au paiement.");
            this.dispose();
            new PaiementFrame(clientConnecte, reservation).setVisible(true);
        }
    }

    private void retourner() {
        this.dispose();
        new VoirFilmsFrame(clientConnecte).setVisible(true);
    }
}