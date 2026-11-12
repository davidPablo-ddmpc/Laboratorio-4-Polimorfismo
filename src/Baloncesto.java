// Las reglas del baloncesto: no hay empates.
// Ganar da 2 puntos de tabla y perder da 1 (en baloncesto perder también suma).
public class Baloncesto extends Encuentro {
    private static final int PUNTOS_VICTORIA = 2;
    private static final int PUNTOS_DERROTA = 1;

    public Baloncesto(Equipo local, Equipo visitante) {
        super(local, visitante);
    }

    @Override
    public String getDeporte() {
        return "Baloncesto";
    }

    // Si los dos marcadores son iguales, el resultado se rechaza.
    @Override
    public boolean esMarcadorValido(int marcadorLocal, int marcadorVisitante) {
        return marcadorLocal != marcadorVisitante;
    }

    // No hace falta revisar el empate aquí: nunca llega uno, porque esMarcadorValido lo frena antes.
    @Override
    public int calcularPuntosTabla(int marcadorPropio, int marcadorRival) {
        if (marcadorPropio > marcadorRival) {
            return PUNTOS_VICTORIA;
        }
        return PUNTOS_DERROTA;
    }
}
