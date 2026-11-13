const formUsuario = document.getElementById("form-usuario");
const formAdmin = document.getElementById("form-admin");
const mensajeUsuario = document.getElementById("mensaje-usuario");
const mensajeAdmin = document.getElementById("mensaje-admin");

function mostrarMensaje(elemento, texto, esError) {
  elemento.textContent = texto;
  elemento.classList.toggle("mensaje--error", esError);
}

// ----- Usuario: nombre + discapacidad -----
formUsuario.addEventListener("submit", function (evento) {
  evento.preventDefault();

  const campoNombre = document.getElementById("nombre");
  const nombre = campoNombre.value.trim();
  const perfil = formUsuario.elements["perfil"].value; // "" si no eligió ninguno

  if (nombre === "") {
    mostrarMensaje(mensajeUsuario, "Error: escribe tu nombre.", true);
    campoNombre.focus();
    return;
  }
  if (perfil === "") {
    mostrarMensaje(mensajeUsuario, "Error: elige una opción de discapacidad.", true);
    formUsuario.querySelector('input[name="perfil"]').focus();
    return;
  }

  // sessionStorage se borra al cerrar la pestaña.
  // Más adelante esto lo hará el servidor (Java) con la sesión.
  try {
    sessionStorage.setItem("rol", "USUARIO");
    sessionStorage.setItem("nombre", nombre);
    sessionStorage.setItem("perfil", perfil);
  } catch (e) { /* si no se puede guardar, igual seguimos */ }

  window.location.href = "index.html";
});

// ----- Administrador: usuario + contraseña -----
formAdmin.addEventListener("submit", function (evento) {
  evento.preventDefault();

  const campoUsuario = document.getElementById("admin-usuario");
  const campoClave = document.getElementById("admin-clave");

  if (campoUsuario.value.trim() === "") {
    mostrarMensaje(mensajeAdmin, "Error: escribe tu usuario.", true);
    campoUsuario.focus();
    return;
  }
  if (campoClave.value === "") {
    mostrarMensaje(mensajeAdmin, "Error: escribe tu contraseña.", true);
    campoClave.focus();
    return;
  }

  // La contraseña se valida en Java (ServicioAutenticacion), nunca aquí.
  mostrarMensaje(mensajeAdmin,
    "El acceso de administrador se activará cuando el servidor esté listo.", false);
});