package com.eiman.olimpiada.dao;

import com.eiman.olimpiada.model.Equipo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipoDAO {

    private static Connection connection;

    public static void setConnection(Connection conn) {
        connection = conn;
    }

    public static boolean insertEquipo(Equipo equipo) throws SQLException {
        String sql = "INSERT INTO equipo (nombre, iniciales) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, equipo.getNombre());
            statement.setString(2, equipo.getIniciales());
            return statement.executeUpdate() > 0;
        }
    }

    public static boolean updateEquipo(Equipo equipo) throws SQLException {
        String sql = "UPDATE equipo SET nombre = ?, iniciales = ? WHERE id_equipo = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, equipo.getNombre());
            statement.setString(2, equipo.getIniciales());
            statement.setInt(3, equipo.getId());
            return statement.executeUpdate() > 0;
        }
    }

    public static boolean deleteEquipo(int id) throws SQLException {
        String sql = "DELETE FROM equipo WHERE id_equipo = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    public static Equipo getEquipoById(int id) throws SQLException {
        String sql = "SELECT * FROM equipo WHERE id_equipo = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToEquipo(resultSet);
                }
            }
        }
        return null;
    }

    public static List<Equipo> getAllEquipos() throws SQLException {
        List<Equipo> equipos = new ArrayList<>();
        String sql = "SELECT * FROM equipo";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                equipos.add(mapResultSetToEquipo(resultSet));
            }
        }
        return equipos;
    }

    public static List<String> getAllEquipoNames() throws SQLException {
        List<String> equipos = new ArrayList<>();
        String sql = "SELECT nombre FROM equipo";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                equipos.add(resultSet.getString("nombre"));
            }
        }
        return equipos;
    }

    public static int getIdByName(String nombre) throws SQLException {
        String sql = "SELECT id_equipo FROM equipo WHERE nombre = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nombre);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_equipo");
                }
            }
        }
        return -1; // Retorna -1 si no se encuentra el equipo
    }

    // Método para obtener el nombre de un deportista por su ID
    public static String getNameById(int id) throws SQLException {
        String query = "SELECT nombre FROM equipo WHERE id_equipo = ?";
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

    private static Equipo mapResultSetToEquipo(ResultSet resultSet) throws SQLException {
        Equipo equipo = new Equipo();
        equipo.setId(resultSet.getInt("id_equipo"));
        equipo.setNombre(resultSet.getString("nombre"));
        equipo.setIniciales(resultSet.getString("iniciales"));
        return equipo;
    }
}
