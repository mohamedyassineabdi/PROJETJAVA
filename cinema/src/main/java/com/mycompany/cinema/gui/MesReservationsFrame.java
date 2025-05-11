// Code optimisé de MesReservationsFrame avec design harmonisé
package com.mycompany.cinema.gui;

import com.mycompany.cinema.dao.FilmDAO;
import com.mycompany.cinema.dao.ReservationDAO;
import com.mycompany.cinema.dao.SeanceDAO;
import com.mycompany.cinema.models.*;
import com.mycompany.cinema.utils.PdfUtils;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class MesReservationsFrame extends JFrame {

    private Client client;
    private JTable tableReservations;
    private DefaultTableModel model;

    public MesReservationsFrame(Client client) {
        this.client = client;
        setTitle("Mes Réservations");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initUI();
        chargerReservations();
    }

    private void initUI() {
        ImageIcon icon = new ImageIcon(getClass().getResource("background_cinema.png"));
        Image img = icon.getImage();
        BackgroundPanel bgPanel = new BackgroundPanel(img);
        setContentPane(bgPanel);
        bgPanel.setLayout(null);

        JLabel titre = new JLabel("Mes Réservations");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titre.setForeground(Color.WHITE);
        titre.setBounds(400, 180, 300, 30);
        bgPanel.add(titre);

        model = new DefaultTableModel(new Object[][]{}, new String[]{
            "ID", "Date", "N° Séance", "Places", "Statut", "Montant", "Détails", "Annuler", "Payer"
        }) {
            public boolean isCellEditable(int row, int column) {
                return column == 6 || column == 7 || column == 8;
            }
        };

        tableReservations = new JTable(model);
        tableReservations.setRowHeight(30);
        tableReservations.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableReservations.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        tableReservations.getColumn("Détails").setCellRenderer(new ButtonRenderer());
        tableReservations.getColumn("Détails").setCellEditor(new ButtonEditor(new JCheckBox(), "details"));
        tableReservations.getColumn("Annuler").setCellRenderer(new ButtonRenderer());
        tableReservations.getColumn("Annuler").setCellEditor(new ButtonEditor(new JCheckBox(), "annuler"));
        tableReservations.getColumn("Payer").setCellRenderer(new ButtonRenderer());
        tableReservations.getColumn("Payer").setCellEditor(new ButtonEditor(new JCheckBox(), "payer"));

        tableReservations.getColumnModel().getColumn(0).setMinWidth(0);
        tableReservations.getColumnModel().getColumn(0).setMaxWidth(0);

        JScrollPane scrollPane = new JScrollPane(tableReservations);
        scrollPane.setBounds(50, 230, 900, 260);
        bgPanel.add(scrollPane);

        JButton boutonRetour = new JButton("Retour à l'accueil");
        boutonRetour.setBounds(400, 510, 200, 40);
        boutonRetour.setBackground(new Color(180, 0, 0));
        boutonRetour.setForeground(Color.WHITE);
        boutonRetour.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boutonRetour.setFocusPainted(false);
        bgPanel.add(boutonRetour);

        boutonRetour.addActionListener(e -> {
            this.dispose();
            new AccueilClient(client).setVisible(true);
        });
    }

    private void chargerReservations() {
        ReservationDAO dao = new ReservationDAO();
        List<Reservation> reservations = dao.getReservationsParClient(client.getIdClient());

        model.setRowCount(0);
        for (Reservation r : reservations) {
            model.addRow(new Object[]{
                r.getIdReservation(),
                r.getDateReservation(),
                r.getIdSeance(),
                r.getNbPlaces(),
                r.getStatut(),
                (r.getNbPlaces() * 18) + " DT",
                "Détails",
                r.getStatut().equals("non payée") ? "Annuler" : "",
                r.getStatut().equals("non payée") ? "Payer" : ""
            });
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private String actionType;
        private JButton button;
        private String label;
        private boolean clicked;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox, String actionType) {
            super(checkBox);
            this.actionType = actionType;
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(new Color(180, 0, 0));
            button.setForeground(Color.WHITE);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                     int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            clicked = true;
            selectedRow = row;
            return button;
        }

        public Object getCellEditorValue() {
            if (clicked) {
                int idReservation = (int) model.getValueAt(selectedRow, 0);
                ReservationDAO dao = new ReservationDAO();
                Reservation reservation = dao.getReservationParId(idReservation);
                if (reservation == null) return label;

                if (!reservation.getStatut().equalsIgnoreCase("non payée") &&
                        (actionType.equals("payer") || actionType.equals("annuler"))) {
                    JOptionPane.showMessageDialog(null, "❌ Action non autorisée. Cette réservation a déjà été payée.");
                    return label;
                }

                switch (actionType) {
                    case "details":
                        SeanceDAO seanceDAO = new SeanceDAO();
                        Seance seance = seanceDAO.getSeanceById(reservation.getIdSeance());
                        FilmDAO filmDAO = new FilmDAO();
                        Film film = filmDAO.getFilmParId(seance.getIdFilm());

                        PdfUtils.genererRecuAvecQRCode(
                                film.getTitre(),
                                seance.getDateSeance(),
                                seance.getHeure(),
                                seance.getNomSalle(),
                                reservation.getNbPlaces(),
                                client.getNom() + " " + client.getPrenom(),
                                "BILLET" + reservation.getIdReservation(),
                                reservation.getNbPlaces() * 18
                        );
                        break;

                    case "annuler":
                        int confirm = JOptionPane.showConfirmDialog(null, "Confirmer l'annulation ?", "Annuler", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            boolean ok = dao.supprimerReservation(idReservation);
                            if (ok) {
                                JOptionPane.showMessageDialog(null, "✅ Réservation annulée !");
                                SwingUtilities.invokeLater(() -> chargerReservations());
                            }
                        }
                        break;

                    case "payer":
                        new PaiementFrame(client, reservation).setVisible(true);
                        SwingUtilities.getWindowAncestor(button).dispose();
                        break;
                }
            }
            clicked = false;
            return label;
        }

        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}