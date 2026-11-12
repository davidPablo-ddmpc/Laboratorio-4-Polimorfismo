# Lab 4 - Polimorfismo: ArenaGT Lite

Programa de consola en Java que lleva la copa deportiva de ArenaGT: equipos, encuentros de
fútbol sala y baloncesto, exhibiciones, calendario y tabla de posiciones.

CC2008 - Programación Orientada a Objetos, Universidad del Valle de Guatemala.

## Requisitos

- JDK 18 o superior. Desde esa versión Java lee los archivos como UTF-8 por defecto, así que
  las tildes del código se compilan bien sin opciones extra. Para ver su versión: `java -version`.

## Compilar y ejecutar

1. Abra una terminal en la carpeta del proyecto (la que contiene `src`).
2. Compile. Los archivos `.class` se guardan en la carpeta `bin`, que se crea sola:

   ```bash
   javac -d bin src/*.java
   ```

3. Ejecute el programa:

   ```bash
   java -cp bin Main
   ```

Si las tildes se ven mal en la consola de Windows, ejecute `chcp 65001` antes de correr el programa.
Si usa un JDK 17 o anterior, compile con `javac -encoding UTF-8 -d bin src/*.java`.

## Estructura

| Archivo | Qué es |
|---|---|
| `src/Programable.java` | Interfaz del calendario (encuentros y exhibiciones). Trae el orden cronológico por defecto. |
| `src/Puntuable.java` | Interfaz de la tabla de posiciones (solo encuentros). |
| `src/Encuentro.java` | Clase abstracta con los dos equipos, el marcador, el estado y la programación. |
| `src/FutbolSala.java`, `src/Baloncesto.java` | Reglas de marcador y de puntos de tabla de cada deporte. |
| `src/Exhibicion.java` | Evento que solo va al calendario. |
| `src/Horario.java` | Fecha, hora y sede; aquí está el único criterio de orden del calendario. |
| `src/Equipo.java` | Equipo comparable por puntos y nombre. |
| `src/Copa.java` | Guarda equipos y eventos, valida las operaciones y se recorre por su calendario. |
| `src/EstadoEvento.java` | Estados: sin programar, programado y finalizado. |
| `src/Main.java` | Menú de consola y datos iniciales (método `main`). |

## Uso

Al iniciar, el programa ya tiene 4 equipos, 7 encuentros (5 con resultado) y 2 exhibiciones.
El menú permite:

1. Registrar equipo
2. Crear encuentro
3. Crear exhibición
4. Programar o reprogramar evento (fecha en formato `AAAA-MM-DD`, hora de 0 a 23)
5. Registrar resultado de un encuentro
6. Mostrar tabla de posiciones
7. Mostrar calendario
8. Ver todos los eventos
0. Salir

Los datos solo se guardan en memoria mientras el programa está abierto.
