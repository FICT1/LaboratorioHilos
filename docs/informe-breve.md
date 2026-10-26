# Informe breve — completar después de ejecutar el laboratorio

Equipo: ______________  Integrantes: _________________________

**1. ¿Qué error apareció antes de sincronizar?**

Explicar la condición de carrera y anotar una ronda observada: __________

**2. ¿Cómo evitó bloquear JavaFX?**

Indicar que `Thread.start()` ejecuta tareas de fondo; el `Timeline` actualiza
la vista cada 100 ms; nunca hay `join()` en eventos de JavaFX.

**3. ¿Qué condiciones verificó para detener y reiniciar?**

Describir bandera `volatile`, `interrupt()`, `InterruptedException` y
esperar `haTerminado()` antes de iniciar otra sesión. Anotar prueba real.

**4. ¿Qué mejoraría para 100 000 solicitudes?**

Proponer `ExecutorService`, colas limitadas, trabajo por lotes, tiempos medidos
y control del tamaño del registro gráfico.

**Reflexión del equipo:** _________________________________
