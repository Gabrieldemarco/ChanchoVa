import ui.PantallaLogin;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PantallaLogin pl = new PantallaLogin();
            pl.setVisible(true);
        });
    }
}
