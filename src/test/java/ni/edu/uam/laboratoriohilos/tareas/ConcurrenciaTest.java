package ni.edu.uam.laboratoriohilos.tareas;

import ni.edu.uam.laboratoriohilos.modelo.ContadorCompartido;
import ni.edu.uam.laboratoriohilos.modelo.ContadorAtomico;
import ni.edu.uam.laboratoriohilos.modelo.Contador;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ConcurrenciaTest {
    private void sumarEnParalelo(Contador contador) throws InterruptedException {
        Runnable proceso = () -> {
            for (int i = 0; i < 1000; i++) contador.incrementar();
        };
        Thread a = new Thread(proceso, "Prueba-A");
        Thread b = new Thread(proceso, "Prueba-B");
        a.start(); b.start();
        a.join(5000); b.join(5000);
        assertFalse(a.isAlive() || b.isAlive(), "Hilos sin finalizar");
        assertEquals(2000, contador.obtener());
    }

    @Test void contadorSincronizadoNoPierdeIncrementos() throws InterruptedException {
        for (int i = 0; i < 10; i++) sumarEnParalelo(new ContadorCompartido());
    }

    @Test void contadorAtomicoNoPierdeIncrementos() throws InterruptedException {
        for (int i = 0; i < 10; i++) sumarEnParalelo(new ContadorAtomico());
    }
}
