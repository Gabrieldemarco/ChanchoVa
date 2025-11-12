package modelos;

public class Carta {
    private int valor;
    private String palo;

    public Carta(String palo,int valor) {
        this.valor = valor;
        this.palo = palo;
    }

    public int getValor() { return valor; }
    public String getPalo() { return palo; }

    @Override
    public String toString() {
        return valor + palo;
    }
}
