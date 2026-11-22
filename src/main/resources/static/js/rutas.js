const parametros = new URLSearchParams(window.location.search);
const origen = parametros.get("origen");
const destino = parametros.get("destino");
const perfil = parametros.get("perfil") || "general";

const mensaje = document.getElementById("mensaje");
const resumen = document.getElementById("resumen");
const NOMBRES_PERFIL = { movilidad: "Movilidad reducida", visual: "Visual", general: "General" };
const ETIQUETAS_TRAMO = { RAMPA: "rampa", ASCENSOR: "ascensor", ESCALERA: "escaleras", PLANO: "" };

function mostrarError(texto) {
  mensaje.textContent = "Error: " + texto;
  mensaje.classList.add("mensaje--error");
}

function mostrarRuta(ruta) {
  const puntos = ruta.puntos;
  document.getElementById("recorrido").textContent =
    puntos[0].nombre + " → " + puntos[puntos.length - 1].nombre;
  document.getElementById("perfil").textContent = NOMBRES_PERFIL[perfil] || perfil;
  document.getElementById("distancia").textContent = Math.round(ruta.distanciaTotal);

  const lista = document.getElementById("pasos");
  lista.replaceChildren();
  ruta.tramos.forEach(function (tramo, i) {
    const extra = ETIQUETAS_TRAMO[tramo.tipo];
    const li = document.createElement("li");
    li.textContent = puntos[i].nombre + " → " + puntos[i + 1].nombre +
      (extra ? " (" + extra + ")" : "") + " · " + Math.round(tramo.distancia) + " m";
    lista.appendChild(li);
  });
  const llegada = document.createElement("li");
  llegada.textContent = "Llegada: " + puntos[puntos.length - 1].nombre;
  lista.appendChild(llegada);

  resumen.hidden = false;
  mensaje.textContent = "Ruta calculada.";
}

async function cargarRuta() {
  if (!origen || !destino) {
    mostrarError("faltan el origen o el destino. Vuelve a buscar.");
    return;
  }
  try {
    const respuesta = await fetch("/api/ruta?" + new URLSearchParams({ origen, destino, perfil }));
    const datos = await respuesta.json();
    if (!respuesta.ok) {
      mostrarError(datos.mensaje || "no se pudo calcular la ruta.");
      return;
    }
    mostrarRuta(datos);
  } catch (e) {
    mostrarError("no se pudo conectar con el servidor. Revisa que esté encendido.");
  }
}

cargarRuta();