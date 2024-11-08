package com.eiman.olimpiada.controller;

import com.eiman.olimpiada.dao.DeporteDAO;
import com.eiman.olimpiada.dao.DeportistaDAO;
import com.eiman.olimpiada.model.Deportista;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controlador para gestionar las operaciones de creación y edición de deportistas.
 */
public class DeportistaController {

    private ResourceBundle bundle;
    private boolean editMode = false; // Indica si estamos en modo edición
    private Deportista deportista;
    private Connection connection;

    @FXML
    private TextField nombreField;
    @FXML
    private RadioButton masculinoRadio, femeninoRadio;
    @FXML
    private TextField edadField;
    @FXML
    private TextField pesoField;
    @FXML
    private TextField alturaField;
    @FXML
    private ImageView fotoImageView;

    private byte[] fotoBytes;
    private ToggleGroup sexoGroup;
    private DeporteDAO deporteDAO;

    public DeportistaController() {
        this.bundle = ResourceBundle.getBundle("lang.messages", Locale.getDefault());
    }

    /**
     * Establece la conexión a la base de datos.
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
        this.deporteDAO = new DeporteDAO();
    }

    @FXML
    public void initialize() {
        // Configuración del ToggleGroup para los RadioButtons de sexo
        sexoGroup = new ToggleGroup();
        masculinoRadio.setToggleGroup(sexoGroup);
        femeninoRadio.setToggleGroup(sexoGroup);
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    /**
     * Establece los datos del objeto Deportista en los campos del formulario.
     */
    public void setData(Deportista deportista) {
        this.deportista = deportista; // Asigna el objeto recibido

        // Cargar datos en los campos del formulario
        nombreField.setText(deportista.getNombre());
        edadField.setText(String.valueOf(deportista.getEdad()));
        pesoField.setText(String.valueOf(deportista.getPeso()));
        alturaField.setText(String.valueOf(deportista.getAltura()));
        masculinoRadio.setSelected(deportista.getSexo() == Deportista.Sexo.M);
        femeninoRadio.setSelected(deportista.getSexo() == Deportista.Sexo.F);
    }

    /**
     * Obtiene los datos actuales del formulario y crea un objeto Deportista.
     *
     * @return Un objeto Deportista con los datos del formulario.
     */
    public Deportista getData() {
        Deportista deportista = new Deportista();
        deportista.setNombre(nombreField.getText());
        deportista.setEdad(Integer.parseInt(edadField.getText()));
        deportista.setPeso(Double.parseDouble(pesoField.getText()));
        deportista.setAltura(Double.parseDouble(alturaField.getText()));

        if (masculinoRadio.isSelected()) {
            deportista.setSexo(Deportista.Sexo.M);
        } else if (femeninoRadio.isSelected()) {
            deportista.setSexo(Deportista.Sexo.F);
        }

        return deportista;
    }

    @FXML
    private void handleSelectPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("button.select_photo"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(nombreField.getScene().getWindow());
        if (selectedFile != null) {
            try {
                fotoBytes = Files.readAllBytes(selectedFile.toPath());
                Image image = new Image(new FileInputStream(selectedFile));
                fotoImageView.setImage(image);
            } catch (IOException e) {
                showAlert(bundle.getString("alert.error_loading_image"), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    /**
     * Maneja la acción de guardar el deportista, validando los datos antes de enviar al DAO.
     */
    @FXML
    private void handleSave() {
        if (this.deportista == null) {
            this.deportista = new Deportista(); // Inicializar si está en null
        }

        List<String> errors = new ArrayList<>();
        ResourceBundle bundle = ResourceBundle.getBundle("lang.messages", Locale.getDefault());

        // Validación de campos
        String nombre = nombreField.getText();
        if (nombre.isEmpty()) {
            errors.add(bundle.getString("label.name"));
        }

        // Mostrar errores si existen
        if (!errors.isEmpty()) {
            showAlert(bundle.getString("error.missing_fields") + "\n- " + String.join("\n- ", errors), Alert.AlertType.ERROR);
            return;
        }

        // Establecer los datos en el objeto deportista
        this.deportista.setNombre(nombre);
        this.deportista.setEdad(Integer.parseInt(edadField.getText()));
        this.deportista.setPeso(Double.parseDouble(pesoField.getText()));
        this.deportista.setAltura(Double.parseDouble(alturaField.getText()));
        this.deportista.setSexo(masculinoRadio.isSelected() ? Deportista.Sexo.M : Deportista.Sexo.F);

        try {
            closeWindow();
            boolean success;
            if (editMode) {
                success = DeportistaDAO.updateDeportista(deportista); // Actualizar si es modo edición
            } else {
                success = DeportistaDAO.insertDeportista(deportista); // Insertar si es nuevo
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
     * Cierra el formulario sin realizar ninguna acción.
     */
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    /**
     * Limpia todos los campos del formulario.
     */
    private void clearFields() {
        nombreField.clear();
        sexoGroup.selectToggle(null);
        edadField.clear();
        pesoField.clear();
        alturaField.clear();
        fotoImageView.setImage(null);
        fotoBytes = null;
    }

    private void closeWindow() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }

    /**
     * Valida los campos del formulario para asegurarse de que contienen datos válidos.
     *
     * @return Una lista de mensajes de error si los campos no son válidos; lista vacía si todos los campos son válidos.
     */
    private List<String> validateFields() {
        List<String> errors = new ArrayList<>();

        // Validar el campo de nombre
        if (nombreField.getText().isEmpty()) {
            errors.add(bundle.getString("label.name"));
        }

        // Validar el campo de sexo usando los RadioButtons
        if (!masculinoRadio.isSelected() && !femeninoRadio.isSelected()) {
            errors.add(bundle.getString("error.invalid_sex"));
        }

        // Validar edad (debe ser un número positivo)
        try {
            int edad = Integer.parseInt(edadField.getText());
            if (edad <= 0) {
                errors.add(bundle.getString("label.age"));
            }
        } catch (NumberFormatException e) {
            errors.add(bundle.getString("error.invalid_number").replace("{0}", bundle.getString("label.age")));
        }

        // Validar peso (debe ser un número positivo)
        try {
            double peso = Double.parseDouble(pesoField.getText());
            if (peso <= 0) {
                errors.add(bundle.getString("label.weight"));
            }
        } catch (NumberFormatException e) {
            errors.add(bundle.getString("error.invalid_number").replace("{0}", bundle.getString("label.weight")));
        }

        // Validar altura (debe ser un número positivo)
        try {
            double altura = Double.parseDouble(alturaField.getText());
            if (altura <= 0) {
                errors.add(bundle.getString("label.height"));
            }
        } catch (NumberFormatException e) {
            errors.add(bundle.getString("error.invalid_number").replace("{0}", bundle.getString("label.height")));
        }

        return errors;
    }


    /**
     * Muestra una alerta en pantalla con un mensaje específico.
     *
     * @param message Mensaje a mostrar.
     * @param alertType Tipo de alerta (ERROR, WARNING, INFORMATION).
     */
    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
