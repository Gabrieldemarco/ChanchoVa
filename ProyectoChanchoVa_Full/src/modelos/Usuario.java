package modelos;

public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
    private String contrasena;
    private String nombreCompleto;

    public Usuario(int idUsuario, String nombreUsuario, String contrasena, String nombreCompleto) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombreCompleto = nombreCompleto;
    }

    public Usuario(String nombreUsuario, String contrasena, String nombreCompleto) {
        this(0, nombreUsuario, contrasena, nombreCompleto);
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int id) { this.idUsuario = id; }

    public String getNombreUsuario() { return nombreUsuario; }
    public String getContrasena() { return contrasena; }
    public String getNombreCompleto() { return nombreCompleto; }
}