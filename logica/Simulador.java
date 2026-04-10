package colectivo.logica;

import java.util.ArrayList;
import java.util.Random;
import net.datastructures.Map;
import net.datastructures.List;
import net.datastructures.PositionalList;
import net.datastructures.ProbeHashMap;
import net.datastructures.Entry;


import colectivo.interfaz.Interfaz;
import colectivo.modelo.Colectivo;
import colectivo.modelo.Linea;
import colectivo.modelo.Parada;
import colectivo.modelo.Pasajero;

public class Simulador {
	private Map<String, Linea> lineas;
    private int cantidadPasajeros;
    private int capacidadColectivo;
    private Map<String, Integer> recorridosPorLinea;
    private Map<Integer, Pasajero> pasajerosGenerados;
    private Map<String, java.util.List<Double>> ocupacionesPorLinea = new ProbeHashMap<>();

    public Simulador(Map<String, Linea> lineas, int cantidadPasajeros, int capacidadColectivo, Map<String, Integer> recorridosPorLinea) {
        this.lineas = lineas;
        this.cantidadPasajeros = cantidadPasajeros;
        this.capacidadColectivo = capacidadColectivo;
        this.recorridosPorLinea = recorridosPorLinea;
        this.ocupacionesPorLinea = new ProbeHashMap<>();
    }


    public void ejecutar() {
        Interfaz.mostrarInicioSimulacion(cantidadPasajeros);
        Interfaz.mostrarCapacidadColectivo(capacidadColectivo);

        GeneradorPasajeros generador = new GeneradorPasajeros(lineas, cantidadPasajeros, recorridosPorLinea);
        pasajerosGenerados = generador.generar();

        simularRecorridosFijos();
        calcularIndiceSatisfaccion();
     // Al terminar recorridos, mostrar ocupación promedio
        Interfaz.mostrarMensaje("\nOcupación promedio por línea:");
        for (Entry<String, java.util.List<Double>> entry : ocupacionesPorLinea.entrySet()) {
            String linea = entry.getKey();
            java.util.List<Double> valores = entry.getValue();

            double suma = 0.0;
            for (double o : valores) suma += o;
            double promedio = valores.isEmpty() ? 0.0 : suma / valores.size();

            Interfaz.mostrarOcupacionPromedio(linea, promedio);
        }
        
        // Cerrar archivo
        //Interfaz.cerrar();
    }
    
    /**
     * Calcula y promedia las reseñas de todos los usuarios
     */

    private void calcularIndiceSatisfaccion() {
        int[] calificaciones = new int[5];
        int total = 0;

        for (Pasajero p : pasajerosGenerados.values()) {
            int cal = calcularCalificacion(p);
            calificaciones[cal - 1]++;
            total++;
        }

        int suma = 0;
        for (int i = 0; i < 5; i++) suma += calificaciones[i] * (i + 1);
        double indice = total == 0 ? 0 : (double) suma / (total * 5);

        Interfaz.mostrarMensaje("\nÍndice de satisfacción del cliente: " + String.format("%.2f", indice));
        for (int i = 4; i >= 0; i--) {
            Interfaz.mostrarMensaje("Calificación " + (i + 1) + ": " + calificaciones[i] + " pasajeros");
        }
    }

    /**
     * Calcula la reseña de un usuario (cuantas estrellas deja)
     * @param p el pasajero
     */
    private int calcularCalificacion(Pasajero p) {
        if (!p.isSubio()) return 1;
        int espera = p.getColectivosEsperados();
        /*if (espera == 1) return p.isViajoSentado() ? 5 : 4;
        if (espera == 2) return 3;
        return 2;*/
        switch(espera)
        {
        case 1:
        	return p.isViajoSentado() ? 5 : 4;
        case 2:
        	return 3;
        default:
        	return 2;
        }
    }

    /**
     * Calcula las paradas que van a recorrer los colectivos y llama a la ejecución
     * 
     */
    private void simularRecorridosFijos() {
        ProbeHashMap<String, Integer> contadorRecorridos = new ProbeHashMap<>();
        java.util.List<String> lineasBase = new ArrayList<>();

        for (String codigo : lineas.keySet()) {
            String base = codigo.replace("I", "").replace("R", "");
            if (!lineasBase.contains(base)) {
                lineasBase.add(base);
            }
        }

        for (String codigoBase : lineasBase) {
            Linea lineaIda = lineas.get(codigoBase + "I");
            Linea lineaVuelta = lineas.get(codigoBase + "R");

            if (lineaIda != null && lineaVuelta != null) {
                Integer totalRecorridos = recorridosPorLinea.get(codigoBase);
                if (totalRecorridos == null) totalRecorridos = 1;

                for (int i = 1; i <= totalRecorridos; i++) {
                    Colectivo colectivo = new Colectivo(codigoBase, lineaIda, capacidadColectivo);

                    Interfaz.mostrarInicioRecorrido(String.format(
                            "Línea %s - Recorrido %d/%d", codigoBase, i, totalRecorridos));

                    simularRecorridoGeneral(colectivo, lineaIda, "Ida", false);
                    
                    
                    simularRecorridoGeneral(colectivo, lineaVuelta, "Vuelta", true);

                    contadorRecorridos.put(codigoBase, i);
                }
            }
        }

        Interfaz.mostrarMensaje("\nSimulación completada. Recorridos realizados:");
        for (Entry<String, Integer> entry : contadorRecorridos.entrySet()) {
            Integer total = recorridosPorLinea.get(entry.getKey());
            if (total == null) total = 1;
            Interfaz.mostrarMensaje("- Línea " + entry.getKey() + ": " + entry.getValue() + " de " + total + " recorridos");
        }
    }
    
