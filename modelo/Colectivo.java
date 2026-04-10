package colectivo.modelo;

import net.datastructures.LinkedQueue;
/**
 * Clase que representa a un colectivo, tiene su lína de recorrido y sus pasajeros actuales (cola)
 */
public class Colectivo {
	 private String id;
	    private Linea lineaActual;
	    private LinkedQueue<Pasajero> pasajeros = new LinkedQueue<>();
	    private int capacidadMaxima;
	    private int pasajerosSentados = 0;
	    private final int capacidadAsientos;
	    private final int capacidadDePie;

	    public Colectivo(String id, Linea lineaInicial, int capacidadMaxima) {
	        this.id = id;
	        this.lineaActual = lineaInicial;
	        this.capacidadMaxima = capacidadMaxima;
	        this.capacidadAsientos = (int) Math.round(capacidadMaxima * 0.75); // 75% sentados
	        this.capacidadDePie = capacidadMaxima - capacidadAsientos; // 25% de pie
	    }

	    public boolean subirPasajero(Pasajero pasajero) {
	        if (!tieneEspacio()) return false;

	        if (hayAsientosDisponibles()) {
	            pasajero.setViajoSentado(true);
	            pasajerosSentados++;
	        } else if (hayEspacioDePieDisponible()) {
	            pasajero.setViajoSentado(false);
	        } else {
	            return false; // No debería ocurrir si tienEspacio() == true
	        }

	        pasajeros.enqueue(pasajero);
	        return true;
	    }

	    public boolean tieneEspacio() {
	        return pasajeros.size() < capacidadMaxima;
	    }

	    public boolean hayAsientosDisponibles() {
	        return pasajerosSentados < capacidadAsientos;
	    }

	    public boolean hayEspacioDePieDisponible() {
	    	//return (pasajeros.size() - pasajerosSentados) < capacidadDePie;
	        return (capacidadMaxima > pasajeros.size());
	    }

    public void incrementarPasajerosSentados() {
        if (hayAsientosDisponibles()) pasajerosSentados++;
    }

    public void decrementarPasajerosSentados() {
        if (pasajerosSentados > 0) pasajerosSentados--;
    }

    public int getPasajerosSentados() {
        return pasajerosSentados;
    }

    public int getCapacidadAsientos() {
        return capacidadAsientos;
    }

    public int getCapacidadDePie() {
        return capacidadDePie;
    }

    // Getters existentes
    public String getId() { return id; }
    public Linea getLineaActual() { return lineaActual; }
    public void setLineaActual(Linea lineaActual) {
		this.lineaActual = lineaActual;
	}

	public LinkedQueue<Pasajero> getPasajeros() { return pasajeros; }
    public int getCapacidadMaxima() { return capacidadMaxima; }
    
}
