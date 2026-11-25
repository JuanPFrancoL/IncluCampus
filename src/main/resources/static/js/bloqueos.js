const listaBloqueos = document.getElementById("lista-bloqueos");
const mensajeBloqueos = document.getElementById("mensaje-bloqueos");

function esAdministrador() {
  return sessionStorage.getItem("rol") === "ADMIN";
}

function mostrarMensaje(texto, esError) {
  if (!mensajeBloqueos) return;
  mensajeBloqueos.textContent = texto;
  mensajeBloqueos.classList.toggle("mensaje--error", esError);
}

async function resolverBloqueo(id) {
  try {
    const resp = await fetch("/api/bloqueos/" + encodeURIComponent(id) + "/levantar", {
      method: "PATCH",
      headers: {
        "X-Rol": sessionStorage.getItem("rol") || ""
      }
    });

    const datos = await resp.json();
    if (!resp.ok) {
      mostrarMensaje("Error: " + (datos.mensaje || "No se pudo resolver el bloqueo."), true);
      return;
    }

    mostrarMensaje(datos.mensaje || "Bloqueo resuelto y camino habilitado.", false);
    cargarBloqueos();
  } catch (error) {
    mostrarMensaje("Error de conexión al intentar resolver el bloqueo.", true);
  }
}

async function cargarBloqueos() {
  if (!listaBloqueos) return;
  listaBloqueos.innerHTML = "";

  try {
    const respBloqueos = await fetch("/api/bloqueos", { cache: "no-store" });
    if (!respBloqueos.ok) {
      throw new Error("HTTP " + respBloqueos.status);
    }

    const todosLosBloqueos = await respBloqueos.json();
    const bloqueos = todosLosBloqueos.filter(function (b) {
      return b.estado === "ACTIVO";
    });

    if (bloqueos.length === 0) {
      mostrarMensaje("No hay bloqueos activos en este momento. Todos los caminos están habilitados.", false);
      return;
    }

    mostrarMensaje("Se encontraron " + bloqueos.length + " bloqueo(s) activo(s):", false);

    const ul = document.createElement("ul");
    bloqueos.forEach(function (b) {
      const li = document.createElement("li");
      li.style.marginBottom = "1rem";

      const nombreCamino = b.camino || b.tramoId;
      const detalle = b.detalle || b.descripcion;
      const info = document.createElement("p");
      info.innerHTML = "<strong>Camino:</strong> " + nombreCamino +
        "<br><strong>Motivo:</strong> " + b.motivo +
        (detalle ? "<br><strong>Detalle:</strong> " + detalle : "");
      li.appendChild(info);

      if (esAdministrador()) {
        const btnResolver = document.createElement("button");
        btnResolver.type = "button";
        btnResolver.className = "boton";
        btnResolver.textContent = "Resolver y habilitar camino";
        btnResolver.addEventListener("click", function () {
          resolverBloqueo(b.id);
        });
        li.appendChild(btnResolver);
      }

      ul.appendChild(li);
    });

    listaBloqueos.appendChild(ul);
  } catch (error) {
    console.error("Error cargando bloqueos:", error);
    mostrarMensaje("Error al cargar bloqueos (" + error.message + ").", true);
  }
}

cargarBloqueos();