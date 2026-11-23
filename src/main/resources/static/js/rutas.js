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

// El plano sirve en los dos sentidos: Biblioteca→Bloque F y Bloque F→Biblioteca
function buscarRuta(a, b) {
  const clave = [a, b].sort().join("|");
  return RUTAS[clave] || null;
}

function mostrarRuta() {
  if (!LUGARES[origen] || !LUGARES[destino]) {
    mostrarError("faltan el origen o el destino. Vuelve a buscar.");
    return;
  }
  const archivo = buscarRuta(origen, destino);
  if (!archivo) {
    mostrarError("todavía no tenemos el plano entre " + LUGARES[origen] + " y " + LUGARES[destino] + ".");
    return;
  }
  const recorrido = LUGARES[origen] + " → " + LUGARES[destino];
  document.getElementById("recorrido").textContent = recorrido;
  document.getElementById("perfil").textContent = NOMBRES_PERFIL[perfil] || perfil;
  document.getElementById("descripcion").textContent =
    "Sigue la línea marcada en el plano para ir de " + LUGARES[origen] + " a " + LUGARES[destino] + ".";
  imagen.alt = "Plano del campus con la ruta de " + LUGARES[origen] + " a " + LUGARES[destino];
  imagen.addEventListener("error", function () {
    mostrarError("no se pudo cargar la imagen de la ruta.");
  });
  imagen.src = "img/rutas/" + archivo;
  resumen.hidden = false;
  mensaje.textContent = "Ruta encontrada.";
}

mostrarRuta();
