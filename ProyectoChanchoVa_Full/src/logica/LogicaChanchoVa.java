package logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import modelos.Carta;
import modelos.Jugador;

public class LogicaChanchoVa {

    private int cantidadJugadores;
    private int cantidadHumanos;
    private int cartasPorJugador = 4;
    private boolean rondaEnCurso = false;

    private List<Jugador> jugadores = new ArrayList<>();
    private List<Carta> mazo = new ArrayList<>();
    private int turnoActual = 0;

    public LogicaChanchoVa(int cantidadTotal, int cantidadHumanos) {
        this.cantidadJugadores = cantidadTotal;
        this.cantidadHumanos = cantidadHumanos;

        inicializarJugadores();
        generarMazo();
    }

    private void inicializarJugadores() {
        jugadores.clear();

        // Agregar jugadores humanos
        for (int i = 0; i < cantidadHumanos; i++) {
            jugadores.add(new Jugador("Jugador " + (i + 1), i + 1)); // usuarioId >0 = humano
        }

        // Agregar bots
        for (int i = cantidadHumanos; i < cantidadJugadores; i++) {
            jugadores.add(new Jugador("Bot " + (i + 1 - cantidadHumanos), 0)); // usuarioId = 0 = bot
        }
    }

    private void generarMazo() {
        mazo.clear();
        String[] palos = {"♠", "♥", "♦", "♣"};

        for (String palo : palos) {
            for (int valor = 1; valor <= 13; valor++) {
                mazo.add(new Carta(palo, valor));
            }
        }

        Collections.shuffle(mazo);
    }

    public void iniciarRonda() {
        rondaEnCurso = true;
        turnoActual = new Random().nextInt(cantidadJugadores);
        repartirCartas();
    }

    private void repartirCartas() {
        for (Jugador jugador : jugadores) {
            jugador.vaciarMano();
        }

        int index = 0;
        for (int i = 0; i < cartasPorJugador; i++) {
            for (Jugador jugador : jugadores) {
                jugador.agregarCarta(mazo.get(index++));
            }
        }
    }

    public void pasarCarta(int jugadorIndex, Carta carta) {
        if (!rondaEnCurso) return;

        int destino = (jugadorIndex + 1) % cantidadJugadores;
        jugadores.get(jugadorIndex).removerCarta(carta);
        jugadores.get(destino).agregarCarta(carta);
        avanzarTurno();
    }

    private void avanzarTurno() {
        turnoActual = (turnoActual + 1) % cantidadJugadores;
    }

    public int getTurnoActual() {
        return turnoActual;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

}
