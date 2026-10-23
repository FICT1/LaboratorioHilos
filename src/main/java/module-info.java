module ni.edu.uam.laboratoriohilos {
    requires javafx.controls;
    requires javafx.fxml;


    opens ni.edu.uam.laboratoriohilos to javafx.fxml;
    exports ni.edu.uam.laboratoriohilos;
}