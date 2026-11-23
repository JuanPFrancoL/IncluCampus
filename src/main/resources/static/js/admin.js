const aviso = document.getElementById("aviso-acceso");
const panel = document.getElementById("panel");

function esAdministrador() {
  return sessionStorage.getItem("rol") === "ADMIN";
}

async function cargarResumen() {
  try {
    const resp = await fetch("/api/admin/resumen", {
      headers: { "X-Rol": sessionStorage.getItem("rol") || "" }
    });
    if (!resp.ok) return;

    const datos = await resp.json();
    const pResumen = document.createElement("p");
    pResumen.innerHTML =
      "<strong>Estado del campus:</strong> " +
      datos.totalLugares + " lugares · " +
      datos.totalTramos + " caminos · " +
      datos.bloqueosActivos + " bloqueo(s) activo(s).";

    panel.insertBefore(pResumen, panel.querySelector("ul"));
  } catch (e) { }
}

if (esAdministrador()) {
  document.getElementById("nombre-admin").textContent =
    sessionStorage.getItem("nombre") || "administrador";
  panel.hidden = false;
  cargarResumen();
} else {
  aviso.hidden = false;
}

document.getElementById("btn-salir").addEventListener("click", function () {
  sessionStorage.removeItem("rol");
  sessionStorage.removeItem("nombre");
  window.location.href = "login.html";
});