function requerirSesion(rol = null) {
    const sesion = obtenerSesion();
    if (!sesion) {
        window.location.href = 'index.html';
        return null;
    }
    if (rol && sesion.rol !== rol) {
        window.location.href = 'panel.html';
        return null;
    }
    return sesion;
}

function pintarUsuario() {
    const sesion = obtenerSesion();
    if (!sesion) return;
    document.querySelectorAll('[data-nombre-usuario]').forEach(el => el.textContent = sesion.nombre);
    document.querySelectorAll('[data-rol-usuario]').forEach(el => el.textContent = sesion.rol);
    document.querySelectorAll('[data-solo-bibliotecario]').forEach(el => {
        if (sesion.rol !== 'BIBLIOTECARIO') el.classList.add('oculto');
    });
    document.querySelectorAll('[data-solo-lector]').forEach(el => {
        if (sesion.rol !== 'LECTOR') el.classList.add('oculto');
    });
}

async function cerrarSesion() {
    try { await sendRequest('/auth/logout', 'POST'); } catch (_) {}
    limpiarSesion();
    window.location.href = 'index.html';
}
