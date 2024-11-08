package com.eiman.olimpiada.controller;

import com.eiman.olimpiada.dao.DeporteDAO;
import com.eiman.olimpiada.model.Deporte;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controlador para gestionar la lógica de negocio de la entidad Deporte.
 */
public class DeporteController {

    @FXML
    private TextField nombreField;

    private DeporteDAO deporteDAO;
    private Deporte deporte;
    private ResourceBundle bundle;
    private boolean editMode;
    private Connection connection;

    /**
     * Constructor vacío para la inicialización.
     */
    public DeporteController() {
        this.bundle = ResourceBundle.getBundle("lang.messages", Locale.getDefault());
    }

    /**
     * Establece la conexión a la base de datos.
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
        this.deporteDAO = new DeporteDAO();
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    /**
     * Establece los datos de la fila seleccionada en el controlador.
     */
    public void setData(ObservableList<String> row) {
        nombreField.setText(row.get(1));  // Nombre del deporte
    }

    public Deporte getData() {
        Deporte deporte = new Deporte();
        deporte.setNombre(nombreField.getText());
        return deporte;
    }

    /**
     * Método para guardar el deporte (Agregar o Modificar).
     */
    @FXML
    private void handleSave() {
        List<String> errors = new ArrayList<>();
        ResourceBundle bundle = ResourceBundle.getBundle("lang.messages", Locale.getDefault());

        String nombre = nombreField.getText();
        if (nombre.isEmpty()) {
            errors.add(bundle.getString("label.name"));
        }

        if (!errors.isEmpty()) {
            showAlert(bundle.getString("error.missing_fields") + "\n- " + String.join("\n- ", errors), Alert.AlertType.ERROR);
            return;
        }

        try {
            closeWindow();
            Deporte deporte = new Deporte();
            deporte.setNombre(nombre);

            boolean success;
            if (editMode) {
                deporte.setId(getData().getId()); // Asignar el ID para editar
                success = DeporteDAO.updateDeporte(deporte);
            } else {
                success = DeporteDAO.insertDeporte(deporte);
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

    /**
     * Método para cancelar la operación y cerrar el formulario.
     */
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    /**
     * Limpia el campo de entrada.
     */
    private void clearFields() {
        nombreField.clear();
    }

    public void closeWindow() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }

    /**
     * Muestra una alerta con el mensaje especificado.
     */
    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
