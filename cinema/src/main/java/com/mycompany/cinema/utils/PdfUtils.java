package com.mycompany.cinema.utils;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author yassi
 */
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.awt.Desktop;

public class PdfUtils {

    public static void genererRecu(String titreFilm, String date, String heure, String salle, int nbPlaces) {
        Document document = new Document();
        try {
            String nomFichier = "recu_" + System.currentTimeMillis() + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(nomFichier));
            document.open();

            Font titreFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("🎟️ Reçu de réservation - Billetterie Cinéma", titreFont));
            document.add(new Paragraph(" ", normalFont));
            document.add(new Paragraph("Film : " + titreFilm, normalFont));
            document.add(new Paragraph("Date : " + date, normalFont));
            document.add(new Paragraph("Heure : " + heure, normalFont));
            document.add(new Paragraph("Salle : " + salle, normalFont));
            document.add(new Paragraph("Nombre de places : " + nbPlaces, normalFont));
            document.add(new Paragraph("Merci pour votre réservation !", normalFont));

            document.close();

            // Ouvrir le PDF
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(nomFichier));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
