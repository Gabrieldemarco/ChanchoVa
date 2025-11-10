package datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    // cadena simple sin SSL ni timezone, como pidió el usuario
    private static final String URL = "jdbc:mysql://localhost:3306/chancho_va";
    private static final String USUARIO = "root";
    private static final String PASS = "orion1"; // pon tu contraseña aquí

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("No se cargó el driver: " + e.getMessage());
        }
    }

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASS);
    }
}
