document.addEventListener('DOMContentLoaded', () => {
    if (!requerirSesion()) return;
    pintarUsuario();
});
