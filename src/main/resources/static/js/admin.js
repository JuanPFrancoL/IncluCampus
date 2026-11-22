const aviso = document.getElementById("aviso-acceso");
const panel = document.getElementById("panel");

function esAdministrador() {
  return sessionStorage.getItem("rol") === "ADMIN";
}

if (esAdministrador()) {
  document.getElementById("nombre-admin").textContent =
    sessionStorage.getItem("nombre") || "administrador";
  panel.hidden = false;
} else {
  aviso.hidden = false;
}

document.getElementById("btn-salir").addEventListener("click", function () {
  sessionStorage.removeItem("rol");
  sessionStorage.removeItem("nombre");
  window.location.href = "login.html";
});