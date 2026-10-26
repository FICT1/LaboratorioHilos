package ni.edu.uam.laboratoriohilos.tareas;

import ni.edu.uam.laboratoriohilos.app.SesionTrabajo;
import ni.edu.uam.laboratoriohilos.modelo.Contador;
import ni.edu.uam.laboratoriohilos.modelo.ContadorAtomico;
import ni.edu.uam.laboratoriohilos.modelo.ContadorCompartido;

/** Verificación de la lógica concurrente en consola, sin ventana JavaFX. */
public final class VerificacionConcurrencia {
    private VerificacionConcurrencia() { }

    private static void comprobar(Contador contador) throws InterruptedException {
        Runnable sumador = () -> {
            for (int i = 0; i < 1000; i++) contador.incrementar();
        };
        Thread a = new Thread(sumador, "Prueba-A");
        Thread b = new Thread(sumador, "Prueba-B");
        a.start();
        b.start();
        a.join(5000);
        b.join(5000);
        if (a.isAlive() || b.isAlive() || contador.obtener() != 2000) {
            throw new AssertionError("Valor esperado 2000, obtenido " + contador.obtener());
        }
    }

    private static void esperarSesion(SesionTrabajo sesion, long limiteMilisegundos)
            throws InterruptedException {
        long fin = System.nanoTime() + limiteMilisegundos * 1_000_000L;
        while (!sesion.haTerminado() && System.nanoTime() < fin) {
            Thread.sleep(10); // Aquí no estamos dentro de JavaFX.
        }
        if (!sesion.haTerminado()) throw new AssertionError("La sesión no terminó a tiempo");
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            comprobar(new ContadorCompartido());
            comprobar(new ContadorAtomico());
        }
        System.out.println("10 rondas de synchronized y AtomicInteger: 2000 correctos.");

        SesionTrabajo cancelada = new SesionTrabajo();
        cancelada.iniciar();
        cancelada.detener();
        esperarSesion(cancelada, 5000);
        if (!cancelada.fueDetenida()) throw new AssertionError("No se registró la detención");
        System.out.println("Cancelación cooperativa: correcta.");

        SesionTrabajo reiniciada = new SesionTrabajo();
        reiniciada.iniciar();
        esperarSesion(reiniciada, 10000);
        if (reiniciada.total() != 2000 || reiniciada.fueDetenida()) {
            throw new AssertionError("Nueva sesión incorrecta: " + reiniciada.total());
        }
        System.out.println("Reinicio sin hilos previos: 2000 correctos.");
    }
}
