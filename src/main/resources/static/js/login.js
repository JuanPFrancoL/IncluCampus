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
  const perfil = formUsuario.elements["perfil"].value;

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

  try {
    sessionStorage.setItem("rol", "USUARIO");
    sessionStorage.setItem("nombre", nombre);
    sessionStorage.setItem("perfil", perfil);
  } catch (e) { }

  window.location.href = "index.html";
});

// ----- Administrador: usuario + contraseña -----
formAdmin.addEventListener("submit", async function (evento) {
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

  try {
    const resp = await fetch("/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        usuario: campoUsuario.value.trim(),
        clave: campoClave.value
      })
    });

    const datos = await resp.json();
    if (!resp.ok) {
      mostrarMensaje(mensajeAdmin, "Error: " + (datos.mensaje || "Credenciales inválidas."), true);
      return;
    }

    sessionStorage.setItem("rol", datos.rol);
    sessionStorage.setItem("nombre", datos.nombre);
    window.location.href = "admin.html";
  } catch (error) {
    mostrarMensaje(mensajeAdmin, "Error de conexión con el servidor.", true);
  }
});