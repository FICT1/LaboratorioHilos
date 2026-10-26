package ni.edu.uam.laboratoriohilos;

import javafx.application.Application;
import ni.edu.uam.laboratoriohilos.app.AplicacionConcurrencia;

/** Punto de entrada opcional para ejecución desde un IDE. */
public final class Launcher {
    private Launcher() { }
    public static void main(String[] args) {
        Application.launch(AplicacionConcurrencia.class, args);
    }
}
