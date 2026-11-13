package org.Juan.modelo.ubicacion;

/** Un lugar de servicio: Cafetería, Biblioteca, Tienda UAM... */
public class PuntoServicio extends Ubicacion {

    public PuntoServicio(String id, String nombre) {
        super(id, nombre);
    }

    @Override
    public String getTipo() {
        return "Punto de servicio";
    }
}