/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.cinema;

import com.mycompany.cinema.gui.MainFrame2;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author yassi
 */
public class Cinema {

    public static void main(String[] args) {
        // Appliquer le thème FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Lancer l'application Swing
        SwingUtilities.invokeLater(() -> {
            MainFrame2 mf = new MainFrame2(); 
            mf.setVisible(true);
        });
    }
}
