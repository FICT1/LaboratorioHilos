package ni.edu.uam.laboratoriohilos.tareas;

import java.util.EnumSet;
import java.util.Queue;

/** Muestrea getState sin sleep ni join en el JavaFX Application Thread. */
public final class MonitorEstados implements Runnable {
    private final Thread a;
    private final Thread b;
    private final Queue<String> registro;
    private final EnumSet<Thread.State> vistosA = EnumSet.noneOf(Thread.State.class);
    private final EnumSet<Thread.State> vistosB = EnumSet.noneOf(Thread.State.class);

    public MonitorEstados(Thread a, Thread b, Queue<String> registro) {
        this.a = a;
        this.b = b;
        this.registro = registro;
    }

    private void registrarEstado(Thread hilo, EnumSet<Thread.State> vistos) {
        Thread.State estado = hilo.getState();
        if (vistos.add(estado)) {
            registro.offer("Estado observado: " + hilo.getName() + " → " + estado);
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                registrarEstado(a, vistosA);
                registrarEstado(b, vistosB);
                if (a.getState() == Thread.State.TERMINATED
                        && b.getState() == Thread.State.TERMINATED) {
                    return;
                }
                Thread.sleep(40); // Espera del monitor, fuera de JavaFX.
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
