const aviso = document.getElementById("aviso-acceso");
const panel = document.getElementById("panel");

async function cargarResumen() {
  try {
    const resp = await fetch("/api/admin/resumen");
    if (!resp.ok) return;

    const datos = await resp.json();
    let pResumen = document.getElementById("resumen-campus");
    if (!pResumen) {
      pResumen = document.createElement("p");
      pResumen.id = "resumen-campus";
      panel.insertBefore(pResumen, panel.querySelector("ul"));
    }
    pResumen.textContent =
      "Estado del campus: " +
      datos.totalLugares + " lugares · " +
      datos.totalTramos + " caminos · " +
      datos.bloqueosActivos + " bloqueo(s) activo(s).";
  } catch (e) { }
}

async function levantarBloqueo(id) {
  try {
    const resp = await fetch("/api/bloqueos/" + encodeURIComponent(id) + "/levantar", {
      method: "PATCH"
    });
    if (resp.ok) {
      await cargarResumen();
      await cargarBloqueosAdmin();
    } else if (resp.status === 403) {
      alert("Tu sesión de administrador terminó. Inicia sesión otra vez.");
      window.location.href = "login.html";
    }
  } catch (e) {
    alert("Error al intentar levantar el bloqueo.");
  }
}

// Se usa textContent (no innerHTML): el motivo y el detalle los escribe cualquier persona,
// y así no se puede colar código en la página del administrador.
async function cargarBloqueosAdmin() {
  let contenedor = document.getElementById("admin-bloqueos");
  if (!contenedor) {
    contenedor = document.createElement("section");
    contenedor.id = "admin-bloqueos";
    panel.appendChild(contenedor);
  }
  contenedor.replaceChildren();

  const titulo = document.createElement("h2");
  titulo.textContent = "Gestión de bloqueos reportados";
  contenedor.appendChild(titulo);

  try {
    const resp = await fetch("/api/bloqueos");
    if (!resp.ok) throw new Error();
    const bloqueos = await resp.json();

    if (bloqueos.length === 0) {
      const vacio = document.createElement("p");
      vacio.textContent = "No hay bloqueos registrados actualmente.";
      contenedor.appendChild(vacio);
      return;
    }

    const lista = document.createElement("ul");
    bloqueos.forEach(function (b) {
      const item = document.createElement("li");
      const camino = b.camino || b.tramoId;
      const detalle = (b.detalle || b.descripcion) ? " — " + (b.detalle || b.descripcion) : "";

      const texto = document.createElement("span");
      texto.textContent = camino + " (" + b.motivo + ")" + detalle + " — Estado: " + b.estado + " ";
      item.appendChild(texto);

      if (b.estado === "ACTIVO") {
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
    const error = document.createElement("p");
    error.textContent = "No se pudieron cargar los bloqueos.";
    contenedor.appendChild(error);
  }
}

async function iniciar() {
  const sesion = await consultarSesion();

  if (sesion.autenticado && sesion.rol === "ADMIN") {
    document.getElementById("nombre-admin").textContent = sesion.nombre || "administrador";
    panel.hidden = false;
    cargarResumen();
    cargarBloqueosAdmin();
  } else {
    aviso.hidden = false;
  }
}

document.getElementById("btn-salir").addEventListener("click", async function () {
  try {
    await fetch("/api/auth/logout", { method: "POST" });
  } catch (e) { }
  try {
    sessionStorage.clear();
  } catch (e) { }
  window.location.href = "login.html";
});

iniciar();
