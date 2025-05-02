/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.cinema;
import com.mycompany.cinema.gui.MainFrame;
import javax.swing.SwingUtilities;


/**
 *
 * @author yassi
 */
public class Cinema {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame(); // 👈 Ça ouvre directement ton interface principale
        });
    }
}
