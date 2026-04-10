package colectivo.modelo;

/**
 * Clase que representa a un pasajero con una linea y parada destino. También representa las condiciones y satisfacción de su viaje
 */
public class Pasajero {
    private int id;
    private Parada destino;
    private Linea P_linea;

    // Campos nuevos para el índice de satisfacción
    private int colectivosEsperados = 0;
    private boolean viajoSentado = false;
    private boolean subio = false;

    public Pasajero(int id, Parada destino) {
        this.id = id;
        this.destino = destino;
    }

    // Getters existentes
    public int getId() { return id; }
    public Parada getDestino() { return destino; }

    // ===== Nuevos métodos =====
    public void incrementarColectivosEsperados() {
        colectivosEsperados++;
    }
    public Linea getP_linea() {
    	return P_linea;
    	
    }
    public void setP_linea(Linea id) {
    this.P_linea=id;
    }

    public int getColectivosEsperados() {
        return colectivosEsperados;
    }

    public void setViajoSentado(boolean viajoSentado) {
        this.viajoSentado = viajoSentado;
    }

    public boolean isViajoSentado() {
        return viajoSentado;
    }

    public void setSubio(boolean subio) {
        this.subio = subio;
    }

    public boolean isSubio() {
        return subio;
    }
}
