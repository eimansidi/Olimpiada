package com.eiman.olimpiada.controller;

import com.eiman.olimpiada.dao.EquipoDAO;
import com.eiman.olimpiada.dao.OlimpiadaDAO;
import com.eiman.olimpiada.model.Equipo;
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
 * Controlador para gestionar la lógica de negocio de la entidad Equipo.
 */
public class EquipoController {

    @FXML
    private TextField nombreField;
    @FXML
    private TextField inicialesField;

    private EquipoDAO equipoDAO;
    private Equipo equipo;
    private Connection connection;
    private ResourceBundle bundle;
    private boolean editMode;

    public EquipoController(){
        this.bundle = ResourceBundle.getBundle("lang.messages", Locale.getDefault());
    }

    /**
     * Establece la conexión a la base de datos.
     *
     * @param connection La conexión de base de datos a usar en este controlador.
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
        this.equipoDAO = new EquipoDAO();
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    /**
     * Establece los datos de la fila seleccionada en el controlador.
     *
     * @param equipo Datos de la fila seleccionada.
     */
    public void setData(Equipo equipo) {
        this.equipo = equipo;
        nombreField.setText(equipo.getNombre());
        inicialesField.setText(equipo.getIniciales());
    }

    public Equipo getData() {
        if (equipo == null) {
            equipo = new Equipo();
        }
        equipo.setNombre(nombreField.getText());
        equipo.setIniciales(inicialesField.getText());
        return equipo;
    }

    /**
     * Maneja la acción de guardar o actualizar un equipo en la base de datos.
     */
    @FXML
    private void handleSave() {
        List<String> errors = new ArrayList<>();
        ResourceBundle bundle = ResourceBundle.getBundle("lang.messages", Locale.getDefault());

        String nombre = nombreField.getText();
        if (nombre.isEmpty()) {
            errors.add(bundle.getString("label.name"));
        }

        String iniciales = inicialesField.getText();
        if (iniciales.isEmpty()) {
            errors.add(bundle.getString("label.region"));
        }

        if (!errors.isEmpty()) {
            showAlert(bundle.getString("error.missing_fields") + "\n- " + String.join("\n- ", errors), Alert.AlertType.ERROR);
            return;
        }

        try {
            closeWindow();
            Equipo equipo = getData(); // Obtener datos del formulario
            boolean success;
            if (editMode) {
                success = equipoDAO.updateEquipo(equipo);  // Llamar a update si está en modo edición
            } else {
                success = equipoDAO.insertEquipo(equipo);  // Insertar si es nuevo
            }

            if (success) {
                showAlert(bundle.getString("alert.success_save"), Alert.AlertType.INFORMATION);
                clearFields();
                closeWindow();
            } else {
                showAlert(bundle.getString("alert.error_save"), Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert(bundle.getString("alert.error_database"), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Maneja la acción de cancelar, cerrando la ventana actual.
     */
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    /**
     * Limpia los campos del formulario.
     */
    private void clearFields() {
        nombreField.clear();
        inicialesField.clear();
    }

    private void closeWindow() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }

    /**
     * Muestra una alerta con el mensaje especificado.
     *
     * @param message   El mensaje a mostrar en la alerta.
     * @param alertType El tipo de alerta.
     */
    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
