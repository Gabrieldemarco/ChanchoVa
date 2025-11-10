package datos;

import java.sql.*;

public class PartidaDAO {
    public int crearPartida(int maxJugadores, int prendasPorPerdedor) throws SQLException {
        String sql = "INSERT INTO partidas (max_jugadores, prendas_por_perdedor, estado) VALUES (?, ?, 'EN_JUEGO')";
        try (Connection c = ConexionBD.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, maxJugadores);
            ps.setInt(2, prendasPorPerdedor);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }
}
