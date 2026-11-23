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
    let pResumen = document.getElementById("resumen-campus");
    if (!pResumen) {
      pResumen = document.createElement("p");
      pResumen.id = "resumen-campus";
      panel.insertBefore(pResumen, panel.querySelector("ul"));
    }
    pResumen.innerHTML =
      "<strong>Estado del campus:</strong> " +
      datos.totalLugares + " lugares · " +
      datos.totalTramos + " caminos · " +
      datos.bloqueosActivos + " bloqueo(s) activo(s).";
  } catch (e) { }
}

async function levantarBloqueo(id) {
  try {
    const resp = await fetch("/api/bloqueos/" + encodeURIComponent(id) + "/levantar", {
      method: "PATCH",
      headers: { "X-Rol": sessionStorage.getItem("rol") || "" }
    });
    if (resp.ok) {
      await cargarResumen();
      await cargarBloqueosAdmin();
    }
  } catch (e) {
    alert("Error al intentar levantar el bloqueo.");
  }
}

async function cargarBloqueosAdmin() {
  let contenedor = document.getElementById("admin-bloqueos");
  if (!contenedor) {
    contenedor = document.createElement("section");
    contenedor.id = "admin-bloqueos";
    panel.appendChild(contenedor);
  }

  try {
    const resp = await fetch("/api/bloqueos");
    if (!resp.ok) throw new Error();
    const bloqueos = await resp.json();

    contenedor.innerHTML = "<h2>Gestión de bloqueos reportados</h2>";

    if (bloqueos.length === 0) {
      contenedor.innerHTML += "<p>No hay bloqueos registrados actualmente.</p>";
      return;
    }

    const lista = document.createElement("ul");
    bloqueos.forEach(function (b) {
      const item = document.createElement("li");
      const camino = b.camino || b.tramoId;
      const detalle = (b.detalle || b.descripcion) ? " — " + (b.detalle || b.descripcion) : "";
      const esActivo = b.estado === "ACTIVO";

      item.innerHTML =
        "<strong>" + camino + "</strong> (" + b.motivo + ")" + detalle +
        " — <em>Estado: " + b.estado + "</em> ";

      if (esActivo) {
        const btn = document.createElement("button");
        btn.type = "button";
        btn.textContent = "Levantar bloqueo";
        btn.addEventListener("click", function () {
          levantarBloqueo(b.id);
        });
        item.appendChild(btn);
      }

      lista.appendChild(item);
    });

    contenedor.appendChild(lista);
  } catch (e) {
    contenedor.innerHTML = "<h2>Gestión de bloqueos reportados</h2><p>No se pudieron cargar los bloqueos.</p>";
  }
}

if (esAdministrador()) {
  document.getElementById("nombre-admin").textContent =
    sessionStorage.getItem("nombre") || "administrador";
  panel.hidden = false;
  cargarResumen();
  cargarBloqueosAdmin();
} else {
  aviso.hidden = false;
}

document.getElementById("btn-salir").addEventListener("click", function () {
  sessionStorage.removeItem("rol");
  sessionStorage.removeItem("nombre");
  window.location.href = "login.html";
});