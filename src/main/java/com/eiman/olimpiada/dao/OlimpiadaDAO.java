package com.eiman.olimpiada.dao;

import com.eiman.olimpiada.model.Olimpiada;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OlimpiadaDAO {

    private static Connection connection;

    // Método para establecer la conexión de la base de datos
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
            return statement.executeUpdate() > 0; // Debería devolver true si se añadió correctamente
        }
    }


    // Método para actualizar una olimpiada existente
    public static boolean updateOlimpiada(Olimpiada olimpiada) throws SQLException {
        String sql = "UPDATE olimpiada SET nombre = ?, anio = ?, temporada = ?, ciudad = ? WHERE id_olimpiada = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, olimpiada.getNombre());
            statement.setInt(2, olimpiada.getAnio());
            statement.setString(3, olimpiada.getTemporada());
            statement.setString(4, olimpiada.getCiudad());
            statement.setInt(5, olimpiada.getId());  // `id_olimpiada` en la base de datos
            return statement.executeUpdate() > 0;
        }
    }

    // Método para eliminar una olimpiada por su ID
    public static boolean deleteOlimpiada(int id) throws SQLException {
        String sql = "DELETE FROM olimpiada WHERE id_olimpiada = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    // Método para obtener el ID de una olimpiada dado su nombre
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

    // Método para obtener una olimpiada por su ID
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

    // Método para obtener el nombre de una olimpiada por su ID
    public static String getNameById(int id) throws SQLException {
        String sql = "SELECT nombre FROM olimpiada WHERE id_olimpiada = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nombre");
                }
            }
        }
        return null; // Retorna null si no se encuentra la olimpiada
    }

    // Método para obtener todas las olimpiadas en una lista de objetos Olimpiada
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

    // Método para obtener los nombres de todas las olimpiadas
    public static List<String> getAllOlimpiadaNames() throws SQLException {
        List<String> olimpiadas = new ArrayList<>();
        String sql = "SELECT nombre FROM olimpiada";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                olimpiadas.add(resultSet.getString("nombre"));
            }
        }
        return olimpiadas;
    }

    // Método para mapear un ResultSet a un objeto Olimpiada
    private static Olimpiada mapResultSetToOlimpiada(ResultSet resultSet) throws SQLException {
        Olimpiada olimpiada = new Olimpiada();
        olimpiada.setId(resultSet.getInt("id_olimpiada"));  // Uso de `id_olimpiada` en toda la clase
        olimpiada.setNombre(resultSet.getString("nombre"));
        olimpiada.setAnio(resultSet.getInt("anio"));
        olimpiada.setTemporada(resultSet.getString("temporada"));
        olimpiada.setCiudad(resultSet.getString("ciudad"));
        return olimpiada;
    }
}
