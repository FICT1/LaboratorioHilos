package ni.edu.uam.laboratoriohilos;

import javafx.application.Application;
import ni.edu.uam.laboratoriohilos.app.AplicacionConcurrencia;

/** Inicia el laboratorio de concurrencia y no la plantilla Hello. */
public final class Launcher {
    private Launcher() { }

    public static void main(String[] args) {
        Application.launch(AplicacionConcurrencia.class, args);
    }
}
