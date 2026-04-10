package colectivo.aplicacion;

import java.io.IOException;

import net.datastructures.Map;
import net.datastructures.TreeMap;
import colectivo.datos.CargarParametros;
import colectivo.datos.Dato;
import colectivo.logica.Simulador;
import colectivo.modelo.Linea;
import colectivo.modelo.Parada;
import colectivo.interfaz.Interfaz;

public class Aplicacion {
    public static void main(String[] args) {
        try {
            // Cargar parámetros
            CargarParametros.parametros();

            // Cargar datos
            TreeMap<String, Parada> paradas = Dato.cargarParadas(CargarParametros.getArchivoParada());
            TreeMap<String, Linea> lineas = Dato.cargarLineas(CargarParametros.getArchivoLinea(), paradas);

            // Configurar simulación
            int cantidadPasajeros = Integer.parseInt(CargarParametros.getCantidadPasajeros());
            int capacidadColectivo = Integer.parseInt(CargarParametros.getCapacidadColectivo());

            // Obtener recorridos por línea desde config
            Map<String, Integer> recorridosPorLinea = CargarParametros.getRecorridosPorLinea();

            // Ejecutar simulación con medición de tiempo
            Simulador simulador = new Simulador(lineas, cantidadPasajeros, capacidadColectivo, recorridosPorLinea);

            long inicio = System.nanoTime(); // ⏱ Inicio de medición
            simulador.ejecutar();
            long fin = System.nanoTime();    // ⏱ Fin de medición

            double duracionMs = (fin - inicio) / 1_000_000.0;
            System.out.printf("⏱ Tiempo de ejecución de la simulación: %.3f ms%n", duracionMs);

        } catch (IOException e) {
            System.err.println("Error al cargar parámetros o archivos");
            System.exit(-1);
        } catch (NumberFormatException e) {
            System.err.println("Error en el formato de parámetros numéricos");
            System.exit(-1);
        }
        Interfaz.cerrar();
    }
}
