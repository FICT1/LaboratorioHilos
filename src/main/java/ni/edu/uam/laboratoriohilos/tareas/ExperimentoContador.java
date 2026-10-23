package ni.edu.uam.laboratoriohilos.tareas;
import modelo.Contador;
import modelo.ContadorAtomico;
import modelo.ContadorCompartido;
import modelo.ContadorSinProteccion;

/** Diez rondas: 2 hilos x 1 000 incrementos = 2 000 esperados. */
public final class ExperimentoContador {
    private ExperimentoContador() { }

    private static int ejecutarRonda(Contador contador) throws InterruptedException {
        Runnable sumarMil = () -> {
            for (int i = 0; i < 1_000; i++) {
                contador.incrementar();
            }
        };
        Thread a = new Thread(sumarMil, "Prueba-A");
        Thread b = new Thread(sumarMil, "Prueba-B");
        a.start();
        b.start();
        // Los join() corren en consola o en el hilo auxiliar de pruebas.
        a.join();
        b.join();
        return contador.obtener();
    }

    public static String ejecutarDiezVeces() throws InterruptedException {
        StringBuilder salida = new StringBuilder();
        salida.append("Ronda | Sin protección | synchronized | AtomicInteger\n");
        salida.append("------------------------------------------------------\n");
        for (int ronda = 1; ronda <= 10; ronda++) {
            int inseguro = ejecutarRonda(new ContadorSinProteccion());
            int protegido = ejecutarRonda(new ContadorCompartido());
            int atomico = ejecutarRonda(new ContadorAtomico());
            salida.append(String.format("%5d | %14d | %12d | %13d%n",
                    ronda, inseguro, protegido, atomico));
        }
        salida.append("Esperado en las columnas seguras: 2000 en todas las rondas.\n");
        salida.append("El fallo sin protección depende de la planificación; no es fijo.\n");
        return salida.toString();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.print(ejecutarDiezVeces());
    }
}
