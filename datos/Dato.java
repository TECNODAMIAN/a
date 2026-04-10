package colectivo.datos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import net.datastructures.TreeMap;
import colectivo.modelo.Linea;
import colectivo.modelo.Parada;

public class Dato {
    public static TreeMap<String, Linea> cargarLineas(String fileName, TreeMap<String, Parada> paradas) 
            throws FileNotFoundException {
        TreeMap<String, Linea> lineas = new TreeMap<>();
        File archivo = new File(fileName);
        
        if (!archivo.exists()) {
            System.err.println("Archivo de líneas no encontrado: " + archivo.getAbsolutePath());
            return lineas;
        }
        
        try (Scanner scanner = new Scanner(archivo, "UTF-8")) {
            while (scanner.hasNextLine()) {
                String lineaTexto = scanner.nextLine().trim();
                if (lineaTexto.startsWith("#") || lineaTexto.isEmpty()) continue;
                
                String[] partes = lineaTexto.split(";");
                if (partes.length < 2) {
                    System.err.println("Línea mal formada: " + lineaTexto);
                    continue;
                }
                
                String codigoLinea = partes[0].trim();
                Linea linea = new Linea(codigoLinea, "Línea " + codigoLinea);
                int paradasCargadas = 0;
                
                for (int i = 1; i < partes.length; i++) {
                    String idParada = partes[i].trim();
                    if (!idParada.isEmpty()) {
                        Parada parada = paradas.get(idParada);
                        if (parada != null) {
                            linea.agregarParada(parada);
                            paradasCargadas++;
                        } else {
                            System.err.println("Parada no encontrada: " + idParada + " para línea " + codigoLinea);
                        }
                    }
                }
                
                if (paradasCargadas > 0) {
                    lineas.put(codigoLinea, linea);
                    System.out.println("Línea " + codigoLinea + " cargada con " + paradasCargadas + " paradas");
                } else {
                    System.err.println("Línea " + codigoLinea + " no tiene paradas válidas");
                }
            }
        }
        return lineas;
    }

    public static TreeMap<String, Parada> cargarParadas(String fileName) throws FileNotFoundException {
        TreeMap<String, Parada> paradas = new TreeMap<>();
        File archivo = new File(fileName);
        
        if (!archivo.exists()) {
            System.err.println("Archivo de paradas no encontrado: " + archivo.getAbsolutePath());
            return paradas;
        }
        
        try (Scanner scanner = new Scanner(archivo, "UTF-8")) {
            while (scanner.hasNextLine()) {
                String lineaTexto = scanner.nextLine().trim();
                if (lineaTexto.startsWith("#") || lineaTexto.isEmpty()) continue;
                
                String[] partes = lineaTexto.split(";");
                if (partes.length < 2) {
                    System.err.println("Parada mal formada: " + lineaTexto);
                    continue;
                }
                
                String id = partes[0].trim();
                String direccion = partes[1].trim();
                paradas.put(id, new Parada(id, direccion));
            }
        }
        return paradas;
    }
}