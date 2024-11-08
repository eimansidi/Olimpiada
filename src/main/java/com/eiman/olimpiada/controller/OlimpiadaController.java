package com.eiman.olimpiada.controller;

import com.eiman.olimpiada.dao.OlimpiadaDAO;
import com.eiman.olimpiada.model.Olimpiada;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controlador para gestionar la lógica de negocio de la entidad Olimpiada.
 */
public class OlimpiadaController {

    private OlimpiadaDAO olimpiadaDAO;
    private boolean editMode;
    private Connection connection;
    private ResourceBundle bundle;
    private Olimpiada olimpiada;

    @FXML
    private TextField nombreField;
    @FXML
    private TextField anioField;
    @FXML
    private TextField ciudadField;
    @FXML
    private RadioButton primaveraRadio;
    @FXML
    private RadioButton veranoRadio;
    @FXML
    private RadioButton otoñoRadio;
    @FXML
    private RadioButton inviernoRadio;

    private ToggleGroup temporadaGroup;

    public OlimpiadaController(){
        this.bundle = ResourceBundle.getBundle("lang.messages");
    }

    @FXML
    public void initialize() {
        // Inicializar el ToggleGroup para los RadioButton de temporada
        temporadaGroup = new ToggleGroup();
        primaveraRadio.setToggleGroup(temporadaGroup);
        veranoRadio.setToggleGroup(temporadaGroup);
        otoñoRadio.setToggleGroup(temporadaGroup);
        inviernoRadio.setToggleGroup(temporadaGroup);
    }

    /**
     * Método para establecer la conexión de base de datos en el controlador.
     *
     * @param connection La conexión a la base de datos.
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
        this.olimpiadaDAO = new OlimpiadaDAO(); // Inicializa OlimpiadaDAO
        OlimpiadaDAO.setConnection(connection); // Configura la conexión en el DAO también
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    /**
     * Configura los datos de la olimpiada en el formulario.
     *
     * @param rowData Datos de la olimpiada como lista observable de strings.
     */
    public void setData(ObservableList<String> rowData) {
        olimpiada = new Olimpiada();  // Inicializa el objeto Olimpiada si aún no está creado
        olimpiada.setId(Integer.parseInt(rowData.get(0)));  // Configura el ID desde rowData
        nombreField.setText(rowData.get(1));
        anioField.setText(rowData.get(2));
        ciudadField.setText(rowData.get(4));

        // Configurar el RadioButton según la temporada
        String temporada = rowData.get(3).toLowerCase();
        switch (temporada) {
            case "primavera":
                temporadaGroup.selectToggle(primaveraRadio);
                break;
            case "verano":
                temporadaGroup.selectToggle(veranoRadio);
                break;
            case "otoño":
                temporadaGroup.selectToggle(otoñoRadio);
                break;
            case "invierno":
                temporadaGroup.selectToggle(inviernoRadio);
                break;
            default:
                temporadaGroup.selectToggle(null);
                break;
        }
    }

    /**
     * Método que guarda o actualiza una olimpiada según el modo (nuevo o edición).
     */
    @FXML
    private void handleSave() {
        if (validateForm()) {
            // Crear una nueva instancia de Olimpiada con los datos del formulario si no existe
            if (olimpiada == null) {
                olimpiada = new Olimpiada();
            }

            olimpiada.setNombre(nombreField.getText());
            olimpiada.setAnio(Integer.parseInt(anioField.getText()));
            olimpiada.setCiudad(ciudadField.getText());
            olimpiada.setTemporada(((RadioButton) temporadaGroup.getSelectedToggle()).getText());

            try {
                boolean success;
                if (editMode) {
                    // Utiliza el ID de olimpiada para actualizar
                    success = olimpiadaDAO.updateOlimpiada(olimpiada);
                } else {
                    success = olimpiadaDAO.insertOlimpiada(olimpiada);
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
    }

    /**
     * Método para validar los campos del formulario de Olimpiada.
     * @return true si el formulario es válido; de lo contrario, false.
     */
    private boolean validateForm() {
        StringBuilder errorMessage = new StringBuilder();

        if (nombreField.getText() == null || nombreField.getText().isEmpty()) {
            errorMessage.append(bundle.getString("error.missing_name")).append("\n");
        }

        try {
            Integer.parseInt(anioField.getText());
        } catch (NumberFormatException e) {
            errorMessage.append(bundle.getString("error.invalid_year")).append("\n");
        }

        if (temporadaGroup.getSelectedToggle() == null) {
            errorMessage.append(bundle.getString("error.missing_season")).append("\n");
        }

        if (ciudadField.getText() == null || ciudadField.getText().isEmpty()) {
            errorMessage.append(bundle.getString("error.missing_city")).append("\n");
        }

        if (errorMessage.length() > 0) {
            showAlert(errorMessage.toString(), Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void clearFields() {
        nombreField.clear();
        anioField.clear();
        temporadaGroup.selectToggle(null);
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