    /**
     * Ejecuta el recorrido ida y vuelta para el colectivo y lineas parámetro
     * @param colectivo
     * @param linea
     * @param direccion
     */
    
    private void simularRecorridoGeneral(Colectivo colectivo, Linea linea, String direccion, boolean esVuelta) {
    	colectivo.setLineaActual(linea);
        Interfaz.mostrarDireccionRecorrido(direccion);
        PositionalList<Parada> paradas = linea.getParadas();
        java.util.List<Double> ocupaciones = new ArrayList<>();

        int totalParadas = contarParadas(paradas);
        int paradaActual = 0;

        for (Parada parada : paradas) {
            paradaActual++;

            int bajaron = procesarBajadas(colectivo, parada);
            int subieron = procesarSubidas(colectivo, parada);

            double ocupacion = colectivo.getPasajeros().size() / (double) colectivo.getCapacidadMaxima();
            ocupaciones.add(ocupacion);

            Interfaz.mostrarParada(parada, subieron, bajaron, colectivo.getPasajeros().size());
        }

        // Calcular promedio de ocupación y registrar
        double suma = 0.0;
        for (double o : ocupaciones) suma += o;
        double promedio = ocupaciones.isEmpty() ? 0.0 : suma / ocupaciones.size();

        String codigoBase = colectivo.getId();
        java.util.List<Double> lista = ocupacionesPorLinea.get(codigoBase);
        if (lista == null) {
            lista = new ArrayList<>();
            ocupacionesPorLinea.put(codigoBase, lista);
        }
        lista.add(promedio);
    }
    
    /**
     * Baja a los pasajeros que haya llegado a destino
     * @param colectivo
     * @param parada
     */

    private int procesarBajadas(Colectivo colectivo, Parada parada) {
        int bajaron = 0;
        int pasajerosABordo = colectivo.getPasajeros().size();

        for (int i = 0; i < pasajerosABordo; i++) {
            Pasajero pasajero = colectivo.getPasajeros().dequeue();
            if (pasajero.getDestino().equals(parada)) {
                bajaron++;
                Interfaz.mostrarBajadaPasajero(pasajero, parada);
            } else {
                colectivo.getPasajeros().enqueue(pasajero);
            }
        }
        return bajaron;
    }

    
    /**
     * Sube a los pasajeros si esperan al colectivo que llegó, y si está lleno no los deja subir.
     * @param colectivo
     * @param parada
     */

    private int procesarSubidas(Colectivo colectivo, Parada parada) {
        int subieron = 0;

        // Paso 1: todos los que están esperando ven pasar un colectivo e intentan subir si es el suyo
        
        int tamaño = parada.getPasajeros().size();
        for (int i = 0; i < tamaño; i++) {
            Pasajero pasajero = parada.getPasajeros().dequeue();
            
            if (!pasajero.getP_linea().equals(colectivo.getLineaActual())) 
            {            	
            	parada.getPasajeros().enqueue(pasajero);
            }
            
            
            else
            {
            	//if (!colectivo.tieneEspacio()) break;
            	pasajero.incrementarColectivosEsperados();
                

                if (colectivo.hayAsientosDisponibles()) {
                    colectivo.incrementarPasajerosSentados();
                    pasajero.setViajoSentado(true);
                } 
                else if (colectivo.hayEspacioDePieDisponible()) {
                    pasajero.setViajoSentado(false);
                } 
                else {
                    // Si no hay espacio de ningún tipo, lo devolvemos al final de la cola
                    parada.getPasajeros().enqueue(pasajero);
                    
                    continue;
                }

                pasajero.setSubio(true);
                colectivo.getPasajeros().enqueue(pasajero);
                Interfaz.mostrarSubidaPasajero(pasajero, parada);
                subieron++;
            }
        }

        
        // Paso 2: informar si quedó gente esperando
        if (!parada.getPasajeros().isEmpty()) {
            Interfaz.mostrarPasajerosEsperando(parada.getPasajeros().size(), parada);
        }

        return subieron;
    }



    public static int contarParadas(PositionalList<Parada> paradas) {
        int count = 0;
        for (Parada p : paradas) {
            count++;
        }
        return count;
    }

}