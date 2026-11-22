const parametros = new URLSearchParams(window.location.search);
const origen = parametros.get("origen");
const destino = parametros.get("destino");
const perfil = parametros.get("perfil") || "general";

const mensaje = document.getElementById("mensaje");
const resumen = document.getElementById("resumen");
const imagen = document.getElementById("imagen-ruta");
const NOMBRES_PERFIL = { movilidad: "Movilidad reducida", visual: "Visual", general: "General" };

function mostrarError(texto) {
  mensaje.textContent = "Error: " + texto;
  mensaje.classList.add("mensaje--error");
}

function buscarRuta(a, b, perfilElegido) {
  const base = [a, b].sort().join("|");
  return RUTAS[base + "|" + perfilElegido] || RUTAS[base] || null;
}

function mostrarRuta() {
  if (!LUGARES[origen] || !LUGARES[destino]) {
    mostrarError("faltan el origen o el destino. Vuelve a buscar.");
    return;
  }
  const ruta = buscarRuta(origen, destino, perfil);
  if (!ruta) {
    mostrarError("todavía no tenemos la ruta entre " + LUGARES[origen] + " y " + LUGARES[destino] + ".");
    return;
  }
  document.getElementById("recorrido").textContent = LUGARES[origen] + " → " + LUGARES[destino];
  document.getElementById("perfil").textContent = NOMBRES_PERFIL[perfil] || perfil;
  document.getElementById("descripcion").textContent = ruta.descripcion;
  document.getElementById("distancia").textContent = ruta.distancia;
  imagen.alt = ruta.alt;
  imagen.addEventListener("error", function () {
    mostrarError("no se pudo cargar la imagen de la ruta.");
  });
  imagen.src = ruta.imagen;
  resumen.hidden = false;
  mensaje.textContent = "Ruta encontrada.";
}

mostrarRuta();