// Todo lo que recibe un resultado y reparte puntos de tabla.
// Solo la cumplen los encuentros: una exhibición no tiene marcador ni equipos,
// así que no tendría sentido obligarla a implementar estos métodos.
public interface Puntuable {
    void registrarResultado(int marcadorLocal, int marcadorVisitante);

    // Cada deporte decide qué marcador es válido (por ejemplo, en baloncesto no hay empates).
    boolean esMarcadorValido(int marcadorLocal, int marcadorVisitante);

    // Cuántos puntos de tabla gana un equipo según su marcador y el del rival.
    int calcularPuntosTabla(int marcadorPropio, int marcadorRival);

    boolean estaFinalizado();

    String getDescripcion();
}
