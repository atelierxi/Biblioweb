let libroEditandoId = null;
let categorias = [];

document.addEventListener('DOMContentLoaded', async () => {
    if (!requerirSesion('BIBLIOTECARIO')) return;
    pintarUsuario();
    document.getElementById('formLibro').addEventListener('submit', guardarLibro);
    document.getElementById('btnCancelar').addEventListener('click', limpiarFormulario);
    await cargarCategorias();
    await cargarLibros();
});

async function cargarCategorias() {
    categorias = await sendRequest('/categorias');
    document.getElementById('categoria').innerHTML = '<option value="">Seleccione...</option>' +
        categorias.map(c => `<option value="${c.id}">${escapeHtml(c.nombre)}</option>`).join('');
}

async function cargarLibros() {
    try {
        const datos = await sendRequest('/libros');
        document.getElementById('tablaLibros').innerHTML = datos.map(l => `
            <tr>
              <td>${l.id}</td><td>${escapeHtml(l.titulo)}</td><td>${escapeHtml(l.autor)}</td>
              <td>${escapeHtml(l.isbn)}</td><td>${l.anioPublicacion ?? ''}</td><td>${escapeHtml(l.categoria?.nombre || '')}</td>
              <td><span class="estado ${l.estado}">${l.estado}</span></td>
              <td class="acciones">
                <button class="boton claro" onclick='editarLibro(${JSON.stringify(JSON.stringify(l))})'>Editar</button>
                <button class="boton peligro" onclick="retirarLibro(${l.id})">Retirar</button>
              </td>
            </tr>`).join('');
    } catch (e) { mostrarAlerta('alerta', textoError(e), 'error'); }
}

function editarLibro(serializado) {
    const l = JSON.parse(serializado);
    libroEditandoId = l.id;
    document.getElementById('titulo').value = l.titulo;
    document.getElementById('autor').value = l.autor;
    document.getElementById('isbn').value = l.isbn;
    document.getElementById('anio').value = l.anioPublicacion || '';
    document.getElementById('categoria').value = l.categoria?.id || '';
    document.getElementById('estado').value = l.estado;
    document.getElementById('tituloForm').textContent = 'Editar libro';
    window.scrollTo({top:0, behavior:'smooth'});
}

async function guardarLibro(event) {
    event.preventDefault();
    const data = {
        titulo: document.getElementById('titulo').value.trim(),
        autor: document.getElementById('autor').value.trim(),
        isbn: document.getElementById('isbn').value.trim(),
        anioPublicacion: document.getElementById('anio').value ? Number(document.getElementById('anio').value) : null,
        estado: document.getElementById('estado').value,
        categoria: { id: Number(document.getElementById('categoria').value) }
    };
    try {
        if (libroEditandoId) await sendRequest('/libros/' + libroEditandoId, 'PUT', data);
        else await sendRequest('/libros', 'POST', data);
        mostrarAlerta('alerta', libroEditandoId ? 'Libro actualizado.' : 'Libro registrado.');
        limpiarFormulario();
        await cargarLibros();
    } catch (e) { mostrarAlerta('alerta', textoError(e), 'error'); }
}

async function retirarLibro(id) {
    if (!confirm('¿Retirar este libro del catálogo? Quedará INACTIVO.')) return;
    try {
        await sendRequest('/libros/' + id, 'DELETE');
        mostrarAlerta('alerta', 'Libro retirado del catálogo.');
        await cargarLibros();
    } catch (e) { mostrarAlerta('alerta', textoError(e), 'error'); }
}

function limpiarFormulario() {
    libroEditandoId = null;
    document.getElementById('formLibro').reset();
    document.getElementById('estado').value = 'DISPONIBLE';
    document.getElementById('tituloForm').textContent = 'Nuevo libro';
}

function escapeHtml(v) { return String(v ?? '').replace(/[&<>'"]/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;',"'":'&#39;','"':'&quot;'}[c])); }
