const selectCamino = document.getElementById("camino");
const selectMotivo = document.getElementById("motivo");
const formulario = document.getElementById("form-bloqueo");
const mensaje = document.getElementById("mensaje");

function mostrarMensaje(texto, esError) {
  mensaje.textContent = texto;
  mensaje.classList.toggle("mensaje--error", esError);
}

function marcarInvalido(campo, invalido) {
  if (invalido) campo.setAttribute("aria-invalid", "true");
  else campo.removeAttribute("aria-invalid");
}

async function cargarCaminos() {
  selectCamino.length = 0;
  selectCamino.add(new Option("Elige un camino", ""));
  try {
    const respuesta = await fetch("/api/tramos");
    if (!respuesta.ok) {
      throw new Error("Respuesta " + respuesta.status);
    }
    const tramos = await respuesta.json();
    tramos.forEach(function (tramo) {
      selectCamino.add(new Option(tramo.descripcion, tramo.id));
    });
  } catch (error) {
    mostrarMensaje("Error: no se pudieron cargar los caminos. Revisa que el servidor esté encendido.", true);
  }
}

cargarCaminos();

formulario.addEventListener("submit", async function (evento) {
  evento.preventDefault();
  marcarInvalido(selectCamino, false);
  marcarInvalido(selectMotivo, false);

  if (selectCamino.value === "") {
    marcarInvalido(selectCamino, true);
    mostrarMensaje("Error: elige el camino bloqueado.", true);
    selectCamino.focus();
    return;
  }
  if (selectMotivo.value === "") {
    marcarInvalido(selectMotivo, true);
    mostrarMensaje("Error: elige el motivo del bloqueo.", true);
    selectMotivo.focus();
    return;
  }

  try {
    const resp = await fetch("/api/bloqueos", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        tramoId: selectCamino.value,
        motivo: selectMotivo.value
      })
    });

    const datos = await resp.json();
    if (!resp.ok) {
      mostrarMensaje("Error: " + (datos.mensaje || "No se pudo registrar el bloqueo."), true);
      return;
    }

    mostrarMensaje("Bloqueo reportado correctamente. Las rutas se recalcularán evitando este tramo.", false);
    formulario.reset();
  } catch (error) {
    mostrarMensaje("Error de conexión con el servidor.", true);
  }
}););