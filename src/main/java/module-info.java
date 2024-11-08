module com.eiman.olimpiada {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // Si usas JDBC para conectarte a MySQL

    opens com.eiman.olimpiada.controller to javafx.fxml; // Permitir acceso a FXMLLoader
    exports com.eiman.olimpiada;
    exports com.eiman.olimpiada.controller;
}
