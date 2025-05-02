/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema.tests;
import com.mycompany.cinema.database.Database;

import java.sql.*;
/**
 *
 * @author yassi
 */
public class TestConnexion {
     public static void main(String[] args) {
        Connection conn = Database.getConnection();
        if (conn != null) {
            System.out.println("✅ Connexion établie avec succès !");
        } else {
            System.out.println("❌ Connexion échouée !");
        }
    }
}
