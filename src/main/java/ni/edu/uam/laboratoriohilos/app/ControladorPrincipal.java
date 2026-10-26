package ni.edu.uam.laboratoriohilos.app;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.util.Duration;
import ni.edu.uam.laboratoriohilos.tareas.ExperimentoContador;

/** La interfaz se actualiza únicamente desde el JavaFX Application Thread. */
public final class ControladorPrincipal {
    @FXML private Label etiquetaEstado;
    @FXML private Label etiquetaContador;
    @FXML private Label etiquetaHilos;
    @FXML private TextArea areaRegistro;
    @FXML private Button botonIniciar;
    @FXML private Button botonDetener;
    @FXML private Button botonPruebas;
    @FXML private ProgressBar barraProgreso;

    private SesionTrabajo sesion;
    private Timeline refresco;
    private Runnable accionCerrar;

    @FXML
    private void initialize() {
        etiquetaEstado.setText("En espera");
        etiquetaContador.setText("Operaciones completadas: 0 / 2000");
        etiquetaHilos.setText("Las ventanillas todavía no se han iniciado");
        botonIniciar.setDisable(false);
        botonDetener.setDisable(true);
        barraProgreso.setProgress(0);
        areaRegistro.setEditable(false);
        areaRegistro.setText("Pulsa Iniciar para procesar las solicitudes.\n");

        // Agrupar las actualizaciones visuales: una lectura cada 100 ms.
        refresco = new Timeline(new KeyFrame(Duration.millis(100), ev -> refrescarVista()));
        refresco.setCycleCount(Animation.INDEFINITE);
        refresco.play();
    }

    @FXML
    private void iniciarSesion() {
        if (sesion != null && !sesion.haTerminado()) return;
        sesion = new SesionTrabajo(); // Cada inicio utiliza hilos y contador nuevos.
        areaRegistro.clear();
        etiquetaEstado.setText("Ejecutando");
        etiquetaContador.setText("Operaciones completadas: 0 / 2000");
        barraProgreso.setProgress(0);
        botonIniciar.setDisable(true);
        botonDetener.setDisable(false);
        sesion.iniciar();
    }

    @FXML
    private void detenerSesion() {
        solicitarDetencion();
    }

    private void solicitarDetencion() {
        if (sesion == null || sesion.haTerminado() || sesion.fueDetenida()) return;
        etiquetaEstado.setText("Deteniendo");
        botonDetener.setDisable(true);
        sesion.detener();
    }

    @FXML
    private void ejecutarPruebas() {
        botonPruebas.setDisable(true);
        Thread pruebas = new Thread(() -> {
            try {
                // join() ocurre dentro de este hilo de pruebas, jamás en JavaFX.
                String resultado = ExperimentoContador.ejecutarDiezVeces();
                Platform.runLater(() -> {
                    areaRegistro.appendText("\n=== PRUEBA DE 10 RONDAS ===\n" + resultado);
                    botonPruebas.setDisable(false);
                });
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                Platform.runLater(() -> {
                    areaRegistro.appendText("Prueba interrumpida.\n");
                    botonPruebas.setDisable(false);
                });
            }
        }, "Pruebas-Contador");
        pruebas.setDaemon(true);
        pruebas.start();
    }

    private void refrescarVista() {
        if (sesion == null) return;
        int total = sesion.total();
        etiquetaContador.setText("Operaciones completadas: " + total + " / 2000");
        barraProgreso.setProgress(Math.min(1.0, total / 2000.0));
        etiquetaHilos.setText(sesion.estados());

        // Limitar cantidad de mensajes por pulso de JavaFX.
        for (int i = 0; i < 80; i++) {
            String mensaje = sesion.siguienteEvento();
            if (mensaje == null) break;
            areaRegistro.appendText(mensaje + "\n");
        }

        if (sesion.haTerminado()
                && ("Ejecutando".equals(etiquetaEstado.getText())
                    || "Deteniendo".equals(etiquetaEstado.getText()))) {
            etiquetaEstado.setText(sesion.fueDetenida() ? "Detenido" : "Finalizado");
            botonIniciar.setDisable(false);
            botonDetener.setDisable(true);
            areaRegistro.appendText("Sesión terminada. Total final: " + total
                    + ". Ya es seguro reiniciar.\n");
        }
        if (sesion.haTerminado() && accionCerrar != null) {
            Runnable accion = accionCerrar;
            accionCerrar = null;
            detenerRefresco();
            accion.run();
        }
    }

    public boolean haySesionActiva() {
        return sesion != null && !sesion.haTerminado();
    }

    public void cerrarCuandoTermine(Runnable accion) {
        accionCerrar = accion;
        if (haySesionActiva()) {
            areaRegistro.appendText("Cierre solicitado: deteniendo de forma segura...\n");
            solicitarDetencion();
        } else {
            detenerRefresco();
            accionCerrar = null;
            accion.run();
        }
    }

    public void detenerRefresco() {
        if (refresco != null) refresco.stop();
    }
}
