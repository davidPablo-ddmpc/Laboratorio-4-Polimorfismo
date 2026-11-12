import java.util.ArrayList;

// Lo que tienen todos los encuentros sin importar el deporte: dos equipos, un marcador,
// un estado y un horario. Es abstracta porque no existe un "encuentro" a secas;
// siempre es de algún deporte, y cada deporte completa lo que aquí queda pendiente.
public abstract class Encuentro implements Programable, Puntuable {
    // Posición 0 = local, posición 1 = visitante (igual en el marcador).
    private Equipo[] equipos;
    private int[] marcador;
    private EstadoEvento estado;
    // Queda en null mientras el encuentro no se programe.
    private Horario horario;

    // Es protected porque solo lo llaman las subclases con super(local, visitante).
    // Todo encuentro arranca sin programar y con el marcador 0-0.
    protected Encuentro(Equipo local, Equipo visitante) {
        if (local == null || visitante == null) {
            throw new IllegalArgumentException("Un encuentro necesita dos equipos.");
        }
        if (local.equals(visitante)) {
            throw new IllegalArgumentException("Un equipo no puede jugar contra sí mismo ("
                    + local.getNombre() + ").");
        }
        this.equipos = new Equipo[] {local, visitante};
        this.marcador = new int[] {0, 0};
        this.estado = EstadoEvento.SIN_PROGRAMAR;
        this.horario = null;
    }

    public Equipo getLocal() {
        return equipos[0];
    }

    public Equipo getVisitante() {
        return equipos[1];
    }

    public int getMarcadorLocal() {
        return marcador[0];
    }

    public int getMarcadorVisitante() {
        return marcador[1];
    }

    public EstadoEvento getEstado() {
        return estado;
    }

    // Cada deporte dice cómo se llama.
    public abstract String getDeporte();

    // Mientras no tenga resultado se puede programar y reprogramar las veces que haga falta.
    @Override
    public boolean puedeProgramarse() {
        return estado != EstadoEvento.FINALIZADO;
    }

    @Override
    public void programar(Horario horario) {
        if (!puedeProgramarse()) {
            throw new IllegalStateException("El encuentro ya finalizó; no se puede reprogramar.");
        }
        if (horario == null) {
            throw new IllegalArgumentException("El horario no puede estar vacío.");
        }
        this.horario = horario;
        this.estado = EstadoEvento.PROGRAMADO;
    }

    @Override
    public boolean estaProgramado() {
        return horario != null;
    }

    @Override
    public Horario getHorario() {
        return horario;
    }

    // Devolvemos una lista nueva para que nadie pueda cambiar los equipos desde afuera.
    @Override
    public ArrayList<Equipo> getParticipantes() {
        ArrayList<Equipo> participantes = new ArrayList<>();
        participantes.add(equipos[0]);
        participantes.add(equipos[1]);
        return participantes;
    }

    // Este mismo método cumple con Programable y con Puntuable a la vez.
    // Queda algo como "Fútbol sala: Faro vs Sol".
    @Override
    public String getDescripcion() {
        return getDeporte() + ": " + equipos[0].getNombre() + " vs " + equipos[1].getNombre();
    }

    // Los pasos son los mismos en todos los deportes, por eso el método es final:
    // ningún deporte puede cambiarlos, solo responder si el marcador vale y cuántos puntos da.
    // Primero se revisa todo y hasta el final se cambia algo,
    // así un resultado inválido no deja el encuentro ni la tabla a medias.
    @Override
    public final void registrarResultado(int marcadorLocal, int marcadorVisitante) {
        if (estado == EstadoEvento.SIN_PROGRAMAR) {
            throw new IllegalStateException("No se puede registrar el resultado: el encuentro \""
                    + getDescripcion() + "\" aún no está programado.");
        }
        if (estado == EstadoEvento.FINALIZADO) {
            throw new IllegalStateException("El encuentro \"" + getDescripcion()
                    + "\" ya finalizó y tiene resultado.");
        }
        if (marcadorLocal < 0 || marcadorVisitante < 0) {
            throw new IllegalArgumentException("El marcador no puede tener valores negativos.");
        }
        // Aquí se le pregunta al deporte; no hace falta saber cuál es.
        if (!esMarcadorValido(marcadorLocal, marcadorVisitante)) {
            throw new IllegalArgumentException("El marcador " + marcadorLocal + "-" + marcadorVisitante
                    + " no es válido en " + getDeporte() + ".");
        }
        marcador[0] = marcadorLocal;
        marcador[1] = marcadorVisitante;
        estado = EstadoEvento.FINALIZADO;
        // Cada equipo recibe sus puntos viendo el partido "desde su lado".
        equipos[0].sumarPuntos(calcularPuntosTabla(marcadorLocal, marcadorVisitante));
        equipos[1].sumarPuntos(calcularPuntosTabla(marcadorVisitante, marcadorLocal));
    }

    // Estas dos reglas cambian según el deporte, así que se quedan sin resolver aquí.
    @Override
    public abstract boolean esMarcadorValido(int marcadorLocal, int marcadorVisitante);

    @Override
    public abstract int calcularPuntosTabla(int marcadorPropio, int marcadorRival);

    @Override
    public boolean estaFinalizado() {
        return estado == EstadoEvento.FINALIZADO;
    }

    @Override
    public String toString() {
        String texto = getDescripcion() + " | " + estado;
        if (horario != null) {
            texto += " | " + horario;
        }
        if (estaFinalizado()) {
            texto += " | Resultado: " + marcador[0] + "-" + marcador[1];
        }
        return texto;
    }
}
