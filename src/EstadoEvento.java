// Los estados por los que puede pasar un evento.
// Un encuentro usa los tres; una exhibición nunca llega a FINALIZADO porque no tiene resultado.
public enum EstadoEvento {
    SIN_PROGRAMAR("Sin programar"),
    PROGRAMADO("Programado"),
    FINALIZADO("Finalizado");

    // El texto bonito que se muestra en pantalla, para no imprimir "SIN_PROGRAMAR".
    private final String texto;

    EstadoEvento(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return texto;
    }
}
