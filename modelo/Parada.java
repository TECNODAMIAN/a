package colectivo.modelo;

import net.datastructures.LinkedQueue;
/**
 * Clase que representa a una parada de colectivo y a los pasajeros que en ella esperan (cola)
 */
public class Parada {
    private String id;
    private String direccion;
    private LinkedQueue<Pasajero> pasajeros = new LinkedQueue<>();

    public Parada(String id, String direccion) {
        this.id = id;
        this.direccion = direccion;
    }

    public String getId() { return id; }
    public String getDireccion() { return direccion; }
    public LinkedQueue<Pasajero> getPasajeros() { return pasajeros; }
    
    public void agregarPasajero(Pasajero pasajero) {
        pasajeros.enqueue(pasajero);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Parada)) return false;
        Parada otra = (Parada) obj;
        return id.equals(otra.id);
    }

	@Override
	public String toString() {
		return "Parada [id=" + id + "]";
	}
}