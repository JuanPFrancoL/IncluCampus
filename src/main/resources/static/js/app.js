// Lugares del campus.
// Por ahora es una lista fija. Cuando el servidor esté listo, vendrá de Java.
const LUGARES = ["Biblioteca", "Bloque F", "Bloque 16", "Cafetería", "Tienda UAM"];

const selectOrigen = document.getElementById("origen");
const selectDestino = document.getElementById("destino");
const formulario = document.getElementById("form-ruta");
const mensaje = document.getElementById("mensaje");

function llenarSelect(select) {
  select.add(new Option("Elige un lugar", ""));
  LUGARES.forEach(function (lugar) {
    select.add(new Option(lugar, lugar));
  });
}

function mostrarMensaje(texto, esError) {
  mensaje.textContent = texto;
  mensaje.classList.toggle("mensaje--error", esError);
}

llenarSelect(selectOrigen);
llenarSelect(selectDestino);

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
  mostrarMensaje("Buscando ruta de " + origen + " a " + destino + " (perfil: " + perfil + ")...", false);
});