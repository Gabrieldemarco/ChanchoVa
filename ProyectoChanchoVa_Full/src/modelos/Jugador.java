package modelos;

import java.util.*;

public class Jugador {
    private final int usuarioId; // 0 para bots temporales
    private final String nombre;
    private final List<Carta> mano = new ArrayList<>();
    private int prendas = 0;
    private boolean eliminado = false;
    private final int indiceTurno;

    public Jugador(int usuarioId, String nombre, int indiceTurno) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.indiceTurno = indiceTurno;
    }

    public int getUsuarioId() { return usuarioId; }
    public String getNombre() { return nombre; }
    public List<Carta> getMano() { return mano; }
    public int getPrendas() { return prendas; }
    public void sumarPrenda(int c) { prendas += c; }
    public boolean estaEliminado() { return eliminado; }
    public void setEliminado(boolean e) { eliminado = e; }
    public int getIndiceTurno() { return indiceTurno; }
    public int getIdJugador() { return usuarioId; }

    public void recibirCarta(Carta c) {
        if (c != null) mano.add(c);
    }

    public Carta jugarCarta(int indice) {
        if (indice < 0 || indice >= mano.size()) throw new IllegalArgumentException("Índice inválido");
        return mano.remove(indice);
    }

    // Claridad semántica: pasar carta (equivalente a jugarCarta)
    public Carta pasarCarta(int indice) {
        return jugarCarta(indice);
    }

    public boolean tieneCuatroIgualesEnMano() {
        Map<Integer, Integer> conteo = new HashMap<>();
        for (Carta c : mano) {
            conteo.put(c.getNumero(), conteo.getOrDefault(c.getNumero(), 0) + 1);
        }
        for (int v : conteo.values()) if (v >= 4) return true;
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s (prendas=%d, cartas=%d)", nombre, prendas, mano.size());
    }
}
