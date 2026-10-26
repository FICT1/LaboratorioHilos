package ni.edu.uam.laboratoriohilos.modelo;

/** Volatile garantiza visibilidad de la solicitud de detención. */
public final class ControlEjecucion {
    private volatile boolean activo = true;

    public boolean estaActivo() {
        return activo;
    }

    public void detener() {
        activo = false;
    }
}
