const parametros = new URLSearchParams(window.location.search);
const origen = parametros.get("origen");
const destino = parametros.get("destino");
const perfil = parametros.get("perfil") || "general";

const mensaje = document.getElementById("mensaje");
const avisoBloqueos = document.getElementById("aviso-bloqueos");
const resumen = document.getElementById("resumen");
const imagen = document.getElementById("imagen-ruta");
const NOMBRES_PERFIL = { movilidad: "Movilidad reducida", visual: "Visual", general: "General" };

function mostrarError(texto) {
  mensaje.textContent = "Error: " + texto;
  mensaje.classList.add("mensaje--error");
}

function mostrarAviso(texto) {
  avisoBloqueos.textContent = texto;
  avisoBloqueos.hidden = false;
}

// La clave es el par de lugares en orden alfabético: sirve en los dos sentidos.
function claveDe(a, b) {
  return [a, b].sort().join("|");
}

// Pide al servidor los bloqueos activos y devuelve la lista de puntos bloqueados (ids).
// Devuelve null si no se pudo consultar.
async function puntosBloqueados() {
  try {
    const resp = await fetch("/api/bloqueos");
    if (!resp.ok) return null;
    const bloqueos = await resp.json();
    const ids = [];
    bloqueos.forEach(function (b) {
      const id = b.camino || b.tramoId;
      if (b.estado === "ACTIVO" && PUNTOS_BLOQUEABLES[id] && ids.indexOf(id) === -1) {
        ids.push(id);
      }
    });
    return ids;
  } catch (e) {
    return null;
  }
}

// Qué variante de planos alternos corresponde a los bloqueos activos
function variantePara(ids) {
  if (ids.length === 0) return null;
  if (ids.length >= 2) return "ambos";
  return ids[0];
}

function nombresDe(ids) {
  return ids.map(nombrePunto).join(" y ");
}

// Decide qué plano mostrar. Devuelve { ruta, texto, mapa }.
function elegirPlano(clave, ids) {
  const normal = RUTAS[clave] ? "img/rutas/" + RUTAS[clave] : null;
  const variante = variantePara(ids);

  if (variante === null) {
    return { ruta: normal, texto: "", mapa: null };
  }

  const mapa = "img/alternas/" + variante + "/mapa.png";
  const alterna = ALTERNAS[variante][clave];
  if (alterna) {
    return {
      ruta: "img/alternas/" + variante + "/" + alterna,
      texto: "Hay un bloqueo en: " + nombresDe(ids) + ". Esta es la ruta alterna para llegar.",
      mapa: mapa,
      alterna: true
    };
  }

  // Con los dos bloqueos y sin plano combinado: se busca el alterno de uno solo
  if (variante === "ambos") {
    for (let i = 0; i < ids.length; i++) {
      const parcial = ALTERNAS[ids[i]][clave];
      if (parcial) {
        return {
          ruta: "img/alternas/" + ids[i] + "/" + parcial,
          texto: "Hay bloqueos en: " + nombresDe(ids) + ". No tenemos un plano para esta ruta con los dos bloqueos; " +
            "este plano evita solo " + nombrePunto(ids[i]) + ", revisa el otro punto antes de pasar.",
          mapa: mapa,
          alterna: true
        };
      }
    }
  }

  return {
    ruta: normal,
    texto: "Hay un bloqueo en: " + nombresDe(ids) + ", pero no afecta esta ruta.",
    mapa: mapa,
    alterna: false
  };
}

async function mostrarRuta() {
  if (!LUGARES[origen] || !LUGARES[destino]) {
    mostrarError("faltan el origen o el destino. Vuelve a buscar.");
    return;
  }

  const clave = claveDe(origen, destino);
  const ids = await puntosBloqueados();

  let plano;
  if (ids === null) {
    plano = { ruta: RUTAS[clave] ? "img/rutas/" + RUTAS[clave] : null, texto: "", mapa: null };
    mostrarAviso("No pudimos revisar si hay bloqueos. Te mostramos la ruta normal; confírmala en el lugar.");
  } else {
    plano = elegirPlano(clave, ids);
  }

  if (!plano.ruta) {
    mostrarError("todavía no tenemos el plano entre " + LUGARES[origen] + " y " + LUGARES[destino] + ".");
    return;
  }

  const nombreOrigen = LUGARES[origen];
  const nombreDestino = LUGARES[destino];
  const esAlterna = plano.alterna === true;

  if (plano.texto) {
    mostrarAviso(plano.texto);
    if (plano.mapa) {
      const enlace = document.createElement("a");
      enlace.href = plano.mapa;
      enlace.textContent = " Ver dónde está el bloqueo.";
      avisoBloqueos.appendChild(enlace);
    }
  }

  document.getElementById("recorrido").textContent = nombreOrigen + " → " + nombreDestino;
  document.getElementById("perfil").textContent = NOMBRES_PERFIL[perfil] || perfil;
  document.getElementById("descripcion").textContent = esAlterna
    ? "Ruta alterna de " + nombreOrigen + " a " + nombreDestino + ". Sigue la línea marcada en el plano."
    : "Sigue la línea marcada en el plano para ir de " + nombreOrigen + " a " + nombreDestino + ".";
  imagen.alt = (esAlterna ? "Plano de la ruta alterna" : "Plano del campus con la ruta") +
    " de " + nombreOrigen + " a " + nombreDestino;
  imagen.addEventListener("error", function () {
    mostrarError("no se pudo cargar la imagen de la ruta.");
  });
  imagen.src = plano.ruta;
  resumen.hidden = false;
  mensaje.textContent = esAlterna ? "Ruta alterna encontrada." : "Ruta encontrada.";
}

mostrarRuta();
