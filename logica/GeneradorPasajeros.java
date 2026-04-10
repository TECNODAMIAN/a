package colectivo.logica;

import colectivo.modelo.Linea; 
import colectivo.modelo.Parada;
import colectivo.modelo.Pasajero;
import colectivo.interfaz.Interfaz;
import net.datastructures.Map;
import net.datastructures.PositionalList;
import net.datastructures.ProbeHashMap;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;
/**
 * Clase que genera a los pasajeros y su destino
 */
public class GeneradorPasajeros {
    private Map<String, Linea> lineas;
    private int cantidadPasajeros;
    private Random random = new Random();
    private Map<String, Integer> recorridosPorLinea;
    
    // Estructuras optimizadas
    private List<LineaInfo> lineasOptimizadas;
    private int totalRecorridos;

    public GeneradorPasajeros(Map<String, Linea> lineas, int cantidadPasajeros, Map<String, Integer> recorridosPorLinea) {
        this.lineas = lineas;
        this.cantidadPasajeros = cantidadPasajeros;
        this.recorridosPorLinea = recorridosPorLinea;
        this.lineasOptimizadas = new ArrayList<>();
        
        preprocesarLineas();
    }

    // Clase auxiliar para almacenar datos preprocesados
    private class LineaInfo {
        Linea linea;
        int peso;
        List<Parada> paradasLista;
        
        LineaInfo(Linea linea, int peso, List<Parada> paradasLista) {
            this.linea = linea;
            this.peso = peso;
            this.paradasLista = paradasLista;
        }
    }

    private void preprocesarLineas() {
        totalRecorridos = 0;
        
        for (Linea linea : lineas.values()) {
            String codigo = linea.getCodigo();
            if ((codigo.endsWith("I") || codigo.endsWith("R")) && Simulador.contarParadas(linea.getParadas()) >= 2) {
                String base = codigo.substring(0, codigo.length() - 1);
                Integer recorridos = recorridosPorLinea.get(base);
                if (recorridos != null) {
                    // Convertir paradas a lista una sola vez
                    List<Parada> paradasLista = convertirParadasALista(linea.getParadas());
                    lineasOptimizadas.add(new LineaInfo(linea, recorridos, paradasLista));
                    totalRecorridos += recorridos;
                }
            }
        }
    }

    private List<Parada> convertirParadasALista(PositionalList<Parada> paradas) {
        List<Parada> lista = new ArrayList<>();
        for (Parada p : paradas) lista.add(p);
        return lista;
    }

    public Map<Integer, Pasajero> generar() {
        Map<Integer, Pasajero> pasajerosGenerados = new ProbeHashMap<>();

        if (lineasOptimizadas.isEmpty()) {
            Interfaz.mostrarError("No hay líneas con suficientes paradas");
            return pasajerosGenerados;
        }

        // Precalcular sumas acumulativas para selección ponderada eficiente
        int[] sumasAcumulativas = new int[lineasOptimizadas.size()];
        sumasAcumulativas[0] = lineasOptimizadas.get(0).peso;
        for (int i = 1; i < lineasOptimizadas.size(); i++) {
            sumasAcumulativas[i] = sumasAcumulativas[i-1] + lineasOptimizadas.get(i).peso;
        }

        for (int i = 0; i < cantidadPasajeros; i++) {
            LineaInfo lineaSeleccionada = seleccionarLineaPonderada(sumasAcumulativas);
            
            if (lineaSeleccionada != null) {
                List<Parada> paradas = lineaSeleccionada.paradasLista;
                Parada origen = seleccionarParadaAleatoria(paradas);
                Parada destino = seleccionarParadaDiferente(paradas, origen);
                

                if (origen != null && destino != null) {
                    Pasajero pasajero = new Pasajero(i + 1, destino);
                    pasajero.setP_linea(lineaSeleccionada.linea);
                    origen.agregarPasajero(pasajero);
                    pasajerosGenerados.put(pasajero.getId(), pasajero);
                    Interfaz.mostrarPasajeroGenerado(pasajero, origen, lineaSeleccionada.linea, destino);
                }
            }
        }

        return pasajerosGenerados;
    }
    /**
     * Selecciona la línea que tomará el pasajero
     */
    private LineaInfo seleccionarLineaPonderada(int[] sumasAcumulativas) {
        if (sumasAcumulativas.length == 0) return null;
        
        int rand = random.nextInt(totalRecorridos);
        // Búsqueda binaria para encontrar la línea correspondiente
        int low = 0, high = sumasAcumulativas.length - 1;
        
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (rand < sumasAcumulativas[mid]) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        
        return lineasOptimizadas.get(low);
    }
    /**
     * Selecciona una parada para que el pasajero espere
     */
    private Parada seleccionarParadaAleatoria(List<Parada> paradas) {
        return paradas.isEmpty() ? null : paradas.get(random.nextInt(paradas.size()-1));
    	//return paradas.isEmpty() ? null : paradas.get(5);
    }
    /**
     * Selecciona una parada distinta y posterior en la misma linea para el destino del pasajero
     */
    private Parada seleccionarParadaDiferente(List<Parada> paradas, Parada excluir) {
        if (paradas.size() <= 1) return null;

        int index = paradas.indexOf(excluir);
        

        // Elegimos un índice aleatorio posterior
        int aux = index + 1;
        int aux_2 = paradas.size() - aux;
        //System.out.printf("posibles_chances: %s %d %d\n",paradas.getFirst().getId().toString(), aux, aux_2);
        //if (aux_2 <=0) return paradas.getLast();
        //System.out.println(paradas.size() + paradas.toString());
        
        int seleccionIndex = index + 1 + random.nextInt(aux_2);
        return paradas.get(seleccionIndex);
    }

}