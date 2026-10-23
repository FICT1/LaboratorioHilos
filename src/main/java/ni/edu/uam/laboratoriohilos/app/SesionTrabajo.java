package ni.edu.uam.laboratoriohilos.app;

package app;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import modelo.ContadorCompartido;
import modelo.ControlEjecucion;
import tareas.MonitorEstados;
import tareas.ProcesoVentanilla;

/** Encapsula los hilos de UNA sesión; una nueva sesión crea objetos nuevos. */
public final class SesionTrabajo {
    private final ContadorCompartido contador = new ContadorCompartido();
    private final ControlEjecucion control = new ControlEjecucion();
    private final Queue<String> eventos = new ConcurrentLinkedQueue<>();
    private final Thread a;
    private final Thread b;
    private final Thread monitor;
    private boolean iniciada = false; // Accedido solo desde el hilo JavaFX.

    public SesionTrabajo() {
        a = new Thread(new ProcesoVentanilla("Ventanilla A", contador,
                control, eventos, 1_000, 2), "Ventanilla-A");
        b = new Thread(new ProcesoVentanilla("Ventanilla B", contador,
                control, eventos, 1_000, 2), "Ventanilla-B");
        monitor = new Thread(new MonitorEstados(a, b, eventos), "Monitor-Estados");
        monitor.setDaemon(true);
        eventos.offer(a.getName() + " antes de start(): " + a.getState());
        eventos.offer(b.getName() + " antes de start(): " + b.getState());
    }

    public void iniciar() {
        if (iniciada) throw new IllegalStateException("Sesión ya iniciada");
        iniciada = true;
        monitor.start();
        a.start();
        b.start();
    }

    public void detener() {
        if (!iniciada || haTerminado()) return;
        control.detener();   // Señal cooperativa y visible por volatile.
        a.interrupt();      // Despierta un sleep() en curso.
        b.interrupt();
        eventos.offer("Detención solicitada; esperando salida cooperativa");
    }

    public boolean haTerminado() {
        return iniciada && !a.isAlive() && !b.isAlive() && !monitor.isAlive();
    }

    public boolean fueDetenida() {
        return !control.estaActivo();
    }

    public int total() {
        return contador.obtener();
    }

    public String estados() {
        return a.getName() + ": " + a.getState()
                + "   ·   " + b.getName() + ": " + b.getState();
    }

    public String siguienteEvento() {
        return eventos.poll(); // null cuando ya no hay eventos.
    }
}
