document.addEventListener('DOMContentLoaded', async () => {
    if (!requerirSesion('BIBLIOTECARIO')) return;
    pintarUsuario();
    document.getElementById('formPrestamo').addEventListener('submit', registrarPrestamo);
    ponerFechasIniciales();
    await Promise.all([cargarLectores(), cargarLibrosDisponibles(), cargarPrestamos()]);
});

function ponerFechasIniciales() {
    const hoy = new Date();
    const limite = new Date();
    limite.setDate(hoy.getDate() + 14);
    document.getElementById('fechaPrestamo').value = hoy.toISOString().slice(0,10);
    document.getElementById('fechaLimite').value = limite.toISOString().slice(0,10);
}

async function cargarLectores() {
    const datos = await sendRequest('/usuarios/lectores');
    document.getElementById('usuario').innerHTML = '<option value="">Seleccione...</option>' +
        datos.filter(u => u.estado === 'ACTIVO').map(u => `<option value="${u.id}">${escapeHtml(u.nombres + ' ' + u.apellidos)}</option>`).join('');
}

async function cargarLibrosDisponibles() {
    const datos = await sendRequest('/libros/disponibles');
    document.getElementById('libro').innerHTML = '<option value="">Seleccione...</option>' +
        datos.map(l => `<option value="${l.id}">${escapeHtml(l.titulo)} - ${escapeHtml(l.autor)}</option>`).join('');
}

async function cargarPrestamos() {
    try {
        const datos = await sendRequest('/prestamos');
        document.getElementById('tablaPrestamos').innerHTML = datos.map(p => `
            <tr>
              <td>${p.id}</td><td>${escapeHtml(p.usuario.nombres + ' ' + p.usuario.apellidos)}</td>
              <td>${escapeHtml(p.libro.titulo)}</td><td>${p.fechaPrestamo}</td><td>${p.fechaLimite}</td>
              <td>${p.fechaDevolucion || ''}</td><td><span class="estado ${p.estado}">${p.estado}</span></td>
              <td>${p.estado === 'ACTIVO' ? `<button class="boton" onclick="devolver(${p.id})">Registrar devolución</button>` : ''}</td>
            </tr>`).join('');
    } catch (e) { mostrarAlerta('alerta', textoError(e), 'error'); }
}

async function registrarPrestamo(event) {
    event.preventDefault();
    const data = {
        usuario: { id: Number(document.getElementById('usuario').value) },
        libro: { id: Number(document.getElementById('libro').value) },
        fechaPrestamo: document.getElementById('fechaPrestamo').value,
        fechaLimite: document.getElementById('fechaLimite').value,
        fechaDevolucion: null,
        estado: 'ACTIVO'
    };
    try {
        await sendRequest('/prestamos', 'POST', data);
        mostrarAlerta('alerta', 'Préstamo registrado. El libro pasó a PRESTADO.');
        document.getElementById('formPrestamo').reset();
        ponerFechasIniciales();
        await Promise.all([cargarLibrosDisponibles(), cargarPrestamos()]);
    } catch (e) { mostrarAlerta('alerta', textoError(e), 'error'); }
}

async function devolver(id) {
    if (!confirm('¿Registrar la devolución de este préstamo?')) return;
    try {
        await sendRequest('/prestamos/' + id + '/devolver', 'PATCH');
        mostrarAlerta('alerta', 'Devolución registrada. El libro volvió a DISPONIBLE.');
        await Promise.all([cargarLibrosDisponibles(), cargarPrestamos()]);
    } catch (e) { mostrarAlerta('alerta', textoError(e), 'error'); }
}

function escapeHtml(v) { return String(v ?? '').replace(/[&<>'"]/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;',"'":'&#39;','"':'&quot;'}[c])); }
