package ni.edu.uam.laboratoriohilos.app;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import ni.edu.uam.laboratoriohilos.tareas.ExperimentoContador;

/** Actualiza la UI cada 100 ms, sin hacer un runLater por solicitud. */
public final class AplicacionConcurrencia extends Application {
    private SesionTrabajo sesion;
    private Label estado;
    private Label contador;
    private Label estadosHilos;
    private TextArea registro;
    private Button iniciar;
    private Button detener;
    private Button pruebas;
    private Timeline refresco;

    @Override
    public void start(Stage escenario) {
        Label titulo = new Label("Laboratorio de concurrencia · Dos ventanillas");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        estado = new Label("En espera");
        contador = new Label("Operaciones completadas: 0 / 2000");
        estadosHilos = new Label("Ventanillas aún no iniciadas");
        registro = new TextArea();
        registro.setEditable(false);
        registro.setWrapText(true);
        registro.setPrefRowCount(15);
        registro.setStyle("-fx-font-family: 'Consolas', monospace;");

        iniciar = new Button("Iniciar");
        detener = new Button("Detener");
        pruebas = new Button("Probar sincronización (10 rondas)");
        detener.setDisable(true);
        iniciar.setOnAction(evento -> iniciarSesion());
        detener.setOnAction(evento -> detenerSesion());
        pruebas.setOnAction(evento -> ejecutarPruebas());

        HBox botones = new HBox(10, iniciar, detener, pruebas);
        VBox principal = new VBox(12, titulo, estado, contador,
                estadosHilos, botones, new Label("Registro de actividad"), registro);
        principal.setPadding(new Insets(20));
        escenario.setScene(new Scene(principal, 830, 500));
        escenario.setTitle("Concurrencia JavaFX - Laboratorio guiado");

        // El Timeline se ejecuta en el JavaFX Application Thread.
        refresco = new Timeline(new KeyFrame(Duration.millis(100), e -> refrescar()));
        refresco.setCycleCount(Animation.INDEFINITE);
        refresco.play();

        escenario.setOnCloseRequest(e -> {
            if (sesion != null && !sesion.haTerminado()) {
                e.consume(); // Primero detener y volver a cerrar después.
                detenerSesion();
                registro.appendText("Cierre aplazado hasta terminar los hilos.\n");
            } else {
                refresco.stop();
            }
        });
        escenario.show();
    }

    private void iniciarSesion() {
        if (sesion != null && !sesion.haTerminado()) return;
        sesion = new SesionTrabajo(); // No reutilizar Thread terminados.
        registro.clear();
        estado.setText("Ejecutando");
        contador.setText("Operaciones completadas: 0 / 2000");
        iniciar.setDisable(true);
        detener.setDisable(false);
        sesion.iniciar();
    }

    private void detenerSesion() {
        if (sesion == null || sesion.haTerminado() || sesion.fueDetenida()) return;
        estado.setText("Deteniendo");
        detener.setDisable(true);
        sesion.detener();
    }

    private void refrescar() {
        if (sesion == null) return;
        contador.setText("Operaciones completadas: " + sesion.total() + " / 2000");
        estadosHilos.setText(sesion.estados());
        // Consumo agrupado: máximo 80 mensajes por actualización de 100 ms.
        for (int i = 0; i < 80; i++) {
            String evento = sesion.siguienteEvento();
            if (evento == null) break;
            registro.appendText(evento + "\n");
        }
        if (sesion.haTerminado() &&
                (estado.getText().equals("Ejecutando")
                        || estado.getText().equals("Deteniendo"))) {
            estado.setText(sesion.fueDetenida() ? "Detenido" : "Finalizado");
            iniciar.setDisable(false);
            detener.setDisable(true);
            registro.appendText("Sesión terminada; es seguro reiniciar. Total: "
                    + sesion.total() + "\n");
        }
    }

    private void ejecutarPruebas() {
        pruebas.setDisable(true);
        Thread prueba = new Thread(() -> {
            try {
                String informe = ExperimentoContador.ejecutarDiezVeces();
                // Este hilo de pruebas NO modifica directamente controles JavaFX.
                Platform.runLater(() -> {
                    registro.appendText("\n=== Prueba de 10 rondas ===\n" + informe);
                    pruebas.setDisable(false);
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Platform.runLater(() -> {
                    registro.appendText("Pruebas interrumpidas.\n");
                    pruebas.setDisable(false);
                });
            }
        }, "Pruebas-Contador");
        prueba.setDaemon(true);
        prueba.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}