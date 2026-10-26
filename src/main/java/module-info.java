module ni.edu.uam.laboratoriohilos {
    requires javafx.controls;
    requires javafx.fxml;

    exports ni.edu.uam.laboratoriohilos;
    exports ni.edu.uam.laboratoriohilos.app;
    exports ni.edu.uam.laboratoriohilos.modelo;
    exports ni.edu.uam.laboratoriohilos.tareas;

    opens ni.edu.uam.laboratoriohilos to javafx.fxml;
}
