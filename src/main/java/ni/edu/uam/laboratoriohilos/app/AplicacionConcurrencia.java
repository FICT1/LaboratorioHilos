package ni.edu.uam.laboratoriohilos.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Inicia la vista FXML. Diseño modificable con Scene Builder. */
public final class AplicacionConcurrencia extends Application {
    @Override
    public void start(Stage escenario) throws Exception {
        FXMLLoader cargador = new FXMLLoader(
                AplicacionConcurrencia.class.getResource("vista-principal.fxml"));
        Parent vista = cargador.load();
        ControladorPrincipal controlador = cargador.getController();

        escenario.setTitle("Laboratorio de Hilos · JavaFX");
        escenario.setScene(new Scene(vista, 950, 690));
        escenario.setMinWidth(780);
        escenario.setMinHeight(600);
        escenario.setOnCloseRequest(evento -> {
            if (controlador.haySesionActiva()) {
                evento.consume(); // Sin cierre abrupto; detener cooperativamente.
                controlador.cerrarCuandoTermine(Platform::exit);
            } else {
                controlador.detenerRefresco();
            }
        });
        escenario.show();
    }

    @Override
    public void stop() {
        // Los hilos de trabajo terminan antes de solicitar Platform.exit().
    }

    public static void main(String[] args) {
        launch(args);
    }
}
