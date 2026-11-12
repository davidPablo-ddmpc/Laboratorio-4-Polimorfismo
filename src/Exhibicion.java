import java.util.ArrayList;

// Una clínica o demostración. No es un encuentro: no tiene equipos, marcador ni resultado.
// Solo se programa y aparece en el calendario, por eso nada más cumple Programable.
public class Exhibicion implements Programable {
    private String titulo;
    // Solo usa SIN_PROGRAMAR y PROGRAMADO.
    private EstadoEvento estado;
    private Horario horario;

    public Exhibicion(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título de la exhibición no puede estar vacío.");
        }
        this.titulo = titulo.trim();
        this.estado = EstadoEvento.SIN_PROGRAMAR;
        this.horario = null;
    }

    public String getTitulo() {
        return titulo;
    }

    public EstadoEvento getEstado() {
        return estado;
    }

    // Como nunca recibe resultado, siempre se puede volver a programar.
    @Override
    public boolean puedeProgramarse() {
        return true;
    }

    @Override
    public void programar(Horario horario) {
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

    // No tiene equipos, así que nunca choca con el horario de un equipo; solo con la sede.
    @Override
    public ArrayList<Equipo> getParticipantes() {
        return new ArrayList<>();
    }

    @Override
    public String getDescripcion() {
        return "Exhibición: " + titulo;
    }

    @Override
    public String toString() {
        String texto = getDescripcion() + " | " + estado;
        if (horario != null) {
            texto += " | " + horario;
        }
        return texto;
    }
}
