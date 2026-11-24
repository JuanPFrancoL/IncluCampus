// Pregunta al servidor quién está conectado. El servidor es quien decide, no el navegador.
// Devuelve { autenticado: true/false, rol, nombre }
async function consultarSesion() {
  try {
    const resp = await fetch("/api/auth/sesion");
    if (!resp.ok) return { autenticado: false };
    return await resp.json();
  } catch (e) {
    return { autenticado: false };
  }
}

async function esAdministrador() {
  const sesion = await consultarSesion();
  return sesion.autenticado && sesion.rol === "ADMIN";
}
