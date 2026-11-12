// Un equipo de alguna empresa que juega en la copa.
// Implementa Comparable para que la tabla de posiciones se pueda ordenar sola con Collections.sort.
public class Equipo implements Comparable<Equipo> {
    private String codigo;
    private String nombre;
    private String empresa;
    // Ojo: estos son los puntos de la tabla, no los goles ni los puntos anotados en un partido.
    private int puntosTabla;

    public Equipo(String codigo, String nombre, String empresa) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del equipo no puede estar vacío.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del equipo no puede estar vacío.");
        }
        if (empresa == null || empresa.isBlank()) {
            throw new IllegalArgumentException("La empresa del equipo no puede estar vacía.");
        }
        // El código se guarda en mayúsculas para que "far" y "FAR" cuenten como el mismo equipo.
        this.codigo = codigo.trim().toUpperCase();
        this.nombre = nombre.trim();
        this.empresa = empresa.trim();
        this.puntosTabla = 0;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmpresa() {
        return empresa;
    }

    public int getPuntosTabla() {
        return puntosTabla;
    }

    // Solo lo usa un encuentro cuando se registra su resultado.
    public void sumarPuntos(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("Los puntos de tabla a sumar no pueden ser negativos.");
        }
        puntosTabla += puntos;
    }

    // Primero va el que tiene más puntos (por eso se comparan "al revés").
    // Si empatan, se ordenan por nombre: con 4 puntos cada uno, Faro va antes que Luna.
    @Override
    public int compareTo(Equipo otro) {
        int porPuntos = Integer.compare(otro.puntosTabla, this.puntosTabla);
        if (porPuntos != 0) {
            return porPuntos;
        }
        return this.nombre.compareToIgnoreCase(otro.nombre);
    }

    // Dos equipos son el mismo si tienen el mismo código, sin importar el resto.
    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (otro == null || getClass() != otro.getClass()) {
            return false;
        }
        return codigo.equals(((Equipo) otro).codigo);
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (" + empresa + ") - " + puntosTabla + " pts";
    }
}
