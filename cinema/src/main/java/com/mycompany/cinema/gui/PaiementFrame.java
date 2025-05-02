/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema.gui;

/**
 *
 * @author yassi
 */


import com.mycompany.cinema.dao.ReservationDAO;
import com.mycompany.cinema.dao.SeanceDAO;
import com.mycompany.cinema.models.Reservation;
import com.mycompany.cinema.models.Seance;
import com.mycompany.cinema.utils.PdfUtils;

import javax.swing.*;
import java.awt.*;

public class PaiementFrame extends JFrame {

    private Reservation reservation;
    private Seance seance;

    public PaiementFrame(Reservation reservation) {
        this.reservation = reservation;

        // On récupère les infos de la séance liée
        SeanceDAO seanceDAO = new SeanceDAO();
        for (Seance s : seanceDAO.getSeancesParFilmEtFiltres("", "", "")) {
            if (s.getIdSeance() == reservation.getIdSeance()) {
                this.seance = s;
                break;
            }
        }

        initUI();
    }

    private void initUI() {
        setTitle("Paiement");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel labelInfo = new JLabel("<html><center>Montant à payer pour<br><b>" +
                reservation.getStatut() + "</b><br>" +
                "Film : " + seance.getDateSeance() + " à " + seance.getHeure() +
                "<br>Salle : " + seance.getNomSalle() +
                "<br>Places : " + reservation.getNbPlaces() +
                "</center></html>", SwingConstants.CENTER);

        JButton boutonPayer = new JButton("Payer maintenant");
        boutonPayer.addActionListener(e -> effectuerPaiement());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(labelInfo, BorderLayout.CENTER);
        panel.add(boutonPayer, BorderLayout.SOUTH);

        add(panel);
    }

    private void effectuerPaiement() {
        ReservationDAO dao = new ReservationDAO();
        dao.validerPaiement(reservation.getIdReservation());

        // Générer le ticket PDF
        PdfUtils.genererRecu(
                "Film réservé", // tu peux adapter pour y inclure le vrai titre si besoin
                seance.getDateSeance(),
                seance.getHeure(),
                seance.getNomSalle(),
                reservation.getNbPlaces()
        );

        JOptionPane.showMessageDialog(this, "✅ Paiement effectué avec succès.\nVotre ticket a été généré.");
        dispose(); // ferme la fenêtre
    }
}
