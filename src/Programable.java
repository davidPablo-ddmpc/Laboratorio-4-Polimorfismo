import java.util.ArrayList;

// Todo lo que se puede programar y mostrar en el calendario.
// La cumplen los encuentros y las exhibiciones, que no se parecen en nada más,
// por eso es una interfaz y no una clase de la que hereden.
public interface Programable extends Comparable<Programable> {
    void programar(Horario horario);

    // Un encuentro que ya terminó no se puede mover; una exhibición siempre se puede.
    boolean puedeProgramarse();

    boolean estaProgramado();

    Horario getHorario();

    // Los equipos que juegan, para revisar choques de horario. Una exhibición devuelve la lista vacía.
    ArrayList<Equipo> getParticipantes();

    String getDescripcion();

    // El criterio de orden está escrito en Horario; aquí solo se lo pedimos.
    // Así sirve igual para encuentros y exhibiciones sin repetir nada.
    @Override
    default int compareTo(Programable otro) {
        return getHorario().compareTo(otro.getHorario());
    }
}
