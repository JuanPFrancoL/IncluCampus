const formUsuario = document.getElementById("form-usuario");
const formAdmin = document.getElementById("form-admin");
const mensajeUsuario = document.getElementById("mensaje-usuario");
const mensajeAdmin = document.getElementById("mensaje-admin");

function mostrarMensaje(elemento, texto, esError) {
  elemento.textContent = texto;
  elemento.classList.toggle("mensaje--error", esError);
}

function marcarInvalido(campo, invalido) {
  if (invalido) campo.setAttribute("aria-invalid", "true");
  else campo.removeAttribute("aria-invalid");
}

// ----- Usuario: nombre + discapacidad -----
formUsuario.addEventListener("submit", async function (evento) {
  evento.preventDefault();

  const campoNombre = document.getElementById("nombre");
  const nombre = campoNombre.value.trim();
  const perfil = formUsuario.elements["perfil"].value;

  marcarInvalido(campoNombre, false);

  if (nombre === "") {
    marcarInvalido(campoNombre, true);
    mostrarMensaje(mensajeUsuario, "Error: escribe tu nombre.", true);
    campoNombre.focus();
    return;
  }
  if (perfil === "") {
    mostrarMensaje(mensajeUsuario, "Error: elige una opción de discapacidad.", true);
    formUsuario.querySelector('input[name="perfil"]').focus();
    return;
  }

  // Si antes había un administrador conectado en este navegador, se cierra su sesión
  try {
    await fetch("/api/auth/logout", { method: "POST" });
  } catch (e) { /* si falla, igual seguimos */ }

  // El nombre y el perfil solo sirven para mostrar y preseleccionar. No dan ningún permiso.
  try {
    sessionStorage.setItem("rol", "USUARIO");
    sessionStorage.setItem("nombre", nombre);
    sessionStorage.setItem("perfil", perfil);
  } catch (e) { }

  window.location.href = "index.html";
});

// ----- Administrador: usuario + contraseña (se validan en Java) -----
formAdmin.addEventListener("submit", async function (evento) {
  evento.preventDefault();

  const campoUsuario = document.getElementById("admin-usuario");
  const campoClave = document.getElementById("admin-clave");

  marcarInvalido(campoUsuario, false);
  marcarInvalido(campoClave, false);

  if (campoUsuario.value.trim() === "") {
    marcarInvalido(campoUsuario, true);
    mostrarMensaje(mensajeAdmin, "Error: escribe tu usuario.", true);
    campoUsuario.focus();
    return;
  }
  if (campoClave.value === "") {
    marcarInvalido(campoClave, true);
    mostrarMensaje(mensajeAdmin, "Error: escribe tu contraseña.", true);
    campoClave.focus();
    return;
  }

  try {
    const resp = await fetch("/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        usuario: campoUsuario.value.trim(),
        clave: campoClave.value
      })
    });

    const datos = await resp.json().catch(function () { return {}; });

    if (!resp.ok) {
      const credencialesMalas = resp.status === 400 || resp.status === 401;
      const texto = credencialesMalas
        ? (datos.mensaje || "Usuario o contraseña incorrectos.")
        : "El servidor no pudo procesar el inicio de sesión.";
      if (credencialesMalas) {
        marcarInvalido(campoUsuario, true);
        marcarInvalido(campoClave, true);
        campoClave.focus();
      }
      mostrarMensaje(mensajeAdmin, "Error: " + texto, true);
      return;
    }

    // El permiso de administrador queda en la sesión del servidor (cookie), no en el navegador.
    try {
      sessionStorage.setItem("nombre", datos.nombre);
    } catch (e) { }
    window.location.href = "admin.html";
  } catch (error) {
    mostrarMensaje(mensajeAdmin, "Error de conexión con el servidor.", true);
  }
});
