package ni.edu.uam.laboratoriohilos.modelo;
import java.util.concurrent.atomic.AtomicInteger;

/** Alternativa sin synchronized para un contador simple. */
public final class ContadorAtomico implements Contador {
    private final AtomicInteger total = new AtomicInteger(0);

    @Override
    public void incrementar() {
        total.incrementAndGet();
    }

    @Override
    public int obtener() {
        return total.get();
    }
}
