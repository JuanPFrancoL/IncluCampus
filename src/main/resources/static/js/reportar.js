const formulario = document.getElementById("form-reportar") || document.querySelector("form");
const selectCamino = document.getElementById("camino") || document.getElementById("tramo") || document.querySelector("select");
const selectMotivo = document.getElementById("motivo") || document.querySelectorAll("select")[1];
const inputDetalle = document.getElementById("detalle") || document.getElementById("descripcion") || document.querySelector("textarea");
const mensaje = document.getElementById("mensaje-reportar") || document.getElementById("mensaje");

function mostrarMensaje(texto, esError) {
  if (!mensaje) return;
  mensaje.textContent = texto;
  mensaje.classList.toggle("mensaje--error", esError);
}

function marcarInvalido(campo, invalido) {
  if (!campo) return;
  campo.setAttribute("aria-invalid", invalido ? "true" : "false");
}

async function cargarCaminos() {
  if (!selectCamino) return;

  selectCamino.innerHTML = '<option value="">Cargando caminos...</option>';
  try {
    const resp = await fetch("/api/tramos");
    if (!resp.ok) throw new Error("HTTP " + resp.status);

    const tramos = await resp.json();
    selectCamino.innerHTML = '<option value="">-- Selecciona un camino --</option>';

    tramos.forEach(function (tramo) {
      const opcion = document.createElement("option");
      opcion.value = tramo.id;
      const nombreOrigen = tramo.origen && tramo.origen.nombre ? tramo.origen.nombre : "";
      const nombreDestino = tramo.destino && tramo.destino.nombre ? tramo.destino.nombre : "";
      opcion.textContent = tramo.descripcion || (nombreOrigen + " ↔ " + nombreDestino) || tramo.id;
      selectCamino.appendChild(opcion);
    });
  } catch (error) {
    console.error("Error cargando tramos:", error);
    selectCamino.innerHTML = '<option value="">Error al cargar caminos</option>';
    mostrarMensaje("Error al cargar caminos: " + error.message, true);
  }
}

if (formulario) {
  formulario.addEventListener("submit", async function (evento) {
    evento.preventDefault();
    marcarInvalido(selectCamino, false);
    marcarInvalido(selectMotivo, false);

    if (!selectCamino || selectCamino.value === "") {
      marcarInvalido(selectCamino, true);
      mostrarMensaje("Error: elige el camino bloqueado.", true);
      if (selectCamino) selectCamino.focus();
      return;
    }

    if (!selectMotivo || selectMotivo.value === "") {
      marcarInvalido(selectMotivo, true);
      mostrarMensaje("Error: elige el motivo del bloqueo.", true);
      if (selectMotivo) selectMotivo.focus();
      return;
    }

    const textoCamino = selectCamino.options[selectCamino.selectedIndex]
      ? selectCamino.options[selectCamino.selectedIndex].textContent
      : selectCamino.value;

    try {
      const resp = await fetch("/api/bloqueos", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          camino: textoCamino,
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