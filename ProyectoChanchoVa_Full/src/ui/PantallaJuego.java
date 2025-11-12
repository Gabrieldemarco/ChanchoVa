package ui;

import modelos.Carta;
import modelos.Jugador;
import modelos.Usuario;
import logica.LogicaChanchoVa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PantallaJuego extends JFrame {

    private static final long serialVersionUID = 1L;

    private LogicaChanchoVa logica;

    private JTextArea taLog;
    private JLabel lblTurno;
    private DefaultListModel<String> modeloMano;
    private JList<String> listaMano;
    private JButton btnPasarCarta;
    private JButton btnListo;
    private JButton btnChancho;

    private List<Jugador> jugadores = new ArrayList<>();
    private Map<Jugador, Carta> eleccionesActuales = new HashMap<>();
    private int indiceJugadorSeleccion = 0;

    public PantallaJuego(Usuario usuario) {
        setTitle("Chancho Va - " + usuario.getNombreUsuario());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initComponentes();
        iniciarPartida();
    }

    private void initComponentes() {
        JPanel main = new JPanel(new BorderLayout());

        taLog = new JTextArea();
        taLog.setFont(new Font("Monospaced", Font.PLAIN, 24));
        taLog.setEditable(false);
        main.add(new JScrollPane(taLog), BorderLayout.CENTER);

        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));

        lblTurno = new JLabel("Turno: 0");
        lblTurno.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelDerecho.add(lblTurno);

        panelDerecho.add(new JLabel("Mano (jugador actual de selección):"));
        modeloMano = new DefaultListModel<>();
        listaMano = new JList<>(modeloMano);
        listaMano.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaMano.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setFont(new Font("Monospaced", Font.BOLD, 24)); // tamaño grande
                label.setOpaque(true);
                label.setBackground(isSelected ? Color.YELLOW : Color.WHITE);
                label.setForeground(Color.BLACK);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            }
        });
        panelDerecho.add(new JScrollPane(listaMano));

        btnPasarCarta = new JButton("Pasar carta seleccionada");
        btnPasarCarta.addActionListener(this::onPasarCarta);
        panelDerecho.add(btnPasarCarta);

        btnListo = new JButton("Marcar listo (siguiente jugador)");
        btnListo.addActionListener(this::onListo);
        panelDerecho.add(btnListo);

        btnChancho = new JButton("CHANCHO!");
        btnChancho.addActionListener(this::onChancho);
        panelDerecho.add(btnChancho);

        main.add(panelDerecho, BorderLayout.EAST);
        add(main);
    }

    private void iniciarPartida() {
        int cantTotal = pedirNumeroJugadores();
        int cantHumanos = pedirNumeroHumanos(cantTotal);

        logica = new LogicaChanchoVa(cantTotal, cantHumanos);
        logica.iniciarRonda(); // reparte cartas al inicio

        jugadores = new ArrayList<>(logica.getJugadores());
        indiceJugadorSeleccion = 0;
        eleccionesActuales.clear();

        actualizarTodo();
        log("Partida iniciada. Turno inicial: " + logica.getTurnoActual());
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
    

    private int pedirNumeroHumanos(int max) {
        while (true) {
            String s = JOptionPane.showInputDialog(this, "¿Cuántos jugadores humanos? (1 a " + max + ")", "1");
            if (s == null) return 1;
            try {
                int n = Integer.parseInt(s.trim());
                if (n >= 1 && n <= max) return n;
            } catch (Exception ignored) {}
        }
    }

    private void actualizarTodo() {
        lblTurno.setText("Turno actual: " + logica.getTurnoActual());
        Jugador sel = jugadores.get(indiceJugadorSeleccion);

        modeloMano.clear();
        for (Carta c : sel.getMano()) modeloMano.addElement(c.toString());

        btnChancho.setEnabled(sel.tieneCuatroIguales()); // solo habilita si tiene 4 iguales
        repaintEstadoJugadores();
    }

    private void repaintEstadoJugadores() {
        StringBuilder sb = new StringBuilder();
        sb.append("Jugadores:\n");
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador j = jugadores.get(i);
            sb.append(String.format("%d) %s - cartas:%d%s\n",
                    i + 1, j.getNombre(), j.getMano().size(),
                    j.getUsuarioId() == 0 ? " [BOT]" : ""));
        }
        taLog.setText(sb.toString());
    }

    private void log(String s) { taLog.append(s + "\n"); }

    private void seleccionarCartaBot(Jugador bot) {
        if (bot.getMano().isEmpty()) return;
        Random rnd = new Random();
        int idx = rnd.nextInt(bot.getMano().size());
        Carta c = bot.getMano().get(idx);
        eleccionesActuales.put(bot, c);
        log(bot.getNombre() + " eligió carta: " + c);
    }

    private void onPasarCarta(ActionEvent e) {
        Jugador sel = jugadores.get(indiceJugadorSeleccion);
        if (sel.getUsuarioId() == 0) {
            JOptionPane.showMessageDialog(this, "Este jugador es bot; pulsa 'Marcar listo'.");
            return;
        }
        int idx = listaMano.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná una carta para pasar.");
            return;
        }
        Carta c = sel.getMano().get(idx);
        eleccionesActuales.put(sel, c);
        log(sel.getNombre() + " eligió carta: " + c);
    }

    private void onListo(ActionEvent e) {
        Jugador actual = jugadores.get(indiceJugadorSeleccion);
        if (actual.getUsuarioId() == 0) seleccionarCartaBot(actual);

        if (todasLasEleccionesCompletas()) ejecutarPasoYEvaluar();
        else avanzarSeleccion();
    }

    private boolean todasLasEleccionesCompletas() {
        return eleccionesActuales.size() >= jugadores.size();
    }

    private void ejecutarPasoYEvaluar() {
        log("Ejecutando pase de cartas...");
        for (Map.Entry<Jugador, Carta> entry : eleccionesActuales.entrySet()) {
            Jugador j = entry.getKey();
            Carta c = entry.getValue();
            int idx = jugadores.indexOf(j);
            logica.pasarCarta(idx, c);
        }
        eleccionesActuales.clear();
        actualizarTodo();
        avanzarSeleccion();
    }

    private void avanzarSeleccion() {
        indiceJugadorSeleccion = (indiceJugadorSeleccion + 1) % jugadores.size();
        actualizarTodo();
    }

    private void onChancho(ActionEvent e) {
        Jugador sel = jugadores.get(indiceJugadorSeleccion);
        if (sel.tieneCuatroIguales()) {
            log(sel.getNombre() + " gritó CHANCHO y gana la ronda!");
            JOptionPane.showMessageDialog(this, sel.getNombre() + " gritó CHANCHO y gana la ronda!");
            // Reiniciar partida
            iniciarPartida();
        } else {
            log(sel.getNombre() + " presionó CHANCHO incorrectamente!");
            JOptionPane.showMessageDialog(this, "¡Incorrecto! No tenés 4 cartas iguales.");
        }
    }
}