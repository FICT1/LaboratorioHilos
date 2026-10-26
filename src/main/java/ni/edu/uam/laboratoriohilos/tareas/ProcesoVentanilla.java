package ni.edu.uam.laboratoriohilos.tareas;

import java.util.Queue;
import ni.edu.uam.laboratoriohilos.modelo.Contador;
import ni.edu.uam.laboratoriohilos.modelo.ControlEjecucion;

/** Cada ventanilla trabaja en un Thread y nunca accede a controles JavaFX. */
public final class ProcesoVentanilla implements Runnable {
    private final String nombre;
    private final Contador contador;
    private final ControlEjecucion control;
    private final Queue<String> registro;
    private final int limite;
    private final int pausaMillis;

    public ProcesoVentanilla(String nombre, Contador contador,
            ControlEjecucion control, Queue<String> registro,
            int limite, int pausaMillis) {
        this.nombre = nombre;
        this.contador = contador;
        this.control = control;
        this.registro = registro;
        this.limite = limite;
        this.pausaMillis = pausaMillis;
    }

    @Override
    public void run() {
        int realizadas = 0;
        registro.offer(nombre + " comienza en " + Thread.currentThread().getName());
        try {
            for (int i = 1; i <= limite; i++) {
                if (!control.estaActivo() || Thread.currentThread().isInterrupted()) {
                    break;
                }
                // Se incrementa completamente antes de comprobar una nueva cancelación.
                contador.incrementar();
                realizadas++;
                if (realizadas == 1 || realizadas % 250 == 0 || realizadas == limite) {
                    registro.offer(nombre + " completó " + realizadas + " solicitudes");
                }
                if (pausaMillis > 0) {
                    Thread.sleep(pausaMillis);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // No perder la señal de interrupción.
            registro.offer(nombre + " recibió interrupción durante sleep()");
        } finally {
            registro.offer(nombre + " terminó con " + realizadas + " solicitudes");
        }
    }
}
