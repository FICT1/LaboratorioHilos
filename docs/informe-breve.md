# Informe breve 

Equipo: 3  Integrantes: Franklin, Cristopher, Juan y Jeremy Fecha: 23/10/2026

**1. Error antes de sincronizar.** Al ejecutar dos incrementadores sobre la misma
variable sin sincronización, puede ocurrir una condición de carrera: ambos leen
el mismo valor y sobrescriben una actualización. En nuestras diez rondas
observamos: [escribir resultados reales y señalar un caso si hubo pérdida].

**2. Cómo se evitó bloquear JavaFX.** Se ejecutaron las ventanillas con
`Thread.start()`. Las pruebas que utilizan `join()` se ejecutan en un hilo
auxiliar. El `Timeline` actualiza el contador y el registro cada 100 ms en el
hilo gráfico. Para los resultados de pruebas se utilizó `Platform.runLater()`.

**3. Condiciones para detener y reiniciar.** `volatile` publica la solicitud de
cancelación y `interrupt()` permite salir de `sleep()`. La aplicación no vuelve a
habilitar **Iniciar** hasta que `isAlive()` sea falso para ambos hilos.
Observación real de nuestro ensayo de detención: [completar].

**4. Mejora para 100 000 solicitudes.** Usaríamos un `ExecutorService` de tamaño
controlado, colas acotadas y lotes de trabajo; separaríamos frecuencia de
procesamiento y refresco gráfico para evitar saturar JavaFX. Mantendríamos
medidas de rendimiento, control de cancelación y pruebas automatizadas.

**Reflexión:** [agregar 2–3 oraciones propias sobre qué aprendió el equipo].
