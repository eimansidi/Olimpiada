package com.eiman.olimpiada.dao;

import com.eiman.olimpiada.model.Participacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipacionDAO {

    private static Connection connection;

    public static void setConnection(Connection conn) {
        connection = conn;
    }

    public static boolean insertParticipacion(Participacion participacion) throws SQLException {
        // Verificar si el id_deportista existe en la tabla deportista
        if (!existsInTable("deportista", "id_deportista", participacion.getIdDeportista())) {
            System.out.println("Error: id_deportista no existe en la tabla deportista.");
            return false;
        }

        // Verificar si el id_evento existe en la tabla evento
        if (!existsInTable("evento", "id_evento", participacion.getIdEvento())) {
            System.out.println("Error: id_evento no existe en la tabla evento.");
            return false;
        }

        // Verificar si el id_equipo existe en la tabla equipo
        if (!existsInTable("equipo", "id_equipo", participacion.getIdEquipo())) {
            System.out.println("Error: id_equipo no existe en la tabla equipo.");
            return false;
        }

        // Inserción en la tabla participacion
        String sql = "INSERT INTO participacion (id_deportista, id_evento, id_equipo, medalla) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, participacion.getIdDeportista());
            statement.setInt(2, participacion.getIdEvento());
            statement.setInt(3, participacion.getIdEquipo());
            statement.setString(4, participacion.getMedalla());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }

    // Método auxiliar para verificar la existencia de un ID en una tabla específica
    private static boolean existsInTable(String tableName, String columnName, int id) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }


    public static boolean updateParticipacion(Participacion participacion) throws SQLException {
        String sql = "UPDATE participacion SET id_equipo = ?, medalla = ? WHERE id_deportista = ? AND id_evento = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, participacion.getIdEquipo());
            statement.setString(2, participacion.getMedalla());
            statement.setInt(3, participacion.getIdDeportista());
            statement.setInt(4, participacion.getIdEvento());
            return statement.executeUpdate() > 0;
        }
    }


    public static boolean deleteParticipacion(int idDeportista, int idEvento) throws SQLException {
        String sql = "DELETE FROM participacion WHERE id_deportista = ? AND id_evento = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idDeportista);
            statement.setInt(2, idEvento);
            return statement.executeUpdate() > 0;
        }
    }

    public static boolean exists(int idDeportista, int idEvento, int idEquipo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM participacion WHERE id_deportista = ? AND id_evento = ? AND id_equipo = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idDeportista);
            statement.setInt(2, idEvento);
            statement.setInt(3, idEquipo);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Retorna true si existe al menos una coincidencia
                }
            }
        }
        return false;
    }


    public static Participacion getParticipacionById(int idDeportista, int idEvento) throws SQLException {
        String sql = "SELECT * FROM participacion WHERE id_deportista = ? AND id_evento = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idDeportista);
            statement.setInt(2, idEvento);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToParticipacion(resultSet);
                }
            }
        }
        return null;
    }

    public static List<Participacion> getAllParticipaciones() throws SQLException {
        List<Participacion> participaciones = new ArrayList<>();
        String sql = "SELECT * FROM participacion";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                participaciones.add(mapResultSetToParticipacion(resultSet));
            }
        }
        return participaciones;
    }

    private static Participacion mapResultSetToParticipacion(ResultSet resultSet) throws SQLException {
        Participacion participacion = new Participacion();
        participacion.setIdDeportista(resultSet.getInt("id_deportista"));
        participacion.setIdEvento(resultSet.getInt("id_evento"));
        participacion.setIdEquipo(resultSet.getInt("id_equipo"));
        participacion.setMedalla(resultSet.getString("medalla"));
        return participacion;
    }
}
