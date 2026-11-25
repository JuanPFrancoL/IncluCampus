const formulario = document.getElementById("form-reportar") || document.querySelector("form");
const selectCamino = document.getElementById("camino") || document.getElementById("tramo") || document.querySelector("select");
const selectMotivo = document.getElementById("motivo") || document.querySelectorAll("select")[1];
const inputDetalle = document.getElementById("detalle") || document.getElementById("descripcion") || document.querySelector("textarea");
const mensaje = document.getElementById("mensaje-reportar") || document.getElementById("mensaje");

function mostrarMensaje(texto, esError) {
  if (!mensaje) {
    alert(texto);
    return;
  }
  mensaje.textContent = texto;
  mensaje.classList.toggle("mensaje--error", esError);
}

function marcarInvalido(campo, invalido) {
  if (!campo) return;
  campo.setAttribute("aria-invalid", invalido ? "true" : "false");
}

// Los puntos bloqueables salen de rutas-datos.js (PUNTOS_BLOQUEABLES)
function cargarCaminos() {
  if (!selectCamino) return;
  selectCamino.length = 0;
  selectCamino.add(new Option("-- Elige el punto bloqueado --", ""));
  Object.keys(PUNTOS_BLOQUEABLES).forEach(function (id) {
    selectCamino.add(new Option(PUNTOS_BLOQUEABLES[id], id));
  });
}

if (formulario) {
  formulario.addEventListener("submit", async function (evento) {
    evento.preventDefault();
    marcarInvalido(selectCamino, false);
    marcarInvalido(selectMotivo, false);

    if (!selectCamino || selectCamino.value === "") {
      marcarInvalido(selectCamino, true);
      mostrarMensaje("Error: elige el punto bloqueado.", true);
      if (selectCamino) selectCamino.focus();
      return;
    }

    if (!selectMotivo || selectMotivo.value === "") {
      marcarInvalido(selectMotivo, true);
      mostrarMensaje("Error: elige el motivo del bloqueo.", true);
      if (selectMotivo) selectMotivo.focus();
      return;
    }

    try {
      const resp = await fetch("/api/bloqueos", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          camino: selectCamino.value,
          tramoId: selectCamino.value,
          motivo: selectMotivo.value,
          detalle: inputDetalle ? inputDetalle.value.trim() : ""
        })
      });

      const datos = await resp.json();
      if (!resp.ok) {
        mostrarMensaje("Error: " + (datos.mensaje || "No se pudo registrar el bloqueo."), true);
        return;
      }

      mostrarMensaje("Bloqueo reportado correctamente.", false);
      formulario.reset();
    } catch (error) {
      mostrarMensaje("Error de conexión con el servidor.", true);
    }
  });
}

cargarCaminos();
