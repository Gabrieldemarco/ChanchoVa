package modelos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jugador {

    private String nombre;
    private int usuarioId;       // 0 = bot, >0 = jugador humano
    private int prendas = 0;     // número de prendas acumuladas
    private boolean eliminado = false;
    private List<Carta> mano = new ArrayList<>();

    private Random rnd = new Random(); // para bots

    // --- Constructor ---
    public Jugador(String nombre, int usuarioId) {
        this.nombre = nombre;
        this.usuarioId = usuarioId;
    }

    // --- Mano de cartas ---
    public void agregarCarta(Carta c) {
        mano.add(c);
    }

    public void removerCarta(Carta c) {
        mano.remove(c);
    }

    public void vaciarMano() {
        mano.clear();
    }

    public List<Carta> getMano() {
        return mano;
    }

    // --- Pasar carta ---
    public Carta pasarCarta(int indice) {
        if (indice < 0 || indice >= mano.size()) return null;
        return mano.remove(indice);
    }

    /**
     * Para bots: elige una carta aleatoria de la mano y la pasa.
     * @return Carta elegida y removida de la mano
     */
    public Carta jugarCartaBot() {
        if (mano.isEmpty()) return null;
        int idx = rnd.nextInt(mano.size());
        return pasarCarta(idx);
    }

    // --- Reglas de juego ---
    public boolean tieneCuatroIguales() {
        if (mano.size() != 4) return false;
        int valor = mano.get(0).getValor();
        for (Carta c : mano) {
            if (c.getValor() != valor) return false;
        }
        return true;
    }

    // --- Datos del jugador ---
    public String getNombre() {
        return nombre;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public int getPrendas() {
        return prendas;
    }

    public void sumarPrenda() {
        prendas++;
    }

    public void restarPrenda() {
        if (prendas > 0) prendas--;
    }

    public boolean estaEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    @Override
    public String toString() {
        return nombre + (usuarioId == 0 ? " [BOT]" : "");
    }
}
