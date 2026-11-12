import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

// La copa guarda los equipos y los eventos, y es la que revisa que cada operación sea válida
// (por ejemplo, que la sede no esté ocupada), porque es la única que ve todos los eventos juntos.
// Implementa Iterable para que el calendario se pueda recorrer con un for-each.
public class Copa implements Iterable<Programable> {
    private ArrayList<Equipo> equipos;
    // Los encuentros están en las dos listas (son los mismos objetos, no copias):
    // en eventos para el calendario y en encuentros para la tabla.
    // Así ni el calendario ni la tabla tienen que preguntar de qué tipo es cada cosa.
    private ArrayList<Programable> eventos;
    private ArrayList<Puntuable> encuentros;

    // La copa empieza vacía.
    public Copa() {
        equipos = new ArrayList<>();
        eventos = new ArrayList<>();
        encuentros = new ArrayList<>();
    }

    public void registrarEquipo(Equipo equipo) {
        if (equipo == null) {
            throw new IllegalArgumentException("El equipo no puede estar vacío.");
        }
        if (buscarEquipo(equipo.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un equipo con el código " + equipo.getCodigo() + ".");
        }
        equipos.add(equipo);
    }

    // Versión cómoda para el menú: arma el equipo y usa el método de arriba.
    public void registrarEquipo(String codigo, String nombre, String empresa) {
        registrarEquipo(new Equipo(codigo, nombre, empresa));
    }

    // Devuelve null si no lo encuentra. No importa si el código se escribe en minúsculas.
    public Equipo buscarEquipo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return null;
        }
        String buscado = codigo.trim().toUpperCase();
        for (Equipo equipo : equipos) {
            if (equipo.getCodigo().equals(buscado)) {
                return equipo;
            }
        }
        return null;
    }

    // Los getters devuelven copias para que nadie modifique las listas de la copa desde afuera.
    public ArrayList<Equipo> getEquipos() {
        return new ArrayList<>(equipos);
    }

    // Para eventos que solo van al calendario, como las exhibiciones.
    public void agregarEvento(Programable evento) {
        if (evento == null) {
            throw new IllegalArgumentException("El evento no puede estar vacío.");
        }
        eventos.add(evento);
    }

    public void agregarEncuentro(Encuentro encuentro) {
        if (encuentro == null) {
            throw new IllegalArgumentException("El encuentro no puede estar vacío.");
        }
        // Si el equipo no es el que está registrado, sus puntos no se verían en la tabla.
        for (Equipo participante : encuentro.getParticipantes()) {
            if (buscarEquipo(participante.getCodigo()) != participante) {
                throw new IllegalArgumentException("El equipo " + participante.getCodigo()
                        + " no está registrado en la copa.");
            }
        }
        eventos.add(encuentro);
        encuentros.add(encuentro);
    }

    public ArrayList<Programable> getEventos() {
        return new ArrayList<>(eventos);
    }

    public ArrayList<Puntuable> getEncuentros() {
        return new ArrayList<>(encuentros);
    }

    // El número de evento es el que ve el usuario en la lista (empieza en 1).
    // Se revisa todo antes de tocar el evento; si algo falla, el evento queda como estaba.
    public void programarEvento(int numeroEvento, LocalDate fecha, int hora, String sede) {
        Programable evento = obtenerEvento(numeroEvento);
        // Si la hora o la sede no sirven, el constructor de Horario ya lanza el error.
        Horario horario = new Horario(fecha, hora, sede);
        if (!evento.puedeProgramarse()) {
            throw new IllegalStateException("\"" + evento.getDescripcion()
                    + "\" ya tiene resultado; no se puede programar ni reprogramar.");
        }
        if (sedeOcupada(evento, horario)) {
            throw new IllegalStateException("La sede " + horario.getSede() + " ya tiene otro evento el "
                    + horario.getFecha() + " a las " + String.format("%02d:00", horario.getHora()) + ".");
        }
        Equipo ocupado = equipoOcupado(evento, horario);
        if (ocupado != null) {
            throw new IllegalStateException("El equipo " + ocupado.getNombre() + " ya tiene otro encuentro el "
                    + horario.getFecha() + " a las " + String.format("%02d:00", horario.getHora()) + ".");
        }
        evento.programar(horario);
    }

    // La copa solo busca el encuentro; las reglas las pone el encuentro (y su deporte).
    public void registrarResultado(int numeroEncuentro, int marcadorLocal, int marcadorVisitante) {
        obtenerEncuentro(numeroEncuentro).registrarResultado(marcadorLocal, marcadorVisitante);
    }

    // Se ordena una copia usando el compareTo de Equipo (puntos y luego nombre).
    public ArrayList<Equipo> getTablaPosiciones() {
        ArrayList<Equipo> tabla = new ArrayList<>(equipos);
        Collections.sort(tabla);
        return tabla;
    }

    @Override
    public Iterator<Programable> iterator() {
        // Solo los eventos con fecha, ya ordenados, y en una lista que no se puede modificar desde afuera.
        // Quien recorre el calendario no necesita saber cómo se guardan ni cómo se ordenan.
        ArrayList<Programable> calendario = new ArrayList<>();
        for (Programable evento : eventos) {
            if (evento.estaProgramado()) {
                calendario.add(evento);
            }
        }
        Collections.sort(calendario);
        return Collections.unmodifiableList(calendario).iterator();
    }

    private Programable obtenerEvento(int numero) {
        if (numero < 1 || numero > eventos.size()) {
            throw new IllegalArgumentException("No existe el evento número " + numero + ".");
        }
        return eventos.get(numero - 1);
    }

    private Puntuable obtenerEncuentro(int numero) {
        if (numero < 1 || numero > encuentros.size()) {
            throw new IllegalArgumentException("No existe el encuentro número " + numero + ".");
        }
        return encuentros.get(numero - 1);
    }

    // Revisa si algún otro evento ya usa esa sede a la misma fecha y hora.
    // Se salta el propio evento para que reprogramarlo a su mismo horario no choque consigo mismo.
    private boolean sedeOcupada(Programable evento, Horario horario) {
        for (Programable otro : eventos) {
            if (otro != evento && otro.estaProgramado() && otro.getHorario().mismaSedeYMomento(horario)) {
                return true;
            }
        }
        return false;
    }

    // Devuelve el primer equipo que ya juega otro encuentro a esa hora, o null si no hay choque.
    // Como las exhibiciones no tienen participantes, nunca chocan aquí y no hace falta un if para ellas.
    private Equipo equipoOcupado(Programable evento, Horario horario) {
        ArrayList<Equipo> participantes = evento.getParticipantes();
        for (Programable otro : eventos) {
            if (otro == evento || !otro.estaProgramado() || !otro.getHorario().mismoMomento(horario)) {
                continue;
            }
            for (Equipo equipo : otro.getParticipantes()) {
                if (participantes.contains(equipo)) {
                    return equipo;
                }
            }
        }
        return null;
    }
}
