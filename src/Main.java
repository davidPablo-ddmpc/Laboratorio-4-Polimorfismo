import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

// El programa de consola: carga los datos iniciales y muestra el menú.
// Es la única clase que imprime en pantalla y la única que sabe que existen
// FutbolSala y Baloncesto (porque aquí es donde se crean los encuentros).
public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Copa copa = new Copa();

    public static void main(String[] args) {
        cargarDatosIniciales();
        System.out.println("==============================================");
        System.out.println("   Copa ArenaGT - Fútbol sala y Baloncesto");
        System.out.println("==============================================");
        System.out.println("Se cargaron los datos iniciales de la copa.");

        // El menú se repite hasta que el usuario elija 0.
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Elija una opción: ");
            System.out.println();
            switch (opcion) {
                case 1:
                    registrarEquipo();
                    break;
                case 2:
                    crearEncuentro();
                    break;
                case 3:
                    crearExhibicion();
                    break;
                case 4:
                    programarEvento();
                    break;
                case 5:
                    registrarResultado();
                    break;
                case 6:
                    mostrarTablaPosiciones();
                    break;
                case 7:
                    mostrarCalendario();
                    break;
                case 8:
                    mostrarEventos();
                    break;
                case 0:
                    System.out.println("¡Hasta pronto!");
                    break;
                default:
                    System.out.println("Opción no válida. Elija un número del 0 al 8.");
            }
        } while (opcion != 0);
        scanner.close();
    }

    // Los datos se cargan con los mismos métodos que usa el menú, así pasan por las mismas revisiones.
    // Con estos resultados la tabla arranca: Faro 4, Luna 4, Sol 3, Cumbre 2.
    private static void cargarDatosIniciales() {
        // Luna se registra antes que Faro para que se note el desempate por nombre:
        // los dos terminan con 4 puntos y Faro debe salir primero.
        copa.registrarEquipo("LUN", "Luna", "Agroexport");
        copa.registrarEquipo("SOL", "Sol", "TecnoSoft");
        copa.registrarEquipo("FAR", "Faro", "TecnoSoft");
        copa.registrarEquipo("CUM", "Cumbre", "Agroexport");

        Equipo luna = copa.buscarEquipo("LUN");
        Equipo sol = copa.buscarEquipo("SOL");
        Equipo faro = copa.buscarEquipo("FAR");
        Equipo cumbre = copa.buscarEquipo("CUM");

        // El último evento agregado siempre es el último de la lista, por eso se usa size() como número.

        // Los dos ejemplos del enunciado: Faro gana 3-1 en fútbol sala y Sol gana 78-74 en baloncesto.
        copa.agregarEncuentro(new FutbolSala(faro, sol));
        copa.programarEvento(copa.getEventos().size(), LocalDate.of(2026, 11, 12), 9, "Gimnasio Norte");
        copa.registrarResultado(copa.getEncuentros().size(), 3, 1);

        copa.agregarEncuentro(new Baloncesto(sol, faro));
        copa.programarEvento(copa.getEventos().size(), LocalDate.of(2026, 11, 12), 14, "Gimnasio Norte");
        copa.registrarResultado(copa.getEncuentros().size(), 78, 74);

        // Un empate en fútbol sala: 1 punto para cada uno.
        copa.agregarEncuentro(new FutbolSala(luna, cumbre));
        copa.programarEvento(copa.getEventos().size(), LocalDate.of(2026, 11, 13), 10, "Gimnasio Norte");
        copa.registrarResultado(copa.getEncuentros().size(), 2, 2);

        copa.agregarEncuentro(new Baloncesto(luna, cumbre));
        copa.programarEvento(copa.getEventos().size(), LocalDate.of(2026, 11, 14), 16, "Cancha Central");
        copa.registrarResultado(copa.getEncuentros().size(), 65, 60);

        // Otro empate; con este Luna llega a 4 y queda igualada con Faro.
        copa.agregarEncuentro(new FutbolSala(sol, luna));
        copa.programarEvento(copa.getEventos().size(), LocalDate.of(2026, 11, 15), 11, "Gimnasio Norte");
        copa.registrarResultado(copa.getEncuentros().size(), 1, 1);

        // Este queda programado pero sin resultado, para poder probar registrarlo desde el menú.
        copa.agregarEncuentro(new Baloncesto(faro, cumbre));
        copa.programarEvento(copa.getEventos().size(), LocalDate.of(2026, 11, 20), 18, "Cancha Central");

        // Y este ni siquiera está programado, para probar que no acepta resultados todavía.
        copa.agregarEncuentro(new FutbolSala(sol, cumbre));

        // Las exhibiciones se agregan al final a propósito: en el calendario igual salen mezcladas por fecha.
        // La clínica es a la misma hora que Faro vs Sol, pero en Cancha Central, así que sale antes por la sede.
        copa.agregarEvento(new Exhibicion("Clínica de baloncesto"));
        copa.programarEvento(copa.getEventos().size(), LocalDate.of(2026, 11, 12), 9, "Cancha Central");

        copa.agregarEvento(new Exhibicion("Demostración de fútbol sala"));
        copa.programarEvento(copa.getEventos().size(), LocalDate.of(2026, 11, 13), 8, "Cancha Central");
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("----------------- MENÚ -----------------");
        System.out.println("1. Registrar equipo");
        System.out.println("2. Crear encuentro");
        System.out.println("3. Crear exhibición");
        System.out.println("4. Programar o reprogramar evento");
        System.out.println("5. Registrar resultado de un encuentro");
        System.out.println("6. Mostrar tabla de posiciones");
        System.out.println("7. Mostrar calendario");
        System.out.println("8. Ver todos los eventos");
        System.out.println("0. Salir");
        System.out.println("----------------------------------------");
    }

    // En todas las opciones del menú la idea es la misma: pedir los datos, intentar la operación
    // y, si la copa la rechaza, mostrar el mensaje del error sin cerrar el programa.

    private static void registrarEquipo() {
        System.out.println("--- Registrar equipo ---");
        String codigo = leerTexto("Código: ");
        String nombre = leerTexto("Nombre: ");
        String empresa = leerTexto("Empresa: ");
        try {
            copa.registrarEquipo(codigo, nombre, empresa);
            System.out.println("Equipo registrado: " + copa.buscarEquipo(codigo));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void crearEncuentro() {
        System.out.println("--- Crear encuentro ---");
        if (copa.getEquipos().size() < 2) {
            System.out.println("Error: se necesitan al menos dos equipos registrados.");
            return;
        }
        System.out.println("Deportes: 1. Fútbol sala   2. Baloncesto");
        int deporte = leerEntero("Deporte: ");
        if (deporte != 1 && deporte != 2) {
            System.out.println("Error: opción de deporte no válida. Elija 1 o 2.");
            return;
        }
        mostrarEquipos();
        String codigoLocal = leerTexto("Código del equipo local: ");
        Equipo local = copa.buscarEquipo(codigoLocal);
        if (local == null) {
            System.out.println("Error: no existe un equipo con el código \"" + codigoLocal + "\".");
            return;
        }
        String codigoVisitante = leerTexto("Código del equipo visitante: ");
        Equipo visitante = copa.buscarEquipo(codigoVisitante);
        if (visitante == null) {
            System.out.println("Error: no existe un equipo con el código \"" + codigoVisitante + "\".");
            return;
        }
        try {
            // Este es el único lugar del menú donde se elige el deporte.
            // Para agregar voleibol, por ejemplo, bastaría con otra opción aquí y su clase.
            Encuentro encuentro;
            if (deporte == 1) {
                encuentro = new FutbolSala(local, visitante);
            } else {
                encuentro = new Baloncesto(local, visitante);
            }
            copa.agregarEncuentro(encuentro);
            System.out.println("Encuentro creado como evento #" + copa.getEventos().size()
                    + " (encuentro #" + copa.getEncuentros().size() + "): " + encuentro);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void crearExhibicion() {
        System.out.println("--- Crear exhibición ---");
        String titulo = leerTexto("Título: ");
        try {
            Exhibicion exhibicion = new Exhibicion(titulo);
            copa.agregarEvento(exhibicion);
            System.out.println("Exhibición creada como evento #" + copa.getEventos().size() + ": " + exhibicion);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void programarEvento() {
        System.out.println("--- Programar / reprogramar evento ---");
        ArrayList<Programable> eventos = copa.getEventos();
        if (eventos.isEmpty()) {
            System.out.println("No hay eventos creados.");
            return;
        }
        mostrarEventos();
        int numero = leerEntero("Número de evento: ");
        // Se revisa el número antes de pedir lo demás, para no hacerle escribir todo al usuario en vano.
        if (numero < 1 || numero > eventos.size()) {
            System.out.println("Error: no existe el evento número " + numero + ".");
            return;
        }
        LocalDate fecha = leerFecha("Fecha (AAAA-MM-DD): ");
        int hora = leerEntero("Hora de inicio (0-23): ");
        String sede = leerTexto("Sede: ");
        try {
            copa.programarEvento(numero, fecha, hora, sede);
            System.out.println("Evento programado: " + eventos.get(numero - 1));
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void registrarResultado() {
        System.out.println("--- Registrar resultado ---");
        ArrayList<Puntuable> encuentros = copa.getEncuentros();
        if (encuentros.isEmpty()) {
            System.out.println("No hay encuentros creados.");
            return;
        }
        for (int i = 0; i < encuentros.size(); i++) {
            System.out.println((i + 1) + ". " + encuentros.get(i));
        }
        int numero = leerEntero("Número de encuentro: ");
        if (numero < 1 || numero > encuentros.size()) {
            System.out.println("Error: no existe el encuentro número " + numero + ".");
            return;
        }
        int marcadorLocal = leerEntero("Marcador del local: ");
        int marcadorVisitante = leerEntero("Marcador del visitante: ");
        try {
            copa.registrarResultado(numero, marcadorLocal, marcadorVisitante);
            System.out.println("Resultado registrado: " + encuentros.get(numero - 1));
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // La copa ya entrega la tabla ordenada; aquí solo se le da formato.
    private static void mostrarTablaPosiciones() {
        System.out.println("--- Tabla de posiciones ---");
        ArrayList<Equipo> tabla = copa.getTablaPosiciones();
        if (tabla.isEmpty()) {
            System.out.println("No hay equipos registrados.");
            return;
        }
        System.out.printf("%-4s %-8s %-20s %-20s %6s%n", "Pos", "Código", "Equipo", "Empresa", "Puntos");
        int posicion = 1;
        for (Equipo equipo : tabla) {
            System.out.printf("%-4d %-8s %-20s %-20s %6d%n", posicion, equipo.getCodigo(),
                    equipo.getNombre(), equipo.getEmpresa(), equipo.getPuntosTabla());
            posicion++;
        }
    }

    // Gracias a que Copa es Iterable, el calendario se recorre con un for-each directo sobre la copa.
    // Aquí no se ordena ni se filtra nada: eso ya lo hizo la copa.
    private static void mostrarCalendario() {
        System.out.println("--- Calendario ---");
        boolean hayEventos = false;
        for (Programable evento : copa) {
            System.out.println(evento.getHorario() + "  |  " + evento.getDescripcion());
            hayEventos = true;
        }
        if (!hayEventos) {
            System.out.println("No hay eventos programados.");
        }
    }

    // Lista todos los eventos, programados o no, con el número que se usa para programarlos.
    private static void mostrarEventos() {
        System.out.println("Eventos:");
        ArrayList<Programable> eventos = copa.getEventos();
        if (eventos.isEmpty()) {
            System.out.println("  (no hay eventos)");
        }
        for (int i = 0; i < eventos.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + eventos.get(i));
        }
    }

    private static void mostrarEquipos() {
        System.out.println("Equipos:");
        ArrayList<Equipo> equipos = copa.getEquipos();
        if (equipos.isEmpty()) {
            System.out.println("  (no hay equipos)");
        }
        for (Equipo equipo : equipos) {
            System.out.println("  " + equipo);
        }
    }

    // Todas las lecturas pasan por aquí. Si la entrada se acaba (por ejemplo, al pasar
    // las pruebas desde un archivo), el programa se despide en lugar de caerse con un error.
    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        if (!scanner.hasNextLine()) {
            System.out.println();
            System.out.println("Fin de la entrada. Saliendo del programa.");
            System.exit(0);
        }
        return scanner.nextLine().trim();
    }

    // Si el usuario escribe letras donde va un número, se le vuelve a preguntar.
    private static int leerEntero(String mensaje) {
        while (true) {
            String linea = leerTexto(mensaje);
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida: \"" + linea + "\" no es un número entero. Intente de nuevo.");
            }
        }
    }

    // Lo mismo con la fecha: se repite hasta que venga en formato AAAA-MM-DD y sea una fecha real.
    private static LocalDate leerFecha(String mensaje) {
        while (true) {
            String linea = leerTexto(mensaje);
            try {
                return LocalDate.parse(linea);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha inválida: \"" + linea
                        + "\". Use el formato AAAA-MM-DD (por ejemplo 2026-11-12).");
            }
        }
    }
}
