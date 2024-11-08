package com.eiman.olimpiada.controller;

import com.eiman.olimpiada.config.DBConfig;
import com.eiman.olimpiada.dao.*;
import com.eiman.olimpiada.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;
import java.io.IOException;

/**
 * Controlador principal para gestionar las operaciones en la aplicación de olimpiadas.
 * Permite seleccionar tablas dinámicamente y abrir ventanas emergentes para gestionar cada entidad.
 */
public class MainController {

    @FXML
    private ComboBox<String> tablaComboBox;
    @FXML private TextField filterField;
    private FilteredList<ObservableList<String>> filteredData;
    @FXML
    private TableView<ObservableList<String>> tablaView;
    @FXML
    private Button addButton, modifyButton, deleteButton;

    private Connection connection;
    private ResourceBundle bundle;
    private String currentTable;

    /**
     * Inicializa el controlador, configurando las opciones de la interfaz principal.
     */
    public void initialize() throws SQLException {
        setLanguage("es");
        tablaComboBox.setItems(FXCollections.observableArrayList("Deportista", "Equipo", "Evento", "Olimpiada", "Participacion", "Deporte"));
        tablaComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldTable, newTable) -> {
            if (newTable != null) {
                currentTable = newTable; // Actualiza la tabla actual
                loadTable(newTable);     // Carga la tabla seleccionada
            }
        });

        bundle = ResourceBundle.getBundle("lang.messages"); // Cargar archivo de mensajes

        try {
            // Configurar la conexión a la base de datos
            connection = DBConfig.getConnection();
            if (connection != null) {
                // Configuración de conexión en los DAOs
                DeportistaDAO.setConnection(connection);
                EventoDAO.setConnection(connection);
                EquipoDAO.setConnection(connection);
                OlimpiadaDAO.setConnection(connection);
                ParticipacionDAO.setConnection(connection);
                DeporteDAO.setConnection(connection);
            } else {
                showAlert("alert.error_database", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("alert.error_database", Alert.AlertType.ERROR);
            e.printStackTrace();
        }

        // Listener para el filtro se asigna después de cargar la tabla
        filterField.textProperty().addListener((obs, oldValue, newValue) -> filterByName());
    }

    /**
     * Configura la conexión de base de datos.
     *
     * @param connection Conexión de base de datos.
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Carga la tabla seleccionada y prepara `filteredData`.
     *
     * @param tableName Nombre de la tabla a cargar.
     */
    private void loadTable(String tableName) {
        tablaView.getColumns().clear();
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + tableName.toLowerCase();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Configuración de las columnas de la tabla
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int colIndex = i;
                TableColumn<ObservableList<String>, String> column = new TableColumn<>(rs.getMetaData().getColumnName(i + 1));
                column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(colIndex)));
                tablaView.getColumns().add(column);
            }

            // Llenado de datos y configuración de `filteredData`
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }

            // Aplicar filtro a los datos cargados
            filteredData = new FilteredList<>(data, p -> true);
            tablaView.setItems(filteredData);

        } catch (SQLException e) {
            System.err.println("Error al cargar la tabla: " + e.getMessage());
        }
    }

    /**
     * Maneja el filtro de búsqueda en la tabla actual.
     */
    @FXML
    private void filterByName() {
        if (filteredData != null) {
            String filterText = filterField.getText().toLowerCase();
            filteredData.setPredicate(row -> {
                if (filterText.isEmpty()) return true;

                // Ajusta para verificar cualquier columna si contiene el texto
                for (String cell : (ObservableList<String>) row) {
                    if (cell != null && cell.toLowerCase().contains(filterText)) {
                        return true;
                    }
                }
                return false;
            });
        }
    }

    // Cambia el idioma a español
    @FXML
    private void setSpanish() {
        setLanguage("es");
    }

    // Cambia el idioma a inglés
    @FXML
    private void setEnglish() {
        setLanguage("en");
    }

    // Método común para establecer el idioma y actualizar los textos
    private void setLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        bundle = ResourceBundle.getBundle("lang.messages", locale);
        updateTexts();
    }

    // Actualiza los textos de los elementos de la interfaz con el nuevo ResourceBundle
    private void updateTexts() {
        tablaComboBox.setPromptText(bundle.getString("select.table"));
        addButton.setText(bundle.getString("button.add"));
        modifyButton.setText(bundle.getString("button.modify"));
        deleteButton.setText(bundle.getString("button.delete"));
        // Actualizar otros textos de la interfaz según sea necesario
    }

    /**
     * Abre el formulario correspondiente para agregar o modificar un registro.
     *
     * @param action Acción a realizar ("add" para añadir o "modify" para modificar).
     */
    /**
     * Abre el formulario correspondiente para agregar o modificar un registro.
     *
     * @param action Acción a realizar ("add" para añadir o "modify" para modificar).
     */
    private void openForm(String action) {
        String titleKey = "dialog." + currentTable.toLowerCase() + ".title";
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + currentTable + "View.fxml"), bundle);
            Stage dialogStage = new Stage();
            dialogStage.setTitle(bundle.getString(titleKey));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(loader.load()));

            ObservableList<String> selectedRow = null;
            if ("modify".equals(action) && tablaView.getSelectionModel().getSelectedItem() != null) {
                selectedRow = tablaView.getSelectionModel().getSelectedItem();
            }

            switch (currentTable.toLowerCase()) {
                case "deportista":
                    DeportistaController deportistaController = loader.getController();
                    deportistaController.setConnection(connection);
                    if (selectedRow != null) {
                        Deportista deportista = new Deportista();
                        deportista.setId(Integer.parseInt(selectedRow.get(0)));
                        deportista.setNombre(selectedRow.get(1));
                        deportista.setSexo(Deportista.Sexo.valueOf(selectedRow.get(2)));

                        // Manejo de posibles valores null en las columnas
                        if (selectedRow.get(3) != null && !selectedRow.get(3).isEmpty()) {
                            deportista.setEdad(Integer.parseInt(selectedRow.get(3)));
                        } else {
                            deportista.setEdad(0);  // Asigna un valor predeterminado si es null
                        }

                        if (selectedRow.get(4) != null && !selectedRow.get(4).isEmpty()) {
                            deportista.setPeso(Double.parseDouble(selectedRow.get(4)));
                        } else {
                            deportista.setPeso(0.0);  // Asigna un valor predeterminado si es null
                        }

                        if (selectedRow.get(5) != null && !selectedRow.get(5).isEmpty()) {
                            deportista.setAltura(Double.parseDouble(selectedRow.get(5)));
                        } else {
                            deportista.setAltura(0.0);  // Asigna un valor predeterminado si es null
                        }

                        deportistaController.setData(deportista);
                        deportistaController.setEditMode(true);
                    } else {
                        deportistaController.setEditMode(false);
                    }
                    break;

                case "equipo":
                    EquipoController equipoController = loader.getController();
                    equipoController.setConnection(connection);
                    if ("modify".equals(action) && selectedRow != null) {
                        Equipo equipo = new Equipo();
                        equipo.setId(Integer.parseInt(selectedRow.get(0)));
                        equipo.setNombre(selectedRow.get(1));
                        equipo.setIniciales(selectedRow.get(2));
                        equipoController.setData(equipo);
                        equipoController.setEditMode(true);
                    } else {
                        equipoController.setEditMode(false);
                    }
                    break;

                case "evento":
                    EventoController eventoController = loader.getController();
                    eventoController.setConnection(connection);
                    if ("modify".equals(action) && selectedRow != null) {
                        Evento evento = new Evento();
                        evento.setId(Integer.parseInt(selectedRow.get(0)));
                        evento.setNombre(selectedRow.get(1));
                        evento.setIdDeporte(DeporteDAO.getIdByName(selectedRow.get(2)));
                        evento.setIdOlimpiada(OlimpiadaDAO.getIdByName(selectedRow.get(3)));
                        eventoController.setData(evento);
                        eventoController.setEditMode(true);
                    } else {
                        eventoController.setEditMode(false);
                    }
                    break;

                case "olimpiada":
                    OlimpiadaController olimpiadaController = loader.getController();
                    olimpiadaController.setConnection(connection);
                    if (selectedRow != null) {
                        // Crear un objeto Olimpiada con los datos de la fila seleccionada
                        Olimpiada olimpiada = new Olimpiada();
                        olimpiada.setId(Integer.parseInt(selectedRow.get(0)));
                        olimpiada.setNombre(selectedRow.get(1));
                        olimpiada.setAnio(Integer.parseInt(selectedRow.get(2)));
                        olimpiada.setTemporada(selectedRow.get(3));
                        olimpiada.setCiudad(selectedRow.get(4));

                        // Enviar los datos al controlador de Olimpiada para edición
                        olimpiadaController.setData(selectedRow);
                        olimpiadaController.setEditMode(true); // Modo edición
                    } else {
                        olimpiadaController.setEditMode(false); // Modo creación
                    }
                    break;

                case "participacion":
                    ParticipacionController participacionController = loader.getController();
                    participacionController.setConnection(connection);
                    participacionController.loadComboBoxData();  // Carga los ComboBox cada vez que se abre el formulario
                    if (selectedRow != null) {
                        Participacion participacion = new Participacion();
                        participacion.setIdDeportista(Integer.parseInt(selectedRow.get(0)));
                        participacion.setIdEvento(Integer.parseInt(selectedRow.get(1)));
                        participacion.setIdEquipo(Integer.parseInt(selectedRow.get(2)));
                        participacion.setMedalla(selectedRow.get(3));
                        participacionController.setData(selectedRow);  // Cargar datos en el formulario
                        participacionController.setEditMode(true);
                    } else {
                        participacionController.setEditMode(false);
                    }
                    break;

                case "deporte":
                    DeporteController deporteController = loader.getController();
                    deporteController.setConnection(connection);
                    if (selectedRow != null) {
                        // Crear un objeto Deporte con los datos de la fila seleccionada
                        Deporte deporte = new Deporte();
                        deporte.setId(Integer.parseInt(selectedRow.get(0)));
                        deporte.setNombre(selectedRow.get(1));

                        // Enviar datos al controlador de Deporte
                        deporteController.setData(selectedRow);
                        deporteController.setEditMode(true); // Modo edición
                    } else {
                        deporteController.setEditMode(false); // Modo creación
                    }
                    break;

                default:
                    showAlert("No se puede abrir el formulario para esta tabla.", Alert.AlertType.WARNING);
                    return;
            }

            dialogStage.showAndWait();
            loadTable(currentTable);
        } catch (IOException e) {
            showAlert(bundle.getString("alert.error_loading_form"), Alert.AlertType.ERROR);
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Elimina la fila seleccionada en la tabla actual.
     */
    private void deleteSelectedRow() {
        ObservableList<String> selectedRow = tablaView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, bundle.getString("alert.confirm_delete"), ButtonType.YES, ButtonType.NO);
            if (confirmAlert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                try {
                    int id = Integer.parseInt(selectedRow.get(0)); // Asumimos que el ID está en la primera columna
                    Integer id1 = null;

                    // Si es la tabla "participacion", asignamos el segundo ID de la segunda columna
                    if ("participacion".equals(currentTable.toLowerCase()) && selectedRow.size() > 1) {
                        id1 = Integer.parseInt(selectedRow.get(1)); // Asumimos que el ID secundario está en la segunda columna
                    }

                    // Selección dinámica del DAO basado en la tabla actual
                    switch (currentTable.toLowerCase()) {
                        case "deportista":
                            DeportistaDAO.deleteDeportista(id);
                            break;
                        case "equipo":
                            EquipoDAO.deleteEquipo(id);
                            break;
                        case "evento":
                            EventoDAO.deleteEvento(id);
                            break;
                        case "olimpiada":
                            OlimpiadaDAO.deleteOlimpiada(id);
                            break;
                        case "participacion":
                            if (id1 != null) {
                                ParticipacionDAO.deleteParticipacion(id, id1);
                            } else {
                                showAlert("ID del evento no encontrado para eliminar participación.", Alert.AlertType.ERROR);
                                return;
                            }
                            break;
                        case "deporte":
                            DeporteDAO.deleteDeporte(id);
                            break;
                        default:
                            showAlert("No se puede eliminar en esta tabla.", Alert.AlertType.WARNING);
                            return;
                    }

                    loadTable(currentTable); // Recargar la tabla después de eliminar
                    showAlert(bundle.getString("alert.success_delete"), Alert.AlertType.INFORMATION);

                } catch (Exception e) {
                    showAlert(bundle.getString("alert.error_delete"), Alert.AlertType.ERROR);
                    e.printStackTrace();
                }
            }
        } else {
            showAlert(bundle.getString("alert.select_for_deletion"), Alert.AlertType.WARNING);
        }
    }



    /**
     * Método para manejar la acción del botón "Añadir".
     */
    @FXML
    private void handleAddAction() {
        openForm("add");
    }

    /**
     * Método para manejar la acción del botón "Modificar".
     */
    @FXML
    private void handleModifyAction() {
        if (tablaView.getSelectionModel().getSelectedItem() != null) {
            openForm("modify");
        } else {
            showAlert(bundle.getString("alert.select_item"), Alert.AlertType.WARNING);
        }
    }

    /**
     * Método para manejar la acción del botón "Eliminar".
     */
    @FXML
    private void handleDeleteAction() {
        deleteSelectedRow();
    }

    /**
     * Muestra una alerta en pantalla con un mensaje específico.
     *
     * @param message Mensaje a mostrar.
     * @param type    Tipo de alerta (ERROR, WARNING, INFORMATION).
     */
    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
