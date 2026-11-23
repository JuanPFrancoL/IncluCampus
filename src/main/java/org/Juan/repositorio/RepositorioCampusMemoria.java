package org.Juan.repositorio;

import org.Juan.excepcion.UbicacionNoEncontradaException;
import org.Juan.modelo.tramo.*;
import org.Juan.modelo.ubicacion.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RepositorioCampusMemoria implements RepositorioCampus {

    private final List<Ubicacion> ubicaciones = new ArrayList<>();
    private final List<Tramo> tramos = new ArrayList<>();

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

    @Override
    public List<Ubicacion> obtenerTodas() {
        return new ArrayList<>(ubicaciones);
    }

    @Override
    public Ubicacion buscarPorId(String id) {
        for (Ubicacion ubicacion : ubicaciones) {
            if (ubicacion.getId().equalsIgnoreCase(id)) {
                return ubicacion;
            }
        }
        throw new UbicacionNoEncontradaException("No se encontró el lugar: " + id);
    }

    @Override
    public List<Tramo> obtenerTramos() {
        return new ArrayList<>(tramos);
    }
}