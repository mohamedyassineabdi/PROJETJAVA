package com.mycompany.cinema.gui;

import com.mycompany.cinema.dao.ReservationDAO;
import com.mycompany.cinema.utils.PdfUtils;
import com.mycompany.cinema.dao.SalleDAO;
import com.mycompany.cinema.dao.SeanceDAO;
import com.mycompany.cinema.models.Reservation;
import com.mycompany.cinema.models.Salle;
import com.mycompany.cinema.models.Seance;
import com.mycompany.cinema.utils.PdfUtils;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ReservationSeanceFrame extends javax.swing.JFrame {

    private String titreFilm;

    private int idClient;

public ReservationSeanceFrame(String titreFilm, int idClient) {
    this.titreFilm = titreFilm;
    this.idClient = idClient;
    initComponents();
    labelTitre.setText("Séances disponibles pour le film : " + titreFilm);

    chargerSalles();
    chargerDates();
    chargerSeances();

    boutonFiltrer.addActionListener(e -> chargerSeances());
    boutonRetour.addActionListener(e -> retourner());
    boutonReserver.addActionListener(e -> reserverSeance());
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
        if (selected != null && selected) {
            selectedRow = i;
            break;
        }
    }

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Veuillez sélectionner une séance.");
        return;
    }

    // Lire le nombre de places
    int nbPlaces;
    try {
        nbPlaces = Integer.parseInt(champNbPlaces.getText().trim());
        if (nbPlaces <= 0) throw new NumberFormatException();
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre de places valide.");
        return;
    }

    int placesDisponibles = (int) model.getValueAt(selectedRow, 4);
    if (nbPlaces > placesDisponibles) {
        JOptionPane.showMessageDialog(this, "Nombre de places insuffisant. Il reste " + placesDisponibles + " places.");
        return;
    }

    // Infos pour retrouver la séance
    String date = model.getValueAt(selectedRow, 0).toString();
    String heure = model.getValueAt(selectedRow, 1).toString();
    String salleNom = model.getValueAt(selectedRow, 2).toString();

    // Récupérer la séance correspondante
    SeanceDAO daoSeance = new SeanceDAO();
    List<Seance> seances = daoSeance.getSeancesParFilmEtFiltres(titreFilm, date, salleNom);
    Seance seance = seances.stream()
        .filter(s -> s.getHeure().equals(heure))
        .findFirst()
        .orElse(null);

    if (seance == null) {
        JOptionPane.showMessageDialog(this, "Erreur lors de la récupération de la séance.");
        return;
    }

    // Décrémenter les places
    daoSeance.decrementerPlaces(seance.getIdSeance(), nbPlaces);

    // ⚠️ ID client à remplacer dynamiquement si login (ici hardcodé à 1)
    

    // Créer réservation en BDD
    Reservation reservation = new Reservation(idClient, seance.getIdSeance(), nbPlaces, "non payé");

    ReservationDAO daoReservation = new ReservationDAO();
    daoReservation.ajouterReservation(reservation);

    // Afficher la page de paiement
    this.dispose();
    new PaiementFrame(reservation).setVisible(true);
}


    private void retourner() {
        this.dispose();
        new VoirFilmsFrame(idClient).setVisible(true); // ✅

    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        labelTitre = new javax.swing.JLabel();
        labelSalle = new javax.swing.JLabel();
        labelDate = new javax.swing.JLabel();
        comboSalle = new javax.swing.JComboBox<>();
        comboDate = new javax.swing.JComboBox<>();
        boutonFiltrer = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableSeances = new javax.swing.JTable();
        boutonReserver = new javax.swing.JButton();
        boutonRetour = new javax.swing.JButton();
        labelNbPlaces = new javax.swing.JLabel();
champNbPlaces = new javax.swing.JTextField();

        labelNbPlaces.setText("Nombre de places :");
champNbPlaces.setColumns(5); // pour une taille raisonnable

        

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        labelTitre.setFont(new java.awt.Font("Segoe UI", 1, 16));
        labelTitre.setText("Séances disponibles pour le film :");

        labelSalle.setText("Filtrer par salle :");
        labelDate.setText("Filtrer par date :");

        boutonFiltrer.setText("Filtrer");
        
        boutonReserver.setText("Réserver");
        boutonRetour.setText("Retour aux films");

        tableSeances.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "Date", "Heure", "Salle", "Langue", "Places disponibles", "Sélectionner" }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class,
                java.lang.String.class, java.lang.Integer.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableSeances);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelTitre)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelSalle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comboSalle, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(labelDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comboDate, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(boutonFiltrer))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
    .addGap(20, 20, 20)
    .addComponent(labelNbPlaces)
    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
    .addComponent(champNbPlaces, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
)

                    .addGroup(layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(boutonReserver, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(boutonRetour, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(labelTitre)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelSalle)
                    .addComponent(comboSalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelDate)
                    .addComponent(comboDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boutonFiltrer))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
    .addComponent(labelNbPlaces)
    .addComponent(champNbPlaces, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
)

                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boutonReserver)
                    .addComponent(boutonRetour))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
    }

    // Variables declaration - do not modify
    private javax.swing.JButton boutonFiltrer;
    private javax.swing.JButton boutonReserver;
    private javax.swing.JButton boutonRetour;
    private javax.swing.JComboBox<String> comboDate;
    private javax.swing.JComboBox<String> comboSalle;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelDate;
    private javax.swing.JLabel labelSalle;
    private javax.swing.JLabel labelTitre;
    private javax.swing.JTable tableSeances;
    private javax.swing.JLabel labelNbPlaces;
    private javax.swing.JTextField champNbPlaces;

    // End of variables declaration
}
