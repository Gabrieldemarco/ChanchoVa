package datos;

import modelos.Usuario;
import java.sql.*;

public class UsuarioDAO {

    public int registrarUsuario(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre_usuario, contraseña_hash, nombre_completo) VALUES (?, ?, ?)";
        try (Connection c = ConexionBD.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNombreUsuario());
            ps.setString(2, u.getContrasena());
            ps.setString(3, u.getNombreCompleto());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public Usuario login(String nombreUsuario, String hashContrasena) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ?";
        try (Connection c = ConexionBD.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("contraseña_hash");
                    if (hash.equals(hashContrasena)) {
                        return new Usuario(rs.getInt("usuario_id"), rs.getString("nombre_usuario"), hash, rs.getString("nombre_completo"));
                    }
                }
            }
        }
        return null;
    }
}
