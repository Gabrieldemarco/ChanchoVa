package logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import modelos.Carta;

// ----------------------------------------------
//   MOTOR DE JUEGO: CHANCHO VA (Versión Pro)
// ----------------------------------------------
public class LogicaChanchoVa {

    // ======== CONFIGURACIÓN GENERAL =========
    private int cantidadJugadores;
    private int cartasPorJugador = 4;
    private boolean rondaEnCurso = false;

    // ======== ESTADO DEL JUEGO =========
    private List<Jugador> jugadores = new ArrayList<>();
    private List<Carta> mazo = new ArrayList<>();
    private int turnoActual = 0;

    // ======== EVENTOS =========
    private OnGameEventListener listener;

    // ======== CONSTRUCTOR =========
    public LogicaChanchoVa(int cantidadJugadores) {
        this.cantidadJugadores = cantidadJugadores;
        inicializarJugadores();
        generarMazo();
    }

    // ----------------------------------------------
    //   INTERFAZ DE EVENTOS (para comunicar a UI)
    // ----------------------------------------------
    public interface OnGameEventListener {
        void onCartaPasada(int jugadorOrigen, int jugadorDestino, Carta carta);
        void onJugadorCompleto(int jugadorIndex);
        void onRondaIniciada(int turnoInicial);
        void onTurnoAvanzado(int nuevoTurno);
        void onRondaTerminada(int ganadorIndex);
    }

    public void setListener(OnGameEventListener listener) {
        this.listener = listener;
    }

    // ----------------------------------------------
    //   INICIALIZACIÓN
    // ----------------------------------------------
    private void inicializarJugadores() {
        for (int i = 0; i < cantidadJugadores; i++) {
            jugadores.add(new Jugador("Jugador " + (i + 1)));
        }
    }

    private void generarMazo() {
        mazo.clear();
        String[] palos = {"♠", "♥", "♦", "♣"};

        for (String palo : palos) {
            for (int valor = 1; valor <= 13; valor++) {
                mazo.add(new Carta(valor, palo));
            }
        }

        Collections.shuffle(mazo);
    }

    // ----------------------------------------------
    //   INICIAR RONDA
    // ----------------------------------------------
    public void iniciarRonda() {
        rondaEnCurso = true;
        turnoActual = new Random().nextInt(cantidadJugadores);

        repartirCartas();

        if (listener != null)
            listener.onRondaIniciada(turnoActual);
    }

    private void repartirCartas() {
        for (Jugador jugador : jugadores) {
            jugador.vaciarMano();
        }

        int indexMazo = 0;

        for (int i = 0; i < cartasPorJugador; i++) {
            for (Jugador jugador : jugadores) {
                jugador.agregarCarta(mazo.get(indexMazo++));
            }
        }
    }

    // ----------------------------------------------
    //   PASAR CARTA
    // ----------------------------------------------
    public void pasarCarta(int jugadorIndex, Carta carta) {

        if (!rondaEnCurso) return;

        int destino = (jugadorIndex + 1) % cantidadJugadores;

        jugadores.get(jugadorIndex).removerCarta(carta);
        jugadores.get(destino).agregarCarta(carta);

        if (listener != null)
            listener.onCartaPasada(jugadorIndex, destino, carta);

        checkGanador(destino);
        avanzarTurno();
    }

    // ----------------------------------------------
    //   TURNOS
    // ----------------------------------------------
    private void avanzarTurno() {
        turnoActual = (turnoActual + 1) % cantidadJugadores;

        if (listener != null)
            listener.onTurnoAvanzado(turnoActual);
    }

    public int getTurnoActual() {
        return turnoActual;
    }

    // ----------------------------------------------
    //   CHEQUEAR SI COMPLETÓ 4 IGUALES
    // ----------------------------------------------
    private void checkGanador(int jugadorIndex) {
        if (jugadores.get(jugadorIndex).tieneCuatroIguales()) {
            rondaEnCurso = false;

            if (listener != null)
                listener.onJugadorCompleto(jugadorIndex);

            if (listener != null)
                listener.onRondaTerminada(jugadorIndex);
        }
    }

    // ----------------------------------------------
    //   ACCESOS ÚTILES
    // ----------------------------------------------
    public List<Carta> getManoJugador(int jugadorIndex) {
        return jugadores.get(jugadorIndex).getMano();
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }
}
