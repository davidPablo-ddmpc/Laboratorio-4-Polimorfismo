// Las reglas del fútbol sala: se vale empatar.
// Ganar da 3 puntos de tabla, empatar 1 y perder 0.
public class FutbolSala extends Encuentro {
    private static final int PUNTOS_VICTORIA = 3;
    private static final int PUNTOS_EMPATE = 1;
    private static final int PUNTOS_DERROTA = 0;

    // Todo lo común lo hace Encuentro; aquí no hay nada extra que guardar.
    public FutbolSala(Equipo local, Equipo visitante) {
        super(local, visitante);
    }

    @Override
    public String getDeporte() {
        return "Fútbol sala";
    }

    @Override
    public boolean esMarcadorValido(int marcadorLocal, int marcadorVisitante) {
        // En fútbol sala vale cualquier marcador; los negativos ya los revisa Encuentro.
        return true;
    }

    @Override
    public int calcularPuntosTabla(int marcadorPropio, int marcadorRival) {
        if (marcadorPropio > marcadorRival) {
            return PUNTOS_VICTORIA;
        }
        if (marcadorPropio == marcadorRival) {
            return PUNTOS_EMPATE;
        }
        return PUNTOS_DERROTA;
    }
}
