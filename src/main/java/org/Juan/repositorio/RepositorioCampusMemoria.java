package org.Juan.repositorio;

import org.Juan.excepcion.UbicacionNoEncontradaException;
import org.Juan.modelo.tramo.*;
import org.Juan.modelo.ubicacion.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
/**
 * Repositorio que guarda el mapa del campus en memoria. Al crearse arma
 * todos los lugares y todos los caminos que los unen, y despues solo los
 * entrega cuando alguien los pide. Los datos estan escritos directamente
 * en el codigo, no vienen de una base de datos, asi que no cambian mientras
 * el programa este corriendo.
 *
 * @version 1.0
 */

@Repository
public class RepositorioCampusMemoria implements RepositorioCampus {
    /** Todos los lugares del campus: los que se pueden escoger y los puntos de paso. */
    private final List<Ubicacion> ubicaciones = new ArrayList<>();
    /** Todos los caminos que unen esos lugares. Juntos forman el grafo del campus. */
    private final List<Tramo> tramos = new ArrayList<>();

    /**
     * Arma el mapa completo del campus. Primero crea los 18 lugares que
     * la persona puede escoger y 3 puntos de paso, luego los guarda en la
     * lista de ubicaciones, y al final crea los tramos que los conectan,
     * agrupados por zonas. No recibe parametros porque el mapa es fijo.
     */
    public RepositorioCampusMemoria() {
        // 1. Los 18 lugares seleccionables (IDs idénticos a rutas-datos.js)
        Ubicacion banoBiblioteca = new PuntoServicio("banobiblioteca", "Baño de la Biblioteca");
        Ubicacion biblioteca = new PuntoServicio("biblioteca", "Biblioteca");
        Ubicacion bloque16 = new Bloque("bloque16", "Bloque 16");
        Ubicacion bloqueF = new Bloque("bloquef", "Bloque F");
        Ubicacion bloqueFisio = new Bloque("bloquefisioterapia", "Bloque de Fisioterapia");
        Ubicacion caf1 = new PuntoServicio("cafeteria1", "Cafetería 1");
        Ubicacion caf2 = new PuntoServicio("cafeteria2", "Cafetería 2");
        Ubicacion caf3 = new PuntoServicio("cafeteria3", "Cafetería 3");
        Ubicacion caf4 = new PuntoServicio("cafeteria4", "Cafetería 4");
        Ubicacion capilla = new PuntoServicio("capilla", "Capilla");
        Ubicacion est = new PuntoServicio("estacionamiento", "Estacionamiento");
        Ubicacion estCarros = new PuntoServicio("estacionamientocarros", "Estacionamiento de carros");
        Ubicacion estMotos = new PuntoServicio("estacionamientomotos", "Estacionamiento de motos");
        Ubicacion estU = new PuntoServicio("estacionamientou", "Estacionamiento U");
        Ubicacion parque = new PuntoServicio("parqueestudiantes", "Parque de los estudiantes");
        Ubicacion salud = new PuntoServicio("salud", "Salud");
        Ubicacion tienda = new PuntoServicio("tiendauam", "Tienda UAM");
        Ubicacion tren = new PuntoServicio("tren", "Tren");

        // 2. Puntos de paso intermedios para conectar el grafo
        Ubicacion entrada = new PuntoPaso("entradaprincipal", "Entrada principal");
        Ubicacion pieRampa = new PuntoPaso("pierampa", "Pie de rampa Bloque F");
        Ubicacion plazoleta = new PuntoPaso("plazoletacentral", "Plazoleta central");

        ubicaciones.addAll(List.of(
                banoBiblioteca, biblioteca, bloque16, bloqueF, bloqueFisio,
                caf1, caf2, caf3, caf4, capilla, est, estCarros, estMotos,
                estU, parque, salud, tienda, tren, entrada, pieRampa, plazoleta
        ));

        // 3. Conexiones del grafo (Tramos)
        // Zona Biblioteca y Entrada
        tramos.add(new TramoPlano(biblioteca, banoBiblioteca, 15));
        tramos.add(new TramoPlano(biblioteca, entrada, 40));
        tramos.add(new TramoPlano(entrada, tienda, 35));
        tramos.add(new TramoPlano(entrada, plazoleta, 50));

        // Zona Bloque 16 y Bloque F (con alternativa accesible y escalera)
        tramos.add(new TramoEscalera(entrada, bloque16, 30));
        tramos.add(new TramoPlano(plazoleta, bloque16, 45));
        tramos.add(new TramoPlano(plazoleta, pieRampa, 40));
        tramos.add(new TramoRampa(pieRampa, bloqueF, 35));
        tramos.add(new TramoAscensor(bloque16, bloqueF, 25));

        // Zona Cafeterías y Parque
        tramos.add(new TramoPlano(plazoleta, caf1, 30));
        tramos.add(new TramoPlano(caf1, caf2, 25));
        tramos.add(new TramoPlano(caf2, parque, 40));
        tramos.add(new TramoPlano(parque, caf3, 35));
        tramos.add(new TramoPlano(caf3, caf4, 30));
        tramos.add(new TramoRampa(caf4, bloqueFisio, 50));

        // Zona Salud, Capilla, Tren y Estacionamientos
        tramos.add(new TramoPlano(plazoleta, salud, 60));
        tramos.add(new TramoPlano(salud, capilla, 45));
        tramos.add(new TramoRampa(capilla, tren, 55));
        tramos.add(new TramoPlano(tren, estU, 40));
        tramos.add(new TramoPlano(entrada, est, 65));
        tramos.add(new TramoPlano(est, estCarros, 30));
        tramos.add(new TramoPlano(estCarros, estMotos, 25));
    }

    /**
     * Devuelve todos los lugares del campus, incluidos los puntos de paso.
     * Entrega una copia de la lista, asi quien la reciba no puede cambiar
     * la lista original.
     *
     * @return una copia de la lista con todas las ubicaciones
     */
    @Override
    public List<Ubicacion> obtenerTodas() {
        return new ArrayList<>(ubicaciones);
    }

    /**
     * Busca un lugar por su id, sin importar si esta en mayusculas o minusculas.
     *
     * @param id id del lugar que se busca (por ejemplo, "bloquef")
     * @return el lugar que tiene ese id
     * @throws UbicacionNoEncontradaException si no existe ningun lugar con ese id
     */
    @Override
    public Ubicacion buscarPorId(String id) {
        for (Ubicacion ubicacion : ubicaciones) {
            if (ubicacion.getId().equalsIgnoreCase(id)) {
                return ubicacion;
            }
        }
        throw new UbicacionNoEncontradaException("No se encontró el lugar: " + id);
    }

    /**
     * Devuelve todos los tramos del campus. Entrega una copia de la lista,
     * asi quien la reciba no puede cambiar la lista original.
     *
     * @return una copia de la lista con todos los tramos
     */
    @Override
    public List<Tramo> obtenerTramos() {
        return new ArrayList<>(tramos);
    }
}