package ni.edu.uam.laboratoriohilos.modelo;

/** Contrato para comparar contadores con distintos mecanismos. */
public interface Contador {
    void incrementar();
    int obtener();
}
