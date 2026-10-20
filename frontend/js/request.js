const API_BASE = 'http://localhost:8094/api';

function obtenerSesion() {
    const raw = sessionStorage.getItem('bibliowebSesion');
    return raw ? JSON.parse(raw) : null;
}

function guardarSesion(sesion) {
    sessionStorage.setItem('bibliowebSesion', JSON.stringify(sesion));
}

function limpiarSesion() {
    sessionStorage.removeItem('bibliowebSesion');
}

function sendRequest(endpoint, method = 'GET', data = null, requiereAuth = true) {
    return new Promise((resolve, reject) => {
        const request = new XMLHttpRequest();
        request.open(method, API_BASE + endpoint, true);
        request.setRequestHeader('Content-Type', 'application/json');

        if (requiereAuth) {
            const sesion = obtenerSesion();
            if (!sesion || !sesion.token) {
                reject({ status: 401, message: 'No existe una sesión activa' });
                return;
            }
            request.setRequestHeader('Authorization', 'Bearer ' + sesion.token);
        }

        request.onreadystatechange = function () {
            if (request.readyState !== 4) return;
            let body = null;
            if (request.responseText) {
                try { body = JSON.parse(request.responseText); }
                catch (_) { body = request.responseText; }
            }

            if (request.status >= 200 && request.status < 300) {
                resolve(body);
            } else {
                if (request.status === 401 && requiereAuth) {
                    limpiarSesion();
                }
                reject({
                    status: request.status,
                    data: body,
                    message: body?.mensaje || body?.message || ('Error HTTP ' + request.status)
                });
            }
        };

        request.onerror = function () {
            reject({ status: 0, message: 'No fue posible conectar con el backend. Verifique que Spring Boot esté iniciado.' });
        };

        request.send(data == null ? null : JSON.stringify(data));
    });
}

function textoError(error) {
    if (!error) return 'Ocurrió un error inesperado';
    if (error.data?.erroresValidacion) {
        return Object.values(error.data.erroresValidacion).join(' | ');
    }
    return error.message || error.data?.mensaje || 'Ocurrió un error inesperado';
}

function mostrarAlerta(id, mensaje, tipo = 'ok') {
    const div = document.getElementById(id);
    if (!div) return;
    div.className = 'alerta ' + tipo;
    div.textContent = mensaje;
}
