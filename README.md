# Laboratorio de concurrencia en JavaFX

**Repositorio:** FICT1/LaboratorioHilos. **Rama para integración:** `Roman`.

El programa ejecuta dos ventanillas en hilos independientes, incrementa un contador seguro,
observa estados y permite detenerlas mediante `volatile` + `interrupt()`. El refresco JavaFX
se realiza con `Timeline` cada 100 ms; no se bloquea el hilo gráfico con `join()`.

## Entorno y ejecución

- Temurin JDK 17 o superior (recomendado JDK 21).
- Maven Wrapper incluido en el repositorio; se requieren dependencias descargables la primera vez.
- Para abrir la aplicación (Windows):

```powershell
.\mvnw.cmd clean verify
.\mvnw.cmd javafx:run
```

En Linux/macOS use `./mvnw` en lugar de `.\mvnw.cmd`.
La clase principal es `ni.edu.uam.laboratoriohilos.app.AplicacionConcurrencia`.

## Pruebas en consola (Windows)

```powershell
.\mvnw.cmd exec:java -Dexec.mainClass=ni.edu.uam.laboratoriohilos.tareas.DemostracionThreadRunnable
.\mvnw.cmd exec:java -Dexec.mainClass=ni.edu.uam.laboratoriohilos.tareas.DemostracionEstados
.\mvnw.cmd exec:java -Dexec.mainClass=ni.edu.uam.laboratoriohilos.tareas.ExperimentoContador
.\mvnw.cmd exec:java -Dexec.mainClass=ni.edu.uam.laboratoriohilos.tareas.VerificacionConcurrencia
```

`VerificacionConcurrencia` realiza 10 rondas con contadores seguros, comprueba detención
cooperativa y reinicio limpio. Lanza un error si alguna condición falla.

## Trabajo de las ramas

- `Roman`: Thread, Runnable y las dos ventanillas.
- `C.Santana`: estados de los hilos y monitor.
- `Franklin`: contadores, sincronización y experimentos.
- `Jeremy`: interfaz y gestión de sesión.
- `main`: integración previa de esas contribuciones.
