package com.mycompany.cinema.tests;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author yassi
 */
import com.mycompany.cinema.dao.ClientDAO;
import com.mycompany.cinema.models.Client;

public class TestAjoutClient {
    public static void main(String[] args) {
Client client = new Client("Durand", "Claire", "claire.durand3@example.com", "password123");
        ClientDAO clientDAO = new ClientDAO();

        if (clientDAO.insert(client)) {
            System.out.println("Client ajouté avec succès !");
        } else {
            System.out.println("Erreur lors de l'ajout du client.");
        }
    }
}

