let categoriaEditandoId = null;

document.addEventListener('DOMContentLoaded', async () => {
    if (!requerirSesion('BIBLIOTECARIO')) return;
    pintarUsuario();
    document.getElementById('formCategoria').addEventListener('submit', guardarCategoria);
    document.getElementById('btnCancelar').addEventListener('click', limpiarFormulario);
    await cargarCategorias();
});

async function cargarCategorias() {
    try {
        const datos = await sendRequest('/categorias');
        document.getElementById('tablaCategorias').innerHTML = datos.map(c => `
            <tr>
                <td>${c.id}</td><td>${escapeHtml(c.nombre)}</td><td>${escapeHtml(c.descripcion || '')}</td>
                <td class="acciones">
                    <button class="boton claro" onclick='editarCategoria(${JSON.stringify(JSON.stringify(c))})'>Editar</button>
                    <button class="boton peligro" onclick="eliminarCategoria(${c.id})">Eliminar</button>
                </td>
            </tr>`).join('');
    } catch (e) { mostrarAlerta('alerta', textoError(e), 'error'); }
}

function editarCategoria(serializada) {
    const c = JSON.parse(serializada);
    categoriaEditandoId = c.id;
    document.getElementById('nombre').value = c.nombre;
    document.getElementById('descripcion').value = c.descripcion || '';
    document.getElementById('tituloForm').textContent = 'Editar categoría';
}

async function guardarCategoria(event) {
    event.preventDefault();
    const data = { nombre: document.getElementById('nombre').value.trim(), descripcion: document.getElementById('descripcion').value.trim() };
    try {
        if (categoriaEditandoId) await sendRequest('/categorias/' + categoriaEditandoId, 'PUT', data);
        else await sendRequest('/categorias', 'POST', data);
        mostrarAlerta('alerta', categoriaEditandoId ? 'Categoría actualizada.' : 'Categoría creada.');
        limpiarFormulario();
        await cargarCategorias();
    } catch (e) { mostrarAlerta('alerta', textoError(e), 'error'); }
}

async function eliminarCategoria(id) {
    if (!confirm('¿Eliminar esta categoría? Solo será posible si no tiene libros asociados.')) return;
    try {
        await sendRequest('/categorias/' + id, 'DELETE');
        mostrarAlerta('alerta', 'Categoría eliminada.');
        await cargarCategorias();
    } catch (e) { mostrarAlerta('alerta', textoError(e), 'error'); }
}

function limpiarFormulario() {
    categoriaEditandoId = null;
    document.getElementById('formCategoria').reset();
    document.getElementById('tituloForm').textContent = 'Nueva categoría';
}

function escapeHtml(v) { return String(v ?? '').replace(/[&<>'"]/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;',"'":'&#39;','"':'&quot;'}[c])); }
