package org.Juan.servicio;

import org.Juan.estrategia.EstrategiaRuta;
import org.Juan.estrategia.RutaMasAccesible;
import org.Juan.estrategia.RutaMasCorta;
import org.Juan.modelo.Ruta;
import org.Juan.modelo.perfil.PerfilAccesibilidad;
import org.Juan.modelo.perfil.PerfilGeneral;
import org.Juan.modelo.perfil.PerfilMovilidadReducida;
import org.Juan.modelo.perfil.PerfilVisual;
import org.Juan.modelo.tramo.Tramo;
import org.Juan.modelo.ubicacion.Ubicacion;
import org.Juan.repositorio.RepositorioCampus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioRutas {

    private final RepositorioCampus repositorio;
    private final EstrategiaRuta rutaMasCorta = new RutaMasCorta();
    private final EstrategiaRuta rutaMasAccesible = new RutaMasAccesible();

    public ServicioRutas(RepositorioCampus repositorio) {
        this.repositorio = repositorio;
    }

    public Ruta buscarRuta(String idOrigen, String idDestino, String codigoPerfil) {
        if (idOrigen.equals(idDestino)) {
            throw new IllegalArgumentException("El origen y el destino son el mismo lugar.");
        }

        Ubicacion origen = repositorio.buscarPorId(idOrigen);
        Ubicacion destino = repositorio.buscarPorId(idDestino);
        PerfilAccesibilidad perfil = crearPerfil(codigoPerfil);

        // Solo pasan los tramos que este perfil puede usar.
        // (Más adelante aquí también se descartan los tramos bloqueados.)
        List<Tramo> disponibles = new ArrayList<>();
        for (Tramo tramo : repositorio.obtenerTramos()) {
            if (perfil.puedeUsar(tramo)) {
                disponibles.add(tramo);
            }
        }

        EstrategiaRuta estrategia = perfil.priorizaDistancia() ? rutaMasCorta : rutaMasAccesible;
        return estrategia.calcular(origen, destino, disponibles, perfil);
    }

    private PerfilAccesibilidad crearPerfil(String codigo) {
        switch (codigo) {
            case "movilidad":
                return new PerfilMovilidadReducida();
            case "visual":
                return new PerfilVisual();
            case "general":
                return new PerfilGeneral();
            default:
                throw new IllegalArgumentException("Perfil desconocido: " + codigo);
        }
    }
}