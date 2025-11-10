package ui;

import modelos.*;
import logica.LogicaChanchoVa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

public class PantallaJuego extends JFrame {

    private Usuario usuarioPrincipal;
    private LogicaChanchoVa logica;

    // UI
    private JTextArea taLog;
    private JLabel lblRonda;
    private DefaultListModel<String> modeloMano;
    private JList<String> listaMano;
    private JButton btnPasarCarta;
    private JButton btnListo;
    private JButton btnSeña;

    // estado local de la partida (para el flujo simultáneo)
    private List<Jugador> jugadores;
    private Map<Jugador, Carta> eleccionesActuales = new HashMap<>();
    private int indiceJugadorSeleccion = 0; // índice del jugador humano que está seleccionando ahora

    public PantallaJuego(Usuario usuario) {
        this.usuarioPrincipal = usuario;
        setTitle("Chancho Va - " + usuario.getNombreUsuario());
        setSize(900,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponentes();
        iniciarPartida();
    }

    private void initComponentes() {
        JPanel main = new JPanel(new BorderLayout());
        taLog = new JTextArea();
        taLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        taLog.setEditable(false);
        main.add(new JScrollPane(taLog), BorderLayout.CENTER);

        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));

        lblRonda = new JLabel("Ronda: 0");
        lblRonda.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelDerecho.add(lblRonda);

        panelDerecho.add(new JLabel("Mano (jugador actual de selección):"));
        modeloMano = new DefaultListModel<>();
        listaMano = new JList<>(modeloMano);
        listaMano.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelDerecho.add(new JScrollPane(listaMano));

        btnPasarCarta = new JButton("Pasar carta seleccionada");
        btnPasarCarta.addActionListener(this::onPasarCarta);
        panelDerecho.add(btnPasarCarta);

        btnListo = new JButton("Marcar listo (siguiente jugador)");
        btnListo.addActionListener(this::onListo);
        panelDerecho.add(btnListo);

        btnSeña = new JButton("CHANCHO (hacer seña)");
        btnSeña.addActionListener(this::onSeña);
        panelDerecho.add(btnSeña);

        main.add(panelDerecho, BorderLayout.EAST);

        add(main);
    }

    private void iniciarPartida() {
        int cant = pedirNumeroJugadores();
        logica = new LogicaChanchoVa(1);

        jugadores = new ArrayList<>();
        for (int i = 0; i < cant; i++) {
            String nombre = JOptionPane.showInputDialog(this, "Nombre del jugador " + (i+1) + ":", "Jugador " + (i+1));
            if (nombre == null || nombre.isBlank()) nombre = "Jugador" + (i+1);
            int usuarioId = (i == 0) ? usuarioPrincipal.getIdUsuario() : 0;
            Jugador j = new Jugador(usuarioId, nombre, i);
            jugadores.add(j);
            logica.agregarJugador(j);
        }

        logica.iniciarRonda(); // reparte 4 cartas
        indiceJugadorSeleccion = 0;
        eleccionesActuales.clear();
        actualizarTodo();
        log("Partida iniciada. Ronda " + logica.getNumeroRonda());
    }

    private int pedirNumeroJugadores() {
        while (true) {
            String s = JOptionPane.showInputDialog(this, "¿Cuántos jugadores? (2 a 6)", "4");
            if (s == null) return 4;
            try {
                int n = Integer.parseInt(s.trim());
                if (n >= 2 && n <= 6) return n;
            } catch (Exception ignored) {}
        }
    }

    private void actualizarTodo() {
        lblRonda.setText("Ronda: " + logica.getNumeroRonda());
        Jugador sel = jugadores.get(indiceJugadorSeleccion);
        modeloMano.clear();
        for (Carta c : sel.getMano()) modeloMano.addElement(c.toString());
        repaintEstadoJugadores();
    }

    private void repaintEstadoJugadores() {
        StringBuilder sb = new StringBuilder();
        sb.append("Jugadores:\n");
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador j = jugadores.get(i);
            sb.append(String.format("%d) %s - cartas:%d - prendas:%d%s\n",
                    i+1, j.getNombre(), j.getMano().size(), j.getPrendas(), j.estaEliminado() ? " [ELIM]" : ""));
        }
        taLog.setText(sb.toString());
    }

    private void log(String s) {
        taLog.append(s + "\n");
    }

    private void seleccionarCartaBot(Jugador bot) {
        if (bot.getMano().isEmpty()) return;
        Random rnd = new Random();
        int idx = rnd.nextInt(bot.getMano().size());
        Carta c = bot.pasarCarta(idx);
        eleccionesActuales.put(bot, c);
        log(bot.getNombre() + " eligió carta para pasar: " + c);
    }

    private void onPasarCarta(ActionEvent e) {
        Jugador sel = jugadores.get(indiceJugadorSeleccion);
        if (sel.getUsuarioId() == 0) {
            JOptionPane.showMessageDialog(this, "Este jugador es bot; pulsa 'Marcar listo' para avanzar.");
            return;
        }
        int idx = listaMano.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná una carta para pasar.");
            return;
        }
        Carta c = sel.pasarCarta(idx);
        eleccionesActuales.put(sel, c);
        log(sel.getNombre() + " eligió carta para pasar: " + c);
    }

    private void onListo(ActionEvent e) {
        int siguiente = (indiceJugadorSeleccion + 1) % jugadores.size();
        for (int i = 0; i < jugadores.size(); i++) {
            int idx = (indiceJugadorSeleccion + 1 + i) % jugadores.size();
            if (!jugadores.get(idx).estaEliminado()) { siguiente = idx; break; }
        }
        indiceJugadorSeleccion = siguiente;

        Jugador actual = jugadores.get(indiceJugadorSeleccion);
        if (actual.getUsuarioId() == 0) {
            seleccionarCartaBot(actual);
        }

        if (todasLasEleccionesCompletas()) {
            ejecutarPasoYEvaluar();
        } else {
            actualizarTodo();
        }
    }

    private boolean todasLasEleccionesCompletas() {
        int activos = 0;
        for (Jugador j : jugadores) if (!j.estaEliminado()) activos++;
        return eleccionesActuales.size() >= activos;
    }

    private void ejecutarPasoYEvaluar() {
        log("Ejecutando pase de cartas...");
        Map<Jugador, Carta> jugadasHumanas = new HashMap<>(eleccionesActuales);
        boolean sigue = logica.ejecutarPasoDeRonda(jugadasHumanas);
        eleccionesActuales.clear();

        actualizarTodo();

        if (!sigue) {
            Jugador per = logica.determinarPerdedor();
            if (per != null) {
                log("Seña detectada. Perdedor: " + per.getNombre());
                logica.aplicarPrenda(per);
                log(per.getNombre() + " recibe prenda. Total prendas: " + per.getPrendas());
            } else {
                log("Seña detectada pero no se encontró perdedor.");
            }

            logica.resetearSeñas();
            log("Ronda finalizada. Repartiendo nueva ronda...");
            logica.iniciarRonda();
            actualizarTodo();
        } else {
            log("No hubo seña. Continuamos con la siguiente selección.");
        }
    }

    private void onSeña(ActionEvent e) {
        Jugador sel = jugadores.get(indiceJugadorSeleccion);
        logica.hacerSeña(sel);
        log(sel.getNombre() + " hizo seña.");
        if (logica.haySeña()) {
            ejecutarPasoYEvaluar();
        }
    }
}
