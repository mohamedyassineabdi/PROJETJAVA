package com.mycompany.cinema.gui;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import com.mycompany.cinema.models.*;
import com.mycompany.cinema.dao.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import com.mycompany.cinema.gui.ReservationSeanceFrame;




public class VoirFilmsFrame extends javax.swing.JFrame {
    
    private int idClient;


    private DefaultTableModel model; 
    private JButton boutonReserver;
    private JButton boutonRetour;
    private JButton boutonRechercher;
    private JTextField champRecherche;
    private JLabel labelRecherche;
    private com.mycompany.cinema.models.Client clientConnecte; // Ajout du client connecté

    public VoirFilmsFrame(int idClient) {
    this.idClient = idClient;
    
        
        initComponents();
        initialiserTable(); 
        chargerFilmsDepuisBD();
        ajouterListeners();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        champRecherche = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableFilms = new javax.swing.JTable();
        boutonReserver = new javax.swing.JButton();
        boutonRetour = new javax.swing.JButton();
        boutonRechercher = new javax.swing.JButton();
        labelRecherche = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        labelRecherche.setText("Rechercher un film:");
        boutonRechercher.setText("Rechercher");
        boutonRetour.setText("Retour Accueil");
        boutonReserver.setText("Réserver");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nom", "Genre" }));

        jLabel1.setText("Filtrer par: ");

        jScrollPane1.setViewportView(tableFilms);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(labelRecherche)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(champRecherche, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(boutonRechercher))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(boutonReserver, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(boutonRetour, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelRecherche)
                    .addComponent(champRecherche, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boutonRechercher))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox1))
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boutonReserver)
                    .addComponent(boutonRetour))
                .addGap(20, 20, 20))
        );

        boutonReserver.addActionListener(e -> reserverFilm());

        pack();
    }

    private void initialiserTable() {
        model = new DefaultTableModel(new Object[][]{}, new String[]{"Sélectionner", "Titre du film", "Genre", "Durée"}) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int columnIndex) {
                return columnIndex == 0;
            }
        };
        tableFilms.setModel(model);
        tableFilms.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

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
            Object[] row = {
                false,
                film.getTitre(),
                film.getGenre(),
                film.getDuree() + " min"
            };
            model.addRow(row);
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
        List<Film> films;

        if ("Nom".equals(critere)) {
            films = filmDAO.getAllFilmsOrderBy("titre");
        } else if ("Genre".equals(critere)) {
            films = filmDAO.getAllFilmsOrderBy("genre");
        } else {
            films = filmDAO.getAllFilms();
        }

        model.setRowCount(0);

        for (Film film : films) {
            Object[] row = {
                false,
                film.getTitre(),
                film.getGenre(),
                film.getDuree() + " min"
            };
            model.addRow(row);
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
                Object[] row = {
                    false,
                    film.getTitre(),
                    film.getGenre(),
                    film.getDuree() + " min"
                };
                model.addRow(row);
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
            new ReservationSeanceFrame(titreFilm, idClient).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Veuillez sélectionner un film avant de réserver.");
        }
    }

    private void retournerAccueil() {
        this.dispose();
        new AccueilClient(clientConnecte).setVisible(true);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            Client client = ...;
new VoirFilmsFrame(client.getIdClient()).setVisible(true);  // ✅

 // Passez un client valide ici dans la vraie utilisation
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableFilms;
    // End of variables declaration                   
}
