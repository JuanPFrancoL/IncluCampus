// Letra grande y alto contraste.
// Recuerda la elección del usuario aunque cambie de página.

const raiz = document.documentElement;

function guardar(clave, valor) {
  try { localStorage.setItem(clave, valor); } catch (e) { /* si no se puede guardar, no pasa nada */ }
}

function leer(clave) {
  try { return localStorage.getItem(clave); } catch (e) { return null; }
}

const btnToggle = document.getElementById("btn-accesibilidad-toggle");
const menuAccesibilidad = document.getElementById("menu-accesibilidad");

if (btnToggle && menuAccesibilidad) {
  function setMenuAbierto(abierto) {
    menuAccesibilidad.hidden = !abierto;
    btnToggle.setAttribute("aria-expanded", String(abierto));
    if (abierto) {
      const primerBoton = menuAccesibilidad.querySelector("button");
      if (primerBoton) primerBoton.focus();
    }
  }

  btnToggle.addEventListener("click", function () {
    const estaAbierto = btnToggle.getAttribute("aria-expanded") === "true";
    setMenuAbierto(!estaAbierto);
  });

  document.addEventListener("click", function (evento) {
    const estaAbierto = btnToggle.getAttribute("aria-expanded") === "true";
    if (!estaAbierto) return;
    if (!evento.target.closest(".accesibilidad-flotante")) {
      menuAccesibilidad.hidden = true;
      btnToggle.setAttribute("aria-expanded", "false");
    }
  });

  document.addEventListener("keydown", function (evento) {
    if (evento.key === "Escape" && btnToggle.getAttribute("aria-expanded") === "true") {
      menuAccesibilidad.hidden = true;
      btnToggle.setAttribute("aria-expanded", "false");
      btnToggle.focus();
    }
  });
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