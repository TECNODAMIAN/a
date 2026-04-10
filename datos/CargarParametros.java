package colectivo.datos;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.datastructures.ProbeHashMap;
import net.datastructures.Map;
/**
 * Clase que carga la info del archivo para preparar la simulación.
 */
public class CargarParametros {
    private static String archivoLinea;
    private static String archivoParada;
    private static String cantidadPasajeros;
    private static String capacidadColectivo;
    private static ProbeHashMap<String, Integer> recorridosPorLinea = new ProbeHashMap<>();


    public static void parametros() throws IOException {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
        }

        archivoLinea = prop.getProperty("linea");
        archivoParada = prop.getProperty("parada");
        cantidadPasajeros = prop.getProperty("cantidadPasajeros");
        capacidadColectivo = prop.getProperty("capacidadColectivo");

        // Cargar dinámicamente los recorridos por línea
        for (String key : prop.stringPropertyNames()) {
            if (key.startsWith("recorridos.")) {
                String codigoLinea = key.substring("recorridos.".length());
                try {
                    int cantidad = Integer.parseInt(prop.getProperty(key).trim());
                    recorridosPorLinea.put(codigoLinea, cantidad);
                } catch (NumberFormatException e) {
                    System.err.println("Valor inválido para " + key + ": " + prop.getProperty(key));
                }
            }
        }
    }

    public static Map<String, Integer> getRecorridosPorLinea() {
        return recorridosPorLinea;
    }

    public static String getArchivoLinea() { return archivoLinea; }
    public static String getArchivoParada() { return archivoParada; }
    public static String getCantidadPasajeros() { return cantidadPasajeros; }
    public static String getCapacidadColectivo() { return capacidadColectivo; }
}
