package ni.edu.uam.laboratoriohilos.tareas;

import ni.edu.uam.laboratoriohilos.app.SesionTrabajo;
import ni.edu.uam.laboratoriohilos.modelo.Contador;
import ni.edu.uam.laboratoriohilos.modelo.ContadorAtomico;
import ni.edu.uam.laboratoriohilos.modelo.ContadorCompartido;

/** Verifica conteo, cancelación cooperativa y reinicio, sin interfaz gráfica. */
public final class VerificacionConcurrencia {
    private VerificacionConcurrencia() { }

    private static void verificarContador(Contador contador) throws InterruptedException {
        Runnable tarea = () -> {
            for (int i = 0; i < 1_000; i++) contador.incrementar();
        };
        Thread a = new Thread(tarea, "Verificacion-A");
        Thread b = new Thread(tarea, "Verificacion-B");
        a.start();
        b.start();
        a.join(5_000);
        b.join(5_000);
        if (a.isAlive() || b.isAlive() || contador.obtener() != 2_000) {
            throw new AssertionError("Contador incorrecto: " + contador.obtener());
        }
    }

    private static void esperarFin(SesionTrabajo sesion, long timeoutMs) throws InterruptedException {
        long limite = System.nanoTime() + timeoutMs * 1_000_000L;
        while (!sesion.haTerminado() && System.nanoTime() < limite) {
            Thread.sleep(10); // Este código se ejecuta fuera del hilo JavaFX.
        }
        if (!sesion.haTerminado()) throw new AssertionError("Los hilos no terminaron dentro del plazo");
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            verificarContador(new ContadorCompartido());
            verificarContador(new ContadorAtomico());
        }
        System.out.println("Contadores seguros: 10 rondas de 2000 correctas.");

        SesionTrabajo cancelada = new SesionTrabajo();
        cancelada.iniciar();
        cancelada.detener();
        esperarFin(cancelada, 5_000);
        if (!cancelada.fueDetenida()) throw new AssertionError("La sesión no registró cancelación");
        System.out.println("Cancelación cooperativa: hilos finalizados.");

        SesionTrabajo nueva = new SesionTrabajo();
        nueva.iniciar();
        esperarFin(nueva, 10_000);
        if (nueva.total() != 2_000 || nueva.fueDetenida()) {
            throw new AssertionError("Reinicio falló; total=" + nueva.total());
        }
        System.out.println("Reinicio: 2000 operaciones, sin hilos pendientes.");
    }
}
