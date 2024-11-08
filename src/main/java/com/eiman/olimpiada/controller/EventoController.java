package com.eiman.olimpiada.controller;

import com.eiman.olimpiada.dao.DeporteDAO;
import com.eiman.olimpiada.dao.EventoDAO;
import com.eiman.olimpiada.dao.OlimpiadaDAO;
import com.eiman.olimpiada.model.Evento;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EventoController {

    private EventoDAO eventoDAO;
    private Evento evento;
    private Connection connection;
    private ResourceBundle bundle;
    private boolean editMode;

    @FXML
    private TextField nombreField;
    @FXML
    private ComboBox<String> deporteComboBox;
    @FXML
    private ComboBox<String> olimpiadaComboBox;

    public EventoController() {
        this.bundle = ResourceBundle.getBundle("lang.messages");
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
        this.eventoDAO = new EventoDAO();
        eventoDAO.setConnection(connection);
        loadComboBoxData(); // Cargar datos en los ComboBox cuando se establece la conexión
    }

    @FXML
    private void initialize() {
        loadComboBoxData();
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public void setData(Evento evento) throws SQLException {
        this.evento = evento;
        nombreField.setText(evento.getNombre());
        deporteComboBox.setValue(DeporteDAO.getNameById(evento.getIdDeporte())); // Cargar nombre del deporte
        olimpiadaComboBox.setValue(OlimpiadaDAO.getNameById(evento.getIdOlimpiada())); // Cargar nombre de la olimpiada
    }

    @FXML
    private void handleSave() {
        List<String> errors = new ArrayList<>();

        String nombre = nombreField.getText();
        if (nombre == null || nombre.isEmpty()) {
            errors.add(bundle.getString("label.name"));
        }

        String deporte = deporteComboBox.getValue();
        if (deporte == null || deporte.isEmpty()) {
            errors.add(bundle.getString("label.sport"));
        }

        String olimpiada = olimpiadaComboBox.getValue();
        if (olimpiada == null || olimpiada.isEmpty()) {
            errors.add(bundle.getString("label.olympics"));
        }

        if (!errors.isEmpty()) {
            showAlert(bundle.getString("error.missing_fields") + "\n- " + String.join("\n- ", errors), Alert.AlertType.ERROR);
            return;
        }

        try {
            // Configurar el evento
            if (evento == null) {
                evento = new Evento(); // Crear un nuevo evento si no existe
            }
            evento.setNombre(nombre);
            evento.setIdDeporte(DeporteDAO.getIdByName(deporte));
            evento.setIdOlimpiada(OlimpiadaDAO.getIdByName(olimpiada));

            // Intentar guardar o actualizar según el modo de edición
            boolean success = editMode ? eventoDAO.updateEvento(evento) : eventoDAO.insertEvento(evento);

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
     * Carga datos en los ComboBox de deportes y olimpiadas.
     */
    private void loadComboBoxData() {
        try {
            deporteComboBox.setItems(FXCollections.observableArrayList(DeporteDAO.getAllDeporteNames()));
            olimpiadaComboBox.setItems(FXCollections.observableArrayList(OlimpiadaDAO.getAllOlimpiadaNames()));
        } catch (SQLException e) {
            showAlert("Error al cargar datos en ComboBox", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void clearFields() {
        nombreField.clear();
        deporteComboBox.getSelectionModel().clearSelection();
        olimpiadaComboBox.getSelectionModel().clearSelection();
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
