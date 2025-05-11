package com.mycompany.cinema.dao;

import com.mycompany.cinema.models.Film;
import com.mycompany.cinema.database.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmDAO {

    // 1. Récupérer tous les films
    public List<Film> getAllFilms() {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT * FROM films";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Film film = new Film(
                    rs.getInt("id_film"),
                    rs.getString("titre"),
                    rs.getString("genre"),
                    rs.getInt("duree"),
                    rs.getString("description")
                );
                films.add(film);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return films;
    }

    // 2. Insérer un nouveau film
    public boolean insertFilm(Film film) {
        String sql = "INSERT INTO films (titre, genre, duree, description) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, film.getTitre());
            stmt.setString(2, film.getGenre());
            stmt.setInt(3, film.getDuree());
            stmt.setString(4, film.getResume());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. Supprimer un film par son id
    public boolean deleteFilm(int idFilm) {
        String sql = "DELETE FROM films WHERE id_film = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFilm);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. Modifier un film
    public boolean updateFilm(Film film) {
        String sql = "UPDATE films SET titre = ?, genre = ?, duree = ?, description = ? WHERE id_film = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, film.getTitre());
            stmt.setString(2, film.getGenre());
            stmt.setInt(3, film.getDuree());
            stmt.setString(4, film.getResume());
            stmt.setInt(5, film.getIdFilm());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5. Rechercher des films par titre
    public List<Film> getFilmsParTitre(String titreRecherche) {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT * FROM films WHERE titre LIKE ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + titreRecherche + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Film film = new Film(
                    rs.getInt("id_film"),
                    rs.getString("titre"),
                    rs.getString("genre"),
                    rs.getInt("duree"),
                    rs.getString("description")
                );
                films.add(film);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return films;
    }

    // 6. Récupérer tous les films triés par une colonne
    public List<Film> getAllFilmsOrderBy(String colonne) {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT * FROM films ORDER BY " + colonne + " ASC";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Film film = new Film(
                    rs.getInt("id_film"),
                    rs.getString("titre"),
                    rs.getString("genre"),
                    rs.getInt("duree"),
                    rs.getString("description")
                );
                films.add(film);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return films;
    }

    // 7. Récupérer un film par son ID
    public Film getFilmParId(int idFilm) {
        String sql = "SELECT * FROM films WHERE id_film = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFilm);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Film(
                    rs.getInt("id_film"),
                    rs.getString("titre"),
                    rs.getString("genre"),
                    rs.getInt("duree"),
                    rs.getString("description")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
