module com.example {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;

    opens com.example.Controller to javafx.fxml; // Permet l'accès par réflexion à ce package

    exports com.example; // Exporte les classes du package principal (si nécessaire)
    exports com.example.Controller; // Exporte le contrôleur (si d'autres modules en dépendent)
}

