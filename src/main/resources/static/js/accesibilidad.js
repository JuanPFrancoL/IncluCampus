// Letra grande y alto contraste.
// Recuerda la elección del usuario aunque cambie de página.

const raiz = document.documentElement;

function guardar(clave, valor) {
  try { localStorage.setItem(clave, valor); } catch (e) { /* si no se puede guardar, no pasa nada */ }
}

function leer(clave) {
  try { return localStorage.getItem(clave); } catch (e) { return null; }
}

function configurarBoton(idBoton, clase, clave) {
  const boton = document.getElementById(idBoton);
  if (!boton) return;

  function aplicar(activo) {
    raiz.classList.toggle(clase, activo);
    boton.setAttribute("aria-pressed", String(activo));
  }

  aplicar(leer(clave) === "si");

  boton.addEventListener("click", function () {
    const activo = !raiz.classList.contains(clase);
    aplicar(activo);
    guardar(clave, activo ? "si" : "no");
  });
}

configurarBoton("btn-letra", "letra-grande", "letraGrande");
configurarBoton("btn-contraste", "alto-contraste", "altoContraste");