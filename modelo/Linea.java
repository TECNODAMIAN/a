package colectivo.modelo;

import net.datastructures.PositionalList;
import net.datastructures.LinkedPositionalList;
/**
 * Clase que representa a una línea de paradas, las cuales están en orden en una lista posicional
 */
public class Linea {
    private String codigo;
    private String nombre;
    private PositionalList<Parada> paradas = new LinkedPositionalList<>();
    

    public Linea(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public PositionalList<Parada> getParadas() { return paradas; }
    
    public void agregarParada(Parada parada) {
        paradas.addLast(parada);
    }
}