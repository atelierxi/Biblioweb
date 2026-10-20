let usuarioEditandoId = null;

document.addEventListener('DOMContentLoaded', async () => {
    if (!requerirSesion('BIBLIOTECARIO')) return;
    pintarUsuario();
    document.getElementById('formUsuario').addEventListener('submit', guardarUsuario);
    document.getElementById('btnCancelar').addEventListener('click', limpiarFormulario);
    await cargarUsuarios();
});

async function cargarUsuarios() {
    try {
        const datos = await sendRequest('/usuarios/lectores');
        document.getElementById('tablaUsuarios').innerHTML = datos.map(u => `
            <tr>
              <td>${u.id}</td><td>${escapeHtml(u.nombres + ' ' + u.apellidos)}</td><td>${escapeHtml(u.correo)}</td>
              <td><span class="estado ${u.estado}">${u.estado}</span></td>
              <td class="acciones">
                <button class="boton claro" onclick='editarUsuario(${JSON.stringify(JSON.stringify(u))})'>Editar</button>
                <button class="boton peligro" onclick="inactivarUsuario(${u.id})">Inactivar</button>
              </td>
            </tr>`).join('');
    } catch (e) { mostrarAlerta('alerta', textoError(e), 'error'); }
}

function editarUsuario(serializado) {
    const u = JSON.parse(serializado);
    usuarioEditandoId = u.id;
    document.getElementById('nombres').value = u.nombres;
    document.getElementById('apellidos').value = u.apellidos;
    document.getElementById('correo').value = u.correo;
    document.getElementById('password').value = '';
    document.getElementById('estado').value = u.estado;
    document.getElementById('tituloForm').textContent = 'Editar lector';
}

async function guardarUsuario(event) {
    event.preventDefault();
    const data = {
        nombres: document.getElementById('nombres').value.trim(),
        apellidos: document.getElementById('apellidos').value.trim(),
        correo: document.getElementById('correo').value.trim(),
        password: document.getElementById('password').value || null,
        rol: 'LECTOR',
        estado: document.getElementById('estado').value
    };
    try {
        if (usuarioEditandoId) await sendRequest('/usuarios/' + usuarioEditandoId, 'PUT', data);
        else await sendRequest('/usuarios', 'POST', data);
        mostrarAlerta('alerta', usuarioEditandoId ? 'Lector actualizado.' : 'Lector registrado.');
        limpiarFormulario();
        await cargarUsuarios();
    } catch (e) { mostrarAlerta('alerta', textoError(e), 'error'); }
}

async function inactivarUsuario(id) {
    if (!confirm('¿Inactivar este lector? Su historial se conservará.')) return;
    try {
        await sendRequest('/usuarios/' + id, 'DELETE');
        mostrarAlerta('alerta', 'Lector inactivado.');
        await cargarUsuarios();
    } catch (e) { mostrarAlerta('alerta', textoError(e), 'error'); }
}

function limpiarFormulario() {
    usuarioEditandoId = null;
    document.getElementById('formUsuario').reset();
    document.getElementById('estado').value = 'ACTIVO';
    document.getElementById('tituloForm').textContent = 'Nuevo lector';
}

function escapeHtml(v) { return String(v ?? '').replace(/[&<>'"]/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;',"'":'&#39;','"':'&quot;'}[c])); }
