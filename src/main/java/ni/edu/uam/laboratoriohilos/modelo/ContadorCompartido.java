package ni.edu.uam.laboratoriohilos.modelo;

/** La escritura y la lectura comparten el mismo monitor intrínseco. */
public final class ContadorCompartido implements Contador {
    private int total = 0;

    @Override
    public synchronized void incrementar() {
        total++;
    }

    @Override
    public synchronized int obtener() {
        return total;
    }
}