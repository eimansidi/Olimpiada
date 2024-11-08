package com.eiman.olimpiada.dao;

import com.eiman.olimpiada.model.Evento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {

    private static Connection connection;

    public static void setConnection(Connection conn) {
        connection = conn;
    }

    public static boolean insertEvento(Evento evento) throws SQLException {
        // Verificar si el id_deporte existe en la tabla deporte
        String checkQuery = "SELECT COUNT(*) FROM deporte WHERE id_deporte = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, evento.getIdDeporte());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    System.out.println("Error: id_deporte no existe en la tabla deporte.");
                    return false;  // id_deporte no existe, cancelar la inserción
                }
            }
        }

        // Si el id_deporte es válido, procede con la inserción
        String sql = "INSERT INTO evento (nombre, id_deporte, id_olimpiada) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, evento.getNombre());
            statement.setInt(2, evento.getIdDeporte());
            statement.setInt(3, evento.getIdOlimpiada());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }

    public static boolean updateEvento(Evento evento) throws SQLException {
        String sql = "UPDATE evento SET nombre = ?, id_olimpiada = ?, id_deporte = ? WHERE id_evento = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, evento.getNombre());
            statement.setInt(2, evento.getIdOlimpiada());
            statement.setInt(3, evento.getIdDeporte());
            statement.setInt(4, evento.getId());
            return statement.executeUpdate() > 0;
        }
    }

    public static boolean deleteEvento(int id) throws SQLException {
        String sql = "DELETE FROM evento WHERE id_evento = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    public static Evento getEventoById(int id) throws SQLException {
        String sql = "SELECT * FROM evento WHERE id_evento = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToEvento(resultSet);
                }
            }
        }
        return null;
    }

    public static List<Evento> getAllEventos() throws SQLException {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT * FROM evento";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                eventos.add(mapResultSetToEvento(resultSet));
            }
        }
        return eventos;
    }

    public static int getIdByName(String nombre) throws SQLException {
        String sql = "SELECT id_evento FROM evento WHERE nombre = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nombre);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_evento");
                }
            }
        }
        return -1; // Retorna -1 si no se encuentra el evento
    }

    // Método para obtener el nombre de un deportista por su ID
    public static String getNameById(int id) throws SQLException {
        String query = "SELECT nombre FROM evento WHERE id_evento = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("nombre");
                }
            }
        }
        return null; // Retorna null si no se encuentra el deportista
    }

    public static List<String> getAllEventoNames() throws SQLException {
        List<String> eventos = new ArrayList<>();
        String sql = "SELECT nombre FROM evento";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                eventos.add(resultSet.getString("nombre"));
            }
        }
        return eventos;
    }


    private static Evento mapResultSetToEvento(ResultSet resultSet) throws SQLException {
        Evento evento = new Evento();
        evento.setId(resultSet.getInt("id_evento"));
        evento.setNombre(resultSet.getString("nombre"));
        evento.setIdOlimpiada(resultSet.getInt("id_olimpiada"));
        evento.setIdDeporte(resultSet.getInt("id_deporte"));
        return evento;
    }
}
