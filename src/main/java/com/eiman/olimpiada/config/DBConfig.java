package com.eiman.olimpiada.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Configuración de la base de datos para la aplicación.
 * Incluye la inicialización de la conexión y la creación de tablas si no existen.
 */
public class DBConfig {

    private static Connection connection;
    private static final String DB_URL_BASE = "jdbc:mysql://database-1.cr60ewocg533.us-east-1.rds.amazonaws.com:3306/";
    private static final String DB_NAME = "olimpiadas"; // Nombre de la base de datos
    private static final String DB_URL = DB_URL_BASE + DB_NAME + "?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "admin"; // Reemplaza con tu usuario
    private static final String PASSWORD = "12345678"; // Reemplaza con tu contraseña

    /**
     * Obtiene la conexión a la base de datos, creando la base de datos y las tablas si es necesario.
     * @return La conexión a la base de datos.
     * @throws SQLException Si ocurre un error en la conexión a la base de datos.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            createDatabaseIfNotExists(); // Crea la base de datos si no existe
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            initializeTables(); // Crea las tablas si no existen
        }
        return connection;
    }

    /**
     * Crea la base de datos si no existe.
     */
    private static void createDatabaseIfNotExists() {
        String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
        try (Connection conn = DriverManager.getConnection(DB_URL_BASE, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createDatabaseQuery);
            System.out.println("Base de datos verificada o creada.");
        } catch (SQLException e) {
            System.err.println("Error al verificar o crear la base de datos: " + e.getMessage());
        }
    }

    /**
     * Crea todas las tablas necesarias en la base de datos si no existen.
     */
    private static void initializeTables() {
        try (Statement stmt = connection.createStatement()) {

            // Crear tabla deportista
            String createDeportistasTable = "CREATE TABLE IF NOT EXISTS deportista (" +
                    "id_deportista INT PRIMARY KEY AUTO_INCREMENT, " +
                    "nombre VARCHAR(100), " +
                    "sexo CHAR(1), " +
                    "edad INT, " +
                    "foto BLOB, " +
                    "peso DECIMAL(5, 2), " +
                    "altura DECIMAL(5, 2))";
            stmt.executeUpdate(createDeportistasTable);

            // Crear tabla equipo
            String createEquiposTable = "CREATE TABLE IF NOT EXISTS equipo (" +
                    "id_equipo INT PRIMARY KEY AUTO_INCREMENT, " +
                    "nombre VARCHAR(100), " +
                    "iniciales VARCHAR(10))";
            stmt.executeUpdate(createEquiposTable);

            // Crear tabla deporte
            String createDeportesTable = "CREATE TABLE IF NOT EXISTS deporte (" +
                    "id_deporte INT PRIMARY KEY AUTO_INCREMENT, " +
                    "nombre VARCHAR(100))";
            stmt.executeUpdate(createDeportesTable);

            // Crear tabla olimpiada
            String createOlimpiadasTable = "CREATE TABLE IF NOT EXISTS olimpiada (" +
                    "id_olimpiada INT PRIMARY KEY AUTO_INCREMENT, " +
                    "nombre VARCHAR(100), " +
                    "anio INT, " +
                    "temporada VARCHAR(50), " +
                    "ciudad VARCHAR(100))";
            stmt.executeUpdate(createOlimpiadasTable);

            // Crear tabla evento
            String createEventosTable = "CREATE TABLE IF NOT EXISTS evento (" +
                    "id_evento INT PRIMARY KEY AUTO_INCREMENT, " +
                    "nombre VARCHAR(100), " +
                    "id_olimpiada INT, " +
                    "id_deporte INT, " +
                    "FOREIGN KEY (id_olimpiada) REFERENCES olimpiada(id_olimpiada), " +
                    "FOREIGN KEY (id_deporte) REFERENCES deporte(id_deporte))";
            stmt.executeUpdate(createEventosTable);

            // Crear tabla participacion
            String createParticipacionesTable = "CREATE TABLE IF NOT EXISTS participacion (" +
                    "id_deportista INT, " +
                    "id_evento INT, " +
                    "id_equipo INT, " +
                    "medalla VARCHAR(50), " +
                    "PRIMARY KEY (id_deportista, id_evento), " +
                    "FOREIGN KEY (id_deportista) REFERENCES deportista(id_deportista), " +
                    "FOREIGN KEY (id_evento) REFERENCES evento(id_evento), " +
                    "FOREIGN KEY (id_equipo) REFERENCES equipo(id_equipo))";
            stmt.executeUpdate(createParticipacionesTable);

            System.out.println("Todas las tablas han sido verificadas o creadas con éxito.");

        } catch (SQLException e) {
            System.err.println("Error al verificar o crear las tablas: " + e.getMessage());
        }
    }
}
