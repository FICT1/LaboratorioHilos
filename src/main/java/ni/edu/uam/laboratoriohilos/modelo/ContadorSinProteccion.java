package ni.edu.uam.laboratoriohilos.modelo;

/** Solo para reproducir y estudiar una condición de carrera. */
public final class ContadorSinProteccion implements Contador {
    private int total = 0;

    @Override
    public void incrementar() {
        int anterior = total;
        Thread.yield(); // Amplía intencionalmente la ventana de carrera.
        total = anterior + 1;
    }

    @Override
    public int obtener() {
        return total;
    }
}
