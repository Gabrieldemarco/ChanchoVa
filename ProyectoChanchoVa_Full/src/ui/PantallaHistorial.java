package ui;

import javax.swing.*;
import java.awt.*;

public class PantallaHistorial extends JFrame {
    private static final long serialVersionUID = 1L;

	public PantallaHistorial() {
        setTitle("Historial de partidas");
        setSize(500,400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JTextArea ta = new JTextArea("Historial (pendiente de implementar carga desde BD)");
        ta.setEditable(false);
        add(new JScrollPane(ta), BorderLayout.CENTER);
    }
}
