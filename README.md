# LaboratorioHilos — JavaFX + FXML + Scene Builder + Maven

Proyecto JavaFX basado en la estructura y las tareas del repositorio `FICT1/LaboratorioHilos`
y el laboratorio guiado de concurrencia.

## Requisitos
- **Temurin JDK 21.0.12**, recomendado. Compilación configurada para Java 17.
- **Apache Maven 3.9+**, o IntelliJ IDEA con soporte Maven.
- **JavaFX Scene Builder** para editar el diseño `.fxml` visualmente.

## Abrir en IntelliJ IDEA
1. Descomprimir el ZIP.
2. En IntelliJ: **Open** → seleccionar la carpeta `LaboratorioHilos_Roman_JavaFX_SceneBuilder_Maven`.
3. Aceptar **Load Maven Project** o **Reload All Maven Projects**.
4. Seleccionar JDK 21 como **Project SDK** y **Maven JDK**.
5. Desde Maven, ejecutar **Plugins → javafx → javafx:run**; o en terminal:

```bash
mvn clean javafx:run
```

## Editar interfaz en Scene Builder
1. Instalar JavaFX Scene Builder (por ejemplo, Gluon Scene Builder).
2. Abrir directamente **`src/main/resources/ni/edu/uam/laboratoriohilos/app/vista-principal.fxml`**.
3. Cambiar distribución, botones, etiquetas, colores; el archivo `estilos.css` está en la misma carpeta.
4. Guardar y volver a ejecutar `mvn javafx:run`.
5. Mantener `fx:id`, `fx:controller` y `onAction` o actualizar también `ControladorPrincipal.java`.

> Scene Builder muestra y edita el diseño FXML. La ejecución real de hilos se prueba
> desde IntelliJ/Maven, no desde la vista previa de Scene Builder.

## Comandos Maven
```bash
mvn clean compile
mvn test
mvn javafx:run
mvn exec:java -Dexec.mainClass=ni.edu.uam.laboratoriohilos.tareas.DemostracionThreadRunnable
mvn exec:java -Dexec.mainClass=ni.edu.uam.laboratoriohilos.tareas.DemostracionEstados
mvn exec:java -Dexec.mainClass=ni.edu.uam.laboratoriohilos.tareas.ExperimentoContador
mvn exec:java -Dexec.mainClass=ni.edu.uam.laboratoriohilos.tareas.VerificacionConcurrencia
```

## Qué hace cada módulo
- `app/AplicacionConcurrencia.java`: arranca la ventana cargando FXML.
- `app/ControladorPrincipal.java`: botones, estado, progreso, registro, cancelación y refresco periódico.
- `app/SesionTrabajo.java`: crea y controla los hilos de ambas ventanillas por sesión.
- `modelo`: contador inseguro, sincronizado, atómico y bandera de detención `volatile`.
- `tareas`: `Runnable`, estados, monitor, experimento de 10 rondas y verificaciones.
- `vista-principal.fxml`: **editable visualmente** en Scene Builder.
- `estilos.css`: apariencia de la interfaz; editable en cualquier editor.
- `pom.xml`: resuelve automáticamente JavaFX y JUnit desde Maven Central.

## Organización de cuatro personas (referencia)
- Roman: `Thread`, `Runnable`, proceso de las ventanillas.
- C.Santana: estado de hilos, monitor y prueba `TIMED_WAITING`.
- Franklin: sincronización, `AtomicInteger` y prueba de incrementos.
- Jeremy: aplicación JavaFX y control de sesiones.
