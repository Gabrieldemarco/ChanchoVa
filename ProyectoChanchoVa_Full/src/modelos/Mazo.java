package modelos;

import java.util.*;

public class Mazo {
    private final Deque<Carta> cartas = new ArrayDeque<>();

    public Mazo() {
        inicializarYBarajar();
    }

    public final void inicializarYBarajar() {
        cartas.clear();
        String[] palos = {"CORAZON","TREBOL","DIAMANTE","PICA"};
        for (String palo : palos) {
            for (int numero = 1; numero <= 13; numero++) {
                cartas.add(new Carta(palo, numero));
            }
        }
        mezclar();
    }

    public void mezclar() {
        List<Carta> lista = new ArrayList<>(cartas);
        Collections.shuffle(lista);
        cartas.clear();
        cartas.addAll(lista);
    }

    public Carta robar() {
        return cartas.pollFirst(); // null si vacío
    }

    public boolean estaVacio() { return cartas.isEmpty(); }
    public int restantes() { return cartas.size(); }

    // útil para debug
    @Override
    public String toString() {
        return "Mazo[" + cartas.size() + "]";
    }
}

