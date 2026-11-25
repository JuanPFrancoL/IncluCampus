const listaBloqueos = document.getElementById("lista-bloqueos");
const mensajeBloqueos = document.getElementById("mensaje-bloqueos");

function mostrarMensaje(texto, esError) {
  mensajeBloqueos.textContent = texto;
  mensajeBloqueos.classList.toggle("mensaje--error", esError);
}

async function resolverBloqueo(id) {
  try {
    const resp = await fetch("/api/bloqueos/" + encodeURIComponent(id) + "/levantar", {
      method: "PATCH"
    });

    const datos = await resp.json().catch(function () { return {}; });
    if (!resp.ok) {
      mostrarMensaje("Error: " + (datos.mensaje || "No se pudo resolver el bloqueo."), true);
      return;
    }

    mostrarMensaje(datos.mensaje || "Bloqueo resuelto.", false);
    cargarBloqueos();
  } catch (error) {
    mostrarMensaje("Error de conexión con el servidor.", true);
  }
}

// Agrega una línea "Etiqueta: valor" usando textContent (nada de innerHTML)
function agregarLinea(padre, etiqueta, valor) {
  const negrita = document.createElement("strong");
  negrita.textContent = etiqueta + ": ";
  padre.appendChild(negrita);
  padre.appendChild(document.createTextNode(valor));
  padre.appendChild(document.createElement("br"));
}

async function cargarBloqueos() {
  listaBloqueos.replaceChildren();
  try {
    const [respBloqueos, admin] = await Promise.all([
      fetch("/api/bloqueos"),
      esAdministrador()
    ]);

    if (!respBloqueos.ok) {
      throw new Error("No se pudieron consultar los bloqueos.");
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

      const nombreCamino = nombrePunto(b.camino || b.tramoId);
      const detalle = b.detalle || b.descripcion;
      const info = document.createElement("p");
      agregarLinea(info, "Camino", nombreCamino);
      agregarLinea(info, "Motivo", b.motivo);
      if (detalle) {
        agregarLinea(info, "Detalle", detalle);
      }
      li.appendChild(info);

      // El botón solo se muestra al administrador. Aunque alguien lo fuerce desde la consola,
      // el servidor responde 403 si no hay sesión de administrador.
      if (admin) {
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
    mostrarMensaje("Error: no se pudieron cargar los bloqueos. Revisa que el servidor esté encendido.", true);
  }
}

cargarBloqueos();
