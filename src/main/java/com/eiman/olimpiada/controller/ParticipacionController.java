package com.eiman.olimpiada.controller;

import com.eiman.olimpiada.dao.*;
import com.eiman.olimpiada.model.Participacion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ParticipacionController {

    private ParticipacionDAO ParticionDAO;
    private Participacion participacion;
    private Connection connection;
    private ResourceBundle bundle;
    private boolean editMode;

    @FXML
    private ComboBox<String> deportistaComboBox;
    @FXML
    private ComboBox<String> eventoComboBox;
    @FXML
    private ComboBox<String> equipoComboBox;

    // RadioButtons para el campo de Medalla
    @FXML
    private RadioButton oroRadio;
    @FXML
    private RadioButton plataRadio;
    @FXML
    private RadioButton bronceRadio;
    @FXML
    private RadioButton otroRadio;

    private ToggleGroup medallaGroup;

    public ParticipacionController() {
        this.bundle = ResourceBundle.getBundle("lang.messages", Locale.getDefault());
    }

    public void initialize() {
        // Configuración del ToggleGroup para los RadioButtons
        medallaGroup = new ToggleGroup();
        oroRadio.setToggleGroup(medallaGroup);
        plataRadio.setToggleGroup(medallaGroup);
        bronceRadio.setToggleGroup(medallaGroup);
        otroRadio.setToggleGroup(medallaGroup);

        loadComboBoxData(); // Cargar datos en los ComboBox al iniciar
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
        this.ParticionDAO = new ParticipacionDAO();
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public void setData(ObservableList<String> row) {
        deportistaComboBox.setValue(row.get(0));
        eventoComboBox.setValue(row.get(1));
        equipoComboBox.setValue(row.get(2));

        String medalla = row.get(3);
        switch (medalla) {
            case "Oro":
                oroRadio.setSelected(true);
                break;
            case "Plata":
                plataRadio.setSelected(true);
                break;
            case "Bronce":
                bronceRadio.setSelected(true);
                break;
            default:
                otroRadio.setSelected(true);
                break;
        }
    }

    /**
     * Método para obtener los datos actuales del formulario y retornarlos en un objeto Participacion.
     *
     * @return Un objeto Participacion con los datos del formulario.
     */
    public Participacion getData() {
        Participacion participacion = new Participacion();

        // Obtener ID de deportista, evento y equipo desde los ComboBox seleccionados
        participacion.setIdDeportista(deportistaComboBox.getSelectionModel().getSelectedIndex() + 1); // Ejemplo de cómo mapear al ID
        participacion.setIdEvento(eventoComboBox.getSelectionModel().getSelectedIndex() + 1);
        participacion.setIdEquipo(equipoComboBox.getSelectionModel().getSelectedIndex() + 1);

        // Obtener el valor de medalla basado en el RadioButton seleccionado
        if (oroRadio.isSelected()) {
            participacion.setMedalla("Oro");
        } else if (plataRadio.isSelected()) {
            participacion.setMedalla("Plata");
        } else if (bronceRadio.isSelected()) {
            participacion.setMedalla("Bronce");
        } else {
            participacion.setMedalla("Otro");
        }

        // Retornar el objeto Participacion con los datos del formulario
        return participacion;
    }

    @FXML
    private void handleSave() {
        List<String> errors = new ArrayList<>();

        String deportistaName = deportistaComboBox.getValue();
        String eventoName = eventoComboBox.getValue();
        String equipoName = equipoComboBox.getValue();

        if (deportistaName == null || deportistaName.isEmpty()) {
            errors.add(bundle.getString("label.name"));
        }
        if (eventoName == null || eventoName.isEmpty()) {
            errors.add(bundle.getString("label.event"));
        }
        if (equipoName == null || equipoName.isEmpty()) {
            errors.add(bundle.getString("label.team"));
        }

        if (!errors.isEmpty()) {
            showAlert(bundle.getString("error.missing_fields") + "\n- " + String.join("\n- ", errors), Alert.AlertType.ERROR);
            return;
        }

        try {
            closeWindow();
            int idDeportista = DeportistaDAO.getIdByName(deportistaName);
            int idEvento = EventoDAO.getIdByName(eventoName);
            int idEquipo = EquipoDAO.getIdByName(equipoName);

            if (idDeportista == -1) {
                showAlert("Error: El deportista seleccionado no existe.", Alert.AlertType.ERROR);
                return;
            }
            if (idEvento == -1) {
                showAlert("Error: El evento seleccionado no existe.", Alert.AlertType.ERROR);
                return;
            }
            if (idEquipo == -1) {
                showAlert("Error: El equipo seleccionado no existe.", Alert.AlertType.ERROR);
                return;
            }

            Participacion participacion = new Participacion();
            participacion.setIdDeportista(idDeportista);
            participacion.setIdEvento(idEvento);
            participacion.setIdEquipo(idEquipo);

            String medalla = oroRadio.isSelected() ? "Oro" : plataRadio.isSelected() ? "Plata" : bronceRadio.isSelected() ? "Bronce" : "Otro";
            participacion.setMedalla(medalla);

            boolean success = editMode ? ParticipacionDAO.updateParticipacion(participacion) : ParticipacionDAO.insertParticipacion(participacion);

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

    public void loadComboBoxData() {
        try {
            deportistaComboBox.setItems(FXCollections.observableArrayList(DeportistaDAO.getAllDeportistaNames()));
            eventoComboBox.setItems(FXCollections.observableArrayList(EventoDAO.getAllEventoNames()));
            equipoComboBox.setItems(FXCollections.observableArrayList(EquipoDAO.getAllEquipoNames()));

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
        deportistaComboBox.getSelectionModel().clearSelection();
        eventoComboBox.getSelectionModel().clearSelection();
        equipoComboBox.getSelectionModel().clearSelection();
        medallaGroup.selectToggle(null); // Desselecciona cualquier opción en el grupo
    }

    private void closeWindow() {
        Stage stage = (Stage) deportistaComboBox.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
