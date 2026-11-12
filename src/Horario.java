import java.time.LocalDate;

// Cuándo y dónde pasa un evento: fecha, hora de inicio y sede.
// Aquí vive el criterio de orden del calendario, escrito una sola vez para encuentros y exhibiciones.
public class Horario implements Comparable<Horario> {
    private LocalDate fecha;
    private int hora;
    private String sede;

    // Si algún dato no sirve, el horario ni siquiera se crea.
    public Horario(LocalDate fecha, int hora, String sede) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede estar vacía.");
        }
        if (hora < 0 || hora > 23) {
            throw new IllegalArgumentException("La hora debe estar entre 0 y 23 (se ingresó " + hora + ").");
        }
        if (sede == null || sede.isBlank()) {
            throw new IllegalArgumentException("La sede no puede estar vacía.");
        }
        this.fecha = fecha;
        this.hora = hora;
        this.sede = sede.trim();
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getHora() {
        return hora;
    }

    public String getSede() {
        return sede;
    }

    // Sirve para saber si un equipo ya está jugando a esa misma hora (la sede no importa aquí).
    public boolean mismoMomento(Horario otro) {
        return otro != null && fecha.equals(otro.fecha) && hora == otro.hora;
    }

    // Sirve para saber si la sede ya está ocupada a esa fecha y hora.
    // No importan mayúsculas: "cancha central" y "Cancha Central" son el mismo lugar.
    public boolean mismaSedeYMomento(Horario otro) {
        return mismoMomento(otro) && sede.equalsIgnoreCase(otro.sede);
    }

    // El orden del calendario: primero la fecha, luego la hora y, si empiezan igual, el nombre de la sede.
    @Override
    public int compareTo(Horario otro) {
        int porFecha = fecha.compareTo(otro.fecha);
        if (porFecha != 0) {
            return porFecha;
        }
        int porHora = Integer.compare(hora, otro.hora);
        if (porHora != 0) {
            return porHora;
        }
        return sede.compareToIgnoreCase(otro.sede);
    }

    // Queda algo como "2026-11-12 09:00 - Gimnasio Norte".
    @Override
    public String toString() {
        return String.format("%s %02d:00 - %s", fecha, hora, sede);
    }
}
