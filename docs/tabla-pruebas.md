Equipo: __________  Fecha: __________  JDK: __________  SO: __________

> Sustituir cada guion por el valor obtenido con `ExperimentoContador`.
> Los valores no están rellenados: no se deben inventar evidencias.

| Ronda | Sin protección | synchronized | AtomicInteger |
|---:|---:|---:|---:|
| 1 | — | — | — |
| 2 | — | — | — |
| 3 | — | — | — |
| 4 | — | — | — |
| 5 | — | — | — |
| 6 | — | — | — |
| 7 | — | — | — |
| 8 | — | — | — |
| 9 | — | — | — |
| 10 | — | — | — |

**Esperado:** 2000 en las alternativas seguras. El contador inseguro puede perder
incrementos, pero la cantidad depende de la planificación de hilos.

## Tabla funcional

| Prueba | Resultado esperado | Resultado real / evidencia |
|---|---|---|
| Dos ventanillas | Ambas trabajan, UI responde | Pendiente de comprobar |
| Conteo | 2000 tras finalización normal | Pendiente de comprobar |
| Cancelación | Detenido; hilos TERMINATED | Pendiente de comprobar |
| Reinicio | Nueva sesión después del fin | Pendiente de comprobar |
| Estados | NEW, activo, TERMINATED y experimento TIMED_WAITING | Pendiente de comprobar |

**Tres capturas requeridas:** `01_dos_ventanillas.png`,
`02_cancelacion_estados.png`, `03_pruebas_10_rondas.png`.
