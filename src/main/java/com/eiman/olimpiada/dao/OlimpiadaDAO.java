package com.eiman.olimpiada.dao;

import com.eiman.olimpiada.model.Olimpiada;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OlimpiadaDAO {

    private static Connection connection;

    public static void setConnection(Connection conn) {
        connection = conn;
    }

    public static boolean insertOlimpiada(Olimpiada olimpiada) throws SQLException {
        String sql = "INSERT INTO olimpiada (nombre, anio, temporada, ciudad) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, olimpiada.getNombre());
            statement.setInt(2, olimpiada.getAnio());
            statement.setString(3, olimpiada.getTemporada());
            statement.setString(4, olimpiada.getCiudad());
            return statement.executeUpdate() > 0;
        }
    }

    public static boolean updateOlimpiada(Olimpiada olimpiada) throws SQLException {
        String sql = "UPDATE olimpiada SET nombre = ?, anio = ?, temporada = ?, ciudad = ? WHERE id_olimpiada = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, olimpiada.getNombre());
            statement.setInt(2, olimpiada.getAnio());
            statement.setString(3, olimpiada.getTemporada());
            statement.setString(4, olimpiada.getCiudad());
            statement.setInt(5, olimpiada.getId());
            return statement.executeUpdate() > 0;
        }
    }

    public static boolean deleteOlimpiada(int id) throws SQLException {
        String sql = "DELETE FROM olimpiada WHERE id_olimpiada = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    public static List<String> getAllOlimpiadaName() throws SQLException {
        List<String> olimpiadas = new ArrayList<>();
        String sql = "SELECT nombre FROM olimpiada"; // Cambiado a 'nombre'
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                olimpiadas.add(resultSet.getString("nombre")); // Cambiado a 'nombre'
            }
        }
        return olimpiadas;
    }

    /**
     * Obtiene el ID de un evento dado su nombre.
     *
     * @param nombre El nombre del evento.
     * @return El ID del evento si se encuentra, o -1 si no se encuentra.
     * @throws SQLException si ocurre un error al acceder a la base de datos.
     */
    public static int getIdByName(String nombre) throws SQLException {
        String query = "SELECT id_olimpiada FROM olimpiada WHERE nombre = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_olimpiada");
                }
            }
        }
        return -1; // Retorna -1 si no se encuentra el evento
    }

    public static Olimpiada getOlimpiadaById(int id) throws SQLException {
        String sql = "SELECT * FROM olimpiada WHERE id_olimpiada = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToOlimpiada(resultSet);
                }
            }
        }
        return null;
    }

    public static String getNameById(int id) throws SQLException {
        String sql = "SELECT anio FROM olimpiada WHERE id_olimpiada = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("anio");
                }
            }
        }
        return null; // Retorna null si no se encuentra la olimpiada
    }

    public static List<Olimpiada> getAllOlimpiadas() throws SQLException {
        List<Olimpiada> olimpiadas = new ArrayList<>();
        String sql = "SELECT * FROM olimpiada";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                olimpiadas.add(mapResultSetToOlimpiada(resultSet));
            }
        }
        return olimpiadas;
    }

    private static Olimpiada mapResultSetToOlimpiada(ResultSet resultSet) throws SQLException {
        Olimpiada olimpiada = new Olimpiada();
        olimpiada.setId(resultSet.getInt("id_olimpiada"));
        olimpiada.setNombre(resultSet.getString("nombre"));
        olimpiada.setAnio(resultSet.getInt("anio"));
        olimpiada.setTemporada(resultSet.getString("temporada"));
        olimpiada.setCiudad(resultSet.getString("ciudad"));
        return olimpiada;
    }
}
