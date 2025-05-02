/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema.database;
import java.sql.*;
/**
 *
 * @author yassi
 */
public class Database {
   private static final String URL = "jdbc:mysql://localhost:3306/billetteriecinema"; // Ton URL (nom de la base BilletterieCinema)
    private static final String USER = "root"; // Ton utilisateur MySQL (par défaut root)
    private static final String PASSWORD = ""; // Ton mot de passe MySQL (vide si tu n'en as pas mis)

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la connexion à la base de données : " + e.getMessage());
            return null;
        }
    } 
}
