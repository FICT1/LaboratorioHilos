package ni.edu.uam.laboratoriohilos.tareas;

/** Ejecutar en consola; nunca llamar a esta demo desde un evento JavaFX. */
public final class DemostracionThreadRunnable {
    private DemostracionThreadRunnable() { }

    private static Runnable crearProceso(String ventanilla) {
        return () -> {
            for (int i = 1; i <= 5; i++) {
                if (Thread.currentThread().isInterrupted()) return;
                System.out.printf("%s | paso %d | hilo %s%n",
                        ventanilla, i, Thread.currentThread().getName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        };
    }

    public static void main(String[] args) throws InterruptedException {
        Thread a = new Thread(crearProceso("Ventanilla A"), "Ventanilla-A");
        Thread b = new Thread(crearProceso("Ventanilla B"), "Ventanilla-B");
        System.out.println("Antes de start(): " + a.getState()); // NEW
        a.start();
        b.start();
        System.out.println("Después de start(): " + a.getState());
        a.join(); // Permitido: este main NO es el hilo JavaFX.
        b.join();
        System.out.println("Al terminar: " + a.getState()); // TERMINATED

        System.out.println("\nComparación: llamar run() no crea otro hilo");
        Thread ejemplo = new Thread(crearProceso("Directo run()"), "No-se-iniciara");
        ejemplo.run(); // Sus mensajes tendrán el nombre del hilo main.
        System.out.println("Estado de ejemplo sin start(): " + ejemplo.getState());
    }
}
