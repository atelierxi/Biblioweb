document.addEventListener('DOMContentLoaded', async () => {
    if (!requerirSesion()) return;
    pintarUsuario();
    document.getElementById('formBuscar').addEventListener('submit', e => { e.preventDefault(); cargarCatalogo(); });
    await cargarCatalogo();
});

async function cargarCatalogo() {
    try {
        const q = document.getElementById('q').value.trim();
        const datos = await sendRequest('/libros/buscar?q=' + encodeURIComponent(q));
        document.getElementById('tablaCatalogo').innerHTML = datos.map(l => `
            <tr><td>${escapeHtml(l.titulo)}</td><td>${escapeHtml(l.autor)}</td><td>${escapeHtml(l.categoria?.nombre || '')}</td>
            <td>${escapeHtml(l.isbn)}</td><td><span class="estado ${l.estado}">${l.estado}</span></td></tr>`).join('');
    } catch (e) { mostrarAlerta('alerta', textoError(e), 'error'); }
}

function escapeHtml(v) { return String(v ?? '').replace(/[&<>'"]/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;',"'":'&#39;','"':'&quot;'}[c])); }
