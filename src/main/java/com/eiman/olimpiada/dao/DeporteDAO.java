package com.eiman.olimpiada.dao;

import com.eiman.olimpiada.model.Deporte;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de Acceso a Datos (DAO) estático para gestionar las operaciones relacionadas con la entidad Deporte en la base de datos.
 */
public class DeporteDAO {

    private static Connection connection;

    /**
     * Inicializa la conexión a la base de datos.
     * @param conn La conexión a la base de datos que usará este DAO.
     */
    public static void setConnection(Connection conn) {
        connection = conn;
    }

    /**
     * Inserta un nuevo registro de Deporte en la base de datos.
     *
     * @param deporte El objeto Deporte a añadir.
     * @return true si el registro se insertó correctamente, false en caso contrario.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    public static boolean insertDeporte(Deporte deporte) throws SQLException {
        String sql = "INSERT INTO deporte (nombre) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, deporte.getNombre());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }

    /**
     * Actualiza un registro existente de Deporte en la base de datos.
     *
     * @param deporte El objeto Deporte con la información actualizada.
     * @return true si el registro se actualizó correctamente, false en caso contrario.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    public static boolean updateDeporte(Deporte deporte) throws SQLException {
        String sql = "UPDATE deporte SET nombre = ? WHERE id_deporte = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, deporte.getNombre());
            statement.setInt(2, deporte.getId());
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    /**
     * Elimina un registro de Deporte de la base de datos basado en el ID proporcionado.
     *
     * @param id El ID del Deporte a eliminar.
     * @return true si el registro se eliminó correctamente, false en caso contrario.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    public static boolean deleteDeporte(int id) throws SQLException {
        String sql = "DELETE FROM deporte WHERE id_deporte = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        }
    }

    public static List<String> getAllDeporteNames() throws SQLException {
        List<String> deportes = new ArrayList<>();
        String sql = "SELECT nombre FROM deporte";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                deportes.add(resultSet.getString("nombre"));
            }
        }
        return deportes;
    }

    /**
     * Obtiene el ID de un evento dado su nombre.
     *
     * @param nombre El nombre del evento.
     * @return El ID del evento si se encuentra, o -1 si no se encuentra.
     * @throws SQLException si ocurre un error al acceder a la base de datos.
     */
    public static int getIdByName(String nombre) throws SQLException {
        String sql = "SELECT id_deporte FROM deporte WHERE nombre = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nombre);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_deporte");
                }
            }
        }
        return -1; // Retorna -1 si no se encuentra el deporte
    }

    public static String getNameById(int id) throws SQLException {
        String sql = "SELECT nombre FROM deporte WHERE id_deporte = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nombre");
                }
            }
        }
        return null; // Retorna null si no se encuentra el deporte
    }

    /**
     * Recupera un registro de Deporte de la base de datos basado en el ID proporcionado.
     *
     * @param id El ID del Deporte a recuperar.
     * @return El objeto Deporte si se encuentra, null en caso contrario.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    public static Deporte getDeporteById(int id) throws SQLException {
        String sql = "SELECT * FROM deporte WHERE id_deporte = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToDeporte(resultSet);
                }
            }
        }
        return null;
    }

    /**
     * Recupera una lista de todos los registros de Deporte de la base de datos.
     *
     * @return Una lista de objetos Deporte.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    public static List<Deporte> getAllDeportes() throws SQLException {
        List<Deporte> deportes = new ArrayList<>();
        String sql = "SELECT * FROM deporte";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                deportes.add(mapResultSetToDeporte(resultSet));
            }
        }
        return deportes;
    }

    /**
     * Mapea la fila actual del ResultSet a un objeto Deporte.
     *
     * @param resultSet El resultado de la consulta a la base de datos.
     * @return Un objeto Deporte con los valores de la fila actual del ResultSet.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    private static Deporte mapResultSetToDeporte(ResultSet resultSet) throws SQLException {
        Deporte deporte = new Deporte();
        deporte.setId(resultSet.getInt("id_deporte"));
        deporte.setNombre(resultSet.getString("nombre"));
        return deporte;
    }
}
