package colectivo.interfaz;

import colectivo.modelo.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
/**
 * Clase que tiene todas las funciones para imprimir en el archivo output
 */
public class Interfaz {
    private static final String ARCHIVO_SALIDA = "salida_simulacion.txt";
    private static BufferedWriter escritor;

    static {
        try {
            escritor = new BufferedWriter(new FileWriter(ARCHIVO_SALIDA));
        } catch (IOException e) {
            System.err.println("No se pudo abrir el archivo de salida: " + ARCHIVO_SALIDA);
            e.printStackTrace();
        }
    }

    private static void escribir(String mensaje) {
        try {
            escritor.write(mensaje);
            escritor.newLine();
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + mensaje);
            e.printStackTrace();
        }
    }

    public static void cerrar() {
        try {
            if (escritor != null) {
                escritor.close();
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar el archivo de salida");
            e.printStackTrace();
        }
    }

    // Métodos para mostrar diferentes tipos de información
    public static void mostrarMensaje(String mensaje) {
        escribir(mensaje);
    }

    public static void mostrarInicioSimulacion(int cantidadPasajeros) {
        escribir("\nIniciando simulación con " + cantidadPasajeros + " pasajeros...");
    }

    public static void mostrarError(String mensajeError) {
        escribir("ERROR: " + mensajeError);
    }

    public static void mostrarPasajeroGenerado(Pasajero pasajero, Parada origen, Linea linea, Parada destino) {
        escribir("Pasajero " + pasajero.getId() + " espera en " +
                origen.getDireccion() + " (Línea " + linea.getCodigo() + ") -> " +
                destino.getDireccion());
    }
    public static void mostrarOcupacionPromedio(String codigoLinea, double promedio) {
        escribir(String.format("Línea %s - Ocupación promedio: %.2f%%", codigoLinea, promedio * 100));
    }


    public static void mostrarInicioRecorrido(String codigoLinea) {
        escribir("\n=== Iniciando recorrido completo para línea " + codigoLinea + " ===");
    }

    public static void mostrarDireccionRecorrido(String direccion) {
        escribir("\n--- " + direccion + " ---");
    }

    public static void mostrarParada(Parada parada, int subieron, int bajaron, int aBordo) {
        escribir(String.format("Parada %s (%s): Suben %d, Bajan %d, A bordo: %d",
                parada.getId(), parada.getDireccion(), subieron, bajaron, aBordo));
    }

    public static void mostrarSubidaPasajero(Pasajero pasajero, Parada parada) {
        escribir("  Pasajero " + pasajero.getId() + " subió en " + parada.getDireccion());
    }

    public static void mostrarBajadaPasajero(Pasajero pasajero, Parada parada) {
        escribir("  Pasajero " + pasajero.getId() + " bajó en " + parada.getDireccion());
    }

    public static void mostrarBajadaForzada(int cantidad, Parada parada) {
        escribir("  ¡ATENCIÓN! " + cantidad + " pasajeros bajaron forzadamente en " +
                parada.getDireccion() + " (final del recorrido)");
    }
   

    public static void mostrarCantidadAsientos(int cantidadAsientos) {
        System.out.println("Cantidad de asientos: " + cantidadAsientos);
    }

    public static void mostrarLineaCargada(String codigoLinea, int paradasCargadas) {
        escribir("Línea " + codigoLinea + " cargada con " + paradasCargadas + " paradas");
    }

    public static void mostrarPasajerosEsperando(int cantidad, Parada parada) {
        escribir("  ¡ATENCIÓN! " + cantidad + " pasajeros quedaron esperando en " +
                parada.getDireccion());
    }

    public static void mostrarCapacidadColectivo(int capacidadTotal) {
        System.out.println("Capacidad del colectivo: " + capacidadTotal + " pasajeros");
    }
    public static void mostrarIndiceSatisfaccion(double indice, int[] calificaciones) {
    	 escribir("\nÍndice de satisfacción: " + indice);
        for (int i = 0; i < calificaciones.length; i++) {
        	 escribir("Calificación " + (i+1) + ": " + calificaciones[i] + " pasajeros");
        }
    }
    }


