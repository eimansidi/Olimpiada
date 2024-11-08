package com.eiman.olimpiada.dao;

import com.eiman.olimpiada.model.Deportista;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeportistaDAO {

    private static Connection connection;

    /**
     * Establece la conexión a la base de datos para esta clase DAO.
     *
     * @param conn La conexión de base de datos a utilizar.
     */
    public static void setConnection(Connection conn) {
        connection = conn;
    }

    /**
     * Inserta un nuevo registro de Deportista en la base de datos.
     *
     * @param deportista El objeto Deportista a añadir.
     * @return true si el registro se insertó correctamente, false en caso contrario.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    public static boolean insertDeportista(Deportista deportista) throws SQLException {
        String sql = "INSERT INTO deportista (nombre, sexo, edad, peso, altura, foto) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, deportista.getNombre());
            statement.setString(2, deportista.getSexo().toString());
            statement.setInt(3, deportista.getEdad());
            statement.setDouble(4, deportista.getPeso());
            statement.setDouble(5, deportista.getAltura());
            statement.setBytes(6, deportista.getFoto());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0; // Devuelve true si se insertó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Devuelve false en caso de excepción
        }
    }

    /**
     * Actualiza un registro existente de Deportista en la base de datos.
     *
     * @param deportista El objeto Deportista con la información actualizada.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    public static boolean updateDeportista(Deportista deportista) throws SQLException {
        String sql = "UPDATE deportista SET nombre = ?, sexo = ?, edad = ?, peso = ?, altura = ?, foto = ? WHERE id_deportista = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, deportista.getNombre());
            statement.setString(2, deportista.getSexo().toString());
            statement.setInt(3, deportista.getEdad());
            statement.setDouble(4, deportista.getPeso());
            statement.setDouble(5, deportista.getAltura());
            statement.setBytes(6, deportista.getFoto());
            statement.setInt(7, deportista.getId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0; // Devuelve true si al menos una fila fue actualizada
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Devuelve false en caso de excepción
        }
    }

    /**
     * Elimina un registro de Deportista de la base de datos basado en el ID proporcionado.
     *
     * @param id El ID del Deportista a eliminar.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    public static boolean deleteDeportista(int id) throws SQLException {
        String sql = "DELETE FROM deportista WHERE id_deportista = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0; // Devuelve true si al menos una fila fue eliminada
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Devuelve false en caso de excepción
        }
    }

    /**
     * Recupera un registro de Deportista de la base de datos basado en el ID proporcionado.
     *
     * @param id El ID del Deportista a recuperar.
     * @return El objeto Deportista si se encuentra, null en caso contrario.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    public static Deportista getDeportistaById(int id) throws SQLException {
        String sql = "SELECT * FROM deportista WHERE id_deportista = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToDeportista(resultSet);
                }
            }
        }
        return null;
    }

    // Método para obtener el nombre de un deportista por su ID
    public static String getNameById(int id) throws SQLException {
        String query = "SELECT nombre FROM deportista WHERE id_deportista = ?";
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

    /**
     * Recupera una lista de todos los registros de Deportista de la base de datos.
     *
     * @return Una lista de objetos Deportista.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    public static List<Deportista> getAllDeportistas() throws SQLException {
        List<Deportista> deportistas = new ArrayList<>();
        String sql = "SELECT * FROM deportista";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                deportistas.add(mapResultSetToDeportista(resultSet));
            }
        }
        return deportistas;
    }

    /**
     * Obtiene una lista de los nombres de todos los deportistas en la base de datos.
     *
     * @return Una lista de nombres de deportistas.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    public static List<String> getAllDeportistaNames() throws SQLException {
        List<String> deportistas = new ArrayList<>();
        String sql = "SELECT nombre FROM deportista";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                deportistas.add(resultSet.getString("nombre"));
            }
        }
        return deportistas;
    }

    /**
     * Obtiene el ID de un deportista dado su nombre.
     *
     * @param nombre El nombre del deportista.
     * @return El ID del deportista, o -1 si no se encuentra.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    public static int getIdByName(String nombre) throws SQLException {
        String sql = "SELECT id_deportista FROM deportista WHERE nombre = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nombre);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_deportista");
                }
            }
        }
        return -1; // Retorna -1 si no se encuentra el deportista
    }

    /**
     * Mapea la fila actual del ResultSet a un objeto Deportista.
     *
     * @param resultSet El resultado de la consulta a la base de datos.
     * @return Un objeto Deportista con los valores de la fila actual del ResultSet.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    private static Deportista mapResultSetToDeportista(ResultSet resultSet) throws SQLException {
        Deportista deportista = new Deportista();
        deportista.setId(resultSet.getInt("id_deportista"));
        deportista.setNombre(resultSet.getString("nombre"));
        deportista.setSexo(Deportista.Sexo.valueOf(resultSet.getString("sexo")));
        deportista.setEdad(resultSet.getInt("edad"));
        deportista.setPeso(resultSet.getDouble("peso"));
        deportista.setAltura(resultSet.getDouble("altura"));
        deportista.setFoto(resultSet.getBytes("foto"));
        return deportista;
    }
}
