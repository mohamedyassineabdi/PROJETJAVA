package com.mycompany.cinema.utils;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;

public class PdfUtils {

    public static void genererRecuAvecQRCode(String titreFilm, String date, String heure, String salle,
                                             int nbPlaces, String nomClient, String numeroBillet, double montant) {
        Document document = new Document();
        try {
            String nomFichier = "recu_" + numeroBillet + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(nomFichier));
            document.open();

            Font titreFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("\uD83C\uDF9F️ Reçu de réservation - Billetterie Cinéma", titreFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Client : " + nomClient, normalFont));
            document.add(new Paragraph("Film : " + titreFilm, normalFont));
            document.add(new Paragraph("Date de la séance : " + date, normalFont));
            document.add(new Paragraph("Heure : " + heure, normalFont));
            document.add(new Paragraph("Salle : " + salle, normalFont));
            document.add(new Paragraph("Nombre de places : " + nbPlaces, normalFont));
            document.add(new Paragraph("Montant : " + montant + " DT", normalFont));
            document.add(new Paragraph("Numéro de billet : " + numeroBillet, normalFont));
            document.add(new Paragraph("Merci pour votre réservation !", normalFont));

            // Génération du QR code
            String qrData = "Film: " + titreFilm + "\nDate: " + date + "\nHeure: " + heure +
                    "\nClient: " + nomClient + "\nBillet: " + numeroBillet;
            Path qrPath = Files.createTempFile("qrcode_", ".png");
            generateQRCodeImage(qrData, 150, 150, qrPath);

            Image qrImage = Image.getInstance(qrPath.toAbsolutePath().toString());
            qrImage.setAlignment(Image.ALIGN_CENTER);
            document.add(qrImage);

            document.close();
            Files.deleteIfExists(qrPath); // Nettoyer le fichier temporaire

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(nomFichier));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generateQRCodeImage(String text, int width, int height, Path filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);
    }
}
