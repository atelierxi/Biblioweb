document.addEventListener('DOMContentLoaded', () => {
    if (obtenerSesion()) window.location.href = 'panel.html';
    document.getElementById('formLogin').addEventListener('submit', iniciarSesion);
});

async function iniciarSesion(event) {
    event.preventDefault();
    const btn = document.getElementById('btnIngresar');
    btn.disabled = true;
    try {
        const respuesta = await sendRequest('/auth/login', 'POST', {
            correo: document.getElementById('correo').value.trim(),
            password: document.getElementById('password').value
        }, false);
        guardarSesion(respuesta);
        window.location.href = 'panel.html';
    } catch (error) {
        mostrarAlerta('alertaLogin', textoError(error), 'error');
    } finally {
        btn.disabled = false;
    }
}
