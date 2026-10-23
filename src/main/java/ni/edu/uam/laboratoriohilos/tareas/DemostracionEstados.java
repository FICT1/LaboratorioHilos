package ni.edu.uam.laboratoriohilos.tareas;

import java.util.concurrent.CountDownLatch;

/** Experimento reproducible de NEW, TIMED_WAITING y TERMINATED. */
public final class DemostracionEstados {
    private DemostracionEstados() { }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch listoParaDormir = new CountDownLatch(1);
        Thread trabajador = new Thread(() -> {
            listoParaDormir.countDown();
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Demo-Estados");

        System.out.println("Antes de start: " + trabajador.getState());
        trabajador.start();
        listoParaDormir.await();
        Thread.sleep(100); // Esperar solo aquí: estamos en un main de consola.
        System.out.println("Durante sleep: " + trabajador.getState());
        trabajador.join();
        System.out.println("Después de join: " + trabajador.getState());
    }
}
