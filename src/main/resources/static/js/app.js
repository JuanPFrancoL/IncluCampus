const selectOrigen = document.getElementById("origen");
const selectDestino = document.getElementById("destino");
const formulario = document.getElementById("form-ruta");
const mensaje = document.getElementById("mensaje");

function mostrarMensaje(texto, esError) {
  mensaje.textContent = texto;
  mensaje.classList.toggle("mensaje--error", esError);
}

// Llena un desplegable con la lista de lugares que manda Java
function llenarSelect(select, lugares) {
  select.length = 0;
  select.add(new Option("Elige un lugar", ""));
  lugares.forEach(function (lugar) {
    select.add(new Option(lugar.nombre, lugar.id));
  });
}

// Texto del lugar elegido (por ejemplo "Bloque F")
function nombreElegido(select) {
  return select.options[select.selectedIndex].text;
}

// Pide los lugares al servidor
async function cargarLugares() {
  try {
    const respuesta = await fetch("/api/lugares");
    if (!respuesta.ok) {
      throw new Error("Respuesta " + respuesta.status);
    }
    const lugares = await respuesta.json();
    llenarSelect(selectOrigen, lugares);
    llenarSelect(selectDestino, lugares);
  } catch (error) {
    mostrarMensaje("Error: no se pudieron cargar los lugares. Revisa que el servidor esté encendido.", true);
  }
}

// Los desplegables empiezan solo con "Elige un lugar" hasta que llegue la lista
llenarSelect(selectOrigen, []);
llenarSelect(selectDestino, []);
cargarLugares();

// Si la persona inició sesión, usamos el perfil que eligió
try {
  const perfilGuardado = sessionStorage.getItem("perfil");
  if (perfilGuardado) {
    const radio = formulario.querySelector('input[name="perfil"][value="' + perfilGuardado + '"]');
    if (radio) radio.checked = true;
  }
} catch (e) { /* sin sesión guardada, queda el perfil por defecto */ }

formulario.addEventListener("submit", function (evento) {
  evento.preventDefault();

  const origen = selectOrigen.value;
  const destino = selectDestino.value;
  const perfil = formulario.elements["perfil"].value;

  if (origen === "") {
    mostrarMensaje("Error: elige dónde estás.", true);
    selectOrigen.focus();
    return;
  }
  if (destino === "") {
    mostrarMensaje("Error: elige a dónde vas.", true);
    selectDestino.focus();
    return;
  }
  if (origen === destino) {
    mostrarMensaje("Error: el origen y el destino son el mismo lugar.", true);
    selectDestino.focus();
    return;
  }

  // Más adelante aquí se pedirá la ruta al servidor y se abrirá rutas.html.
  mostrarMensaje("Buscando ruta de " + nombreElegido(selectOrigen) + " a " +
    nombreElegido(selectDestino) + " (perfil: " + perfil + ")...", false);
});