const LUGARES = {
  BIBLIOTECA: "Biblioteca",
  BLOQUE_16: "Bloque 16",
  BLOQUE_F: "Bloque F",
  PARQUE: "Parque de los estudiantes"
};

// La clave son los dos lugares en orden alfabético, separados por "|".
// Si el recorrido cambia para un perfil, agrega otra entrada con "|movilidad" al final.
const RUTAS = {
  "BIBLIOTECA|BLOQUE_F": {
    imagen: "img/rutas/biblioteca-bloque-f.png",
    alt: "Plano del campus con la ruta entre la Biblioteca y el Bloque F por la entrada principal y la rampa",
    descripcion: "El recorrido pasa por la entrada principal y el pie de la rampa, y llega al Bloque F por la rampa, sin escaleras.",
    distancia: 165
  },
  "BLOQUE_16|BLOQUE_F": {
    imagen: "img/rutas/bloque-16-bloque-f.png",
    alt: "Plano del campus con la ruta entre el Bloque 16 y el Bloque F",
    descripcion: "Los dos bloques se conectan por el ascensor.",
    distancia: 25
  }
  // ...una entrada por cada pareja de lugares
};