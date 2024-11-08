package com.eiman.olimpiada.controller;

import com.eiman.olimpiada.dao.OlimpiadaDAO;
import com.eiman.olimpiada.dao.ParticipacionDAO;
import com.eiman.olimpiada.model.Olimpiada;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controlador para gestionar la lógica de negocio de la entidad Olimpiada.
 */
public class OlimpiadaController {

    private OlimpiadaDAO olimpiadaDAO;
    private Olimpiada olimpiada;
    private Connection connection;
    private boolean editMode;

    @FXML
    private TextField nombreField;
    @FXML
    private TextField anioField;
    @FXML
    private TextField temporadaField;
    @FXML
    private TextField ciudadField;

    private ResourceBundle bundle;

    public OlimpiadaController(){
        this.bundle = ResourceBundle.getBundle("lang.messages", Locale.getDefault());
    }

    /**
     * Establece la conexión a la base de datos y crea la instancia de OlimpiadaDAO.
     *
     * @param connection La conexión de base de datos a usar en este controlador.
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
        this.olimpiadaDAO = new OlimpiadaDAO();
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    /**
     * Configura los datos en el formulario para modificar una olimpiada existente.
     */
    public void setData(ObservableList<String> row) {
        nombreField.setText(row.get(1));       // Nombre de la olimpiada
        anioField.setText(row.get(2));         // Año de la olimpiada
        temporadaField.setText(row.get(3));    // Temporada (verano/invierno)
        ciudadField.setText(row.get(4));       // Ciudad
    }

    /**
     * Recupera los datos de la Olimpiada desde el formulario.
     * @return Una instancia de Olimpiada con los datos ingresados en el formulario.
     */
    public Olimpiada getOlimpiadaData() {
        Olimpiada olimpiada = new Olimpiada();
        olimpiada.setNombre(nombreField.getText());
        olimpiada.setAnio(Integer.parseInt(anioField.getText()));
        olimpiada.setTemporada(temporadaField.getText());
        olimpiada.setCiudad(ciudadField.getText());
        return olimpiada;
    }

    public void setEditMode(ObservableList<String> rowData) {
        anioField.setText(rowData.get(1)); // Año de la olimpiada
        nombreField.setText(rowData.get(2)); // Nombre de la olimpiada
        temporadaField.setText(rowData.get(3)); // Temporada
        ciudadField.setText(rowData.get(4)); // Ciudad
    }

    @FXML
    private void handleSave() {
        List<String> errors = new ArrayList<>();

        String nombre = nombreField.getText();
        if (nombre.isEmpty()) {
            errors.add(bundle.getString("label.name"));
        }

        int anio = 0;
        try {
            anio = Integer.parseInt(anioField.getText());
        } catch (NumberFormatException e) {
            errors.add(MessageFormat.format(bundle.getString("error.invalid_number"), bundle.getString("label.year")));
        }

        String temporada = temporadaField.getText();
        if (temporada.isEmpty()) {
            errors.add(bundle.getString("label.season"));
        }

        String ciudad = ciudadField.getText();
        if (ciudad.isEmpty()) {
            errors.add(bundle.getString("label.city"));
        }

        if (!errors.isEmpty()) {
            showAlert(bundle.getString("error.missing_fields") + "\n- " + String.join("\n- ", errors), Alert.AlertType.ERROR);
            return;
        }

        try {
            closeWindow();
            Olimpiada olimpiada = new Olimpiada();
            olimpiada.setNombre(nombre);
            olimpiada.setAnio(anio);
            olimpiada.setTemporada(temporada);
            olimpiada.setCiudad(ciudad);

            boolean success;
            if (editMode) {
                olimpiada.setId(this.olimpiada.getId()); // Aseguramos que use el ID en modo edición
                success = olimpiadaDAO.updateOlimpiada(olimpiada); // Actualizar en modo edición
            } else {
                success = olimpiadaDAO.insertOlimpiada(olimpiada); // Insertar en modo nuevo
            }

            if (success) {
                showAlert(bundle.getString("alert.success_save"), Alert.AlertType.INFORMATION);
                clearFields();
            } else {
                showAlert(bundle.getString("alert.error_save"), Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert(bundle.getString("alert.error_database"), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void clearFields() {
        nombreField.clear();
        anioField.clear();
        temporadaField.clear();
        ciudadField.clear();
    }

    private void closeWindow() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }


    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
