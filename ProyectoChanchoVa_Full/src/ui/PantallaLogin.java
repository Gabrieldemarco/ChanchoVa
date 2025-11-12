package ui;

import modelos.*;
import javax.swing.*;


public class PantallaLogin extends JFrame {
    private static final long serialVersionUID = 1L;
	private JTextField tfUsuario;
    private JPasswordField pfPass;

    public PantallaLogin() {
        setTitle("Chancho Va - Login");
        setSize(380,220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel p = new JPanel(null);

        JLabel l1 = new JLabel("Usuario:");
        l1.setBounds(20, 20, 80, 25); p.add(l1);
        tfUsuario = new JTextField(); tfUsuario.setBounds(110, 20, 220, 25); p.add(tfUsuario);

        JLabel l2 = new JLabel("Contraseña:");
        l2.setBounds(20, 60, 80, 25); p.add(l2);
        pfPass = new JPasswordField(); pfPass.setBounds(110, 60, 220, 25); p.add(pfPass);

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setBounds(40, 110, 120, 30);
        btnEntrar.addActionListener(e -> onEntrar());
        p.add(btnEntrar);

        JButton btnRegistro = new JButton("Jugar sin registro");
        btnRegistro.setBounds(180, 110, 150, 30);
        btnRegistro.addActionListener(e -> onJugarSinRegistro());
        p.add(btnRegistro);

        add(p);
    }

    private void onEntrar() {
        String usuario = tfUsuario.getText().trim();
        String pass = new String(pfPass.getPassword()).trim();
        if (usuario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresá usuario (o usa 'Jugar sin registro').");
            return;
        }
        // aquí podés integrar UsuarioDAO real; por ahora generamos Usuario local
        Usuario u = new Usuario(1, usuario, pass, usuario);
        abrirPantallaJuego(u);
    }

    private void onJugarSinRegistro() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre para jugar (visible en partida):", "Jugador");
        if (nombre == null || nombre.isBlank()) nombre = "Jugador";
        Usuario u = new Usuario(1, nombre, "", nombre);
        abrirPantallaJuego(u);
    }

    private void abrirPantallaJuego(Usuario u) {
        SwingUtilities.invokeLater(() -> {
            PantallaJuego pj = new PantallaJuego(u);
            pj.setVisible(true);
        });
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PantallaLogin pl = new PantallaLogin();
            pl.setVisible(true);
        });
    }
}
