document.addEventListener('DOMContentLoaded', async () => {
    if (!requerirSesion('LECTOR')) return;
    pintarUsuario();
    try {
        const datos = await sendRequest('/prestamos/mis-prestamos');
        document.getElementById('tablaMisPrestamos').innerHTML = datos.map(p => `
            <tr><td>${p.id}</td><td>${escapeHtml(p.libro.titulo)}</td><td>${p.fechaPrestamo}</td><td>${p.fechaLimite}</td>
            <td>${p.fechaDevolucion || ''}</td><td><span class="estado ${p.estado}">${p.estado}</span></td></tr>`).join('');
    } catch (e) { mostrarAlerta('alerta', textoError(e), 'error'); }
});

function escapeHtml(v) { return String(v ?? '').replace(/[&<>'"]/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;',"'":'&#39;','"':'&quot;'}[c])); }
