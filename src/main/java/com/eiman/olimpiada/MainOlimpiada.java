package com.eiman.olimpiada;

import com.eiman.olimpiada.config.DBConfig;
import com.eiman.olimpiada.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.ResourceBundle;

/**
 * Clase principal que inicia la aplicación de gestión de olimpiadas.
 */
// En MainOlimpiada.java
public class MainOlimpiada extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Establecer la conexión a la base de datos usando DBConfig
            Connection connection = DBConfig.getConnection();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
            ResourceBundle bundle = ResourceBundle.getBundle("lang.messages_en");
            loader.setResources(bundle);

            // Cargar y obtener el controlador
            Parent root = loader.load();
            MainController mainController = loader.getController();
            mainController.setConnection(connection);

            // Configuración de la escena
            primaryStage.setTitle(bundle.getString("app.title"));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
