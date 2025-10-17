package com.prueba.usuarios.servicios.Impl;

import com.prueba.usuarios.entidades.Asistencia;
import com.prueba.usuarios.entidades.Evento;
import com.prueba.usuarios.entidades.Usuario;
import com.prueba.usuarios.repositorios.AsistenciaRepository;
import com.prueba.usuarios.repositorios.EventoRepository;
import com.prueba.usuarios.repositorios.UsuarioRepository;
import com.prueba.usuarios.servicios.AsistenciaService;
import com.prueba.usuarios.servicios.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AsistenciaServiceImpl implements AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EventoRepository eventoRepository;
    //Para QR:
    @Autowired
    private QrCodeService qrCodeService;

    @Override
    public Asistencia crearAsistencia(Asistencia asistencia) {
        // Cargar las entidades completas
        Usuario usuarioCompleto = usuarioRepository.findById(asistencia.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Evento eventoCompleto = eventoRepository.findById(asistencia.getEvento().getId())
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        // Asignar entidades completas
        asistencia.setUsuario(usuarioCompleto);
        asistencia.setEvento(eventoCompleto);

        // Generar código QR único
        asistencia.setCodigoQr(UUID.randomUUID().toString());
        asistencia.setAsistio(false);

        return asistenciaRepository.save(asistencia);
    }

    @Override
    public Asistencia obtenerAsistenciaPorId(Long id) {
        return asistenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada con id: " + id));
    }

    @Override
    public List<Asistencia> obtenerAsistenciasPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuarioId));
        return asistenciaRepository.findByUsuario(usuario);
    }

    @Override
    public List<Asistencia> obtenerAsistenciasPorEvento(Long eventoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con id: " + eventoId));
        return asistenciaRepository.findByEvento(evento);
    }

    @Override
    public Asistencia actualizarAsistencia(Long id, Asistencia asistencia) {
        asistencia.setId(id);
        return asistenciaRepository.save(asistencia);
    }

    @Override
    public Asistencia registrarAsistencia(Long id) {
        Asistencia asistencia = obtenerAsistenciaPorId(id);
        asistencia.setAsistio(true);
        asistencia.setHoraAsistencia(LocalDateTime.now());
        return asistenciaRepository.save(asistencia);
    }

    @Override
    @Transactional
    public void eliminarAsistencia(Long id) {
        // Primero obtener la asistencia para asegurar que existe
        Asistencia asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada con id: " + id));

        // Eliminar usando el objeto en lugar del ID
        asistenciaRepository.delete(asistencia);
    }

    //Nuevos metodos para QR:

    @Override
    public Asistencia registrarAsistenciaPorQR(String codigoQr) {
        Asistencia asistencia = asistenciaRepository.findByCodigoQr(codigoQr)
                .orElseThrow(() -> new RuntimeException("No se encontró asistencia con el código QR: " + codigoQr));

        if (asistencia.isAsistio()) {
            throw new IllegalStateException("La asistencia ya fue registrada previamente");
        }

        asistencia.setAsistio(true);
        asistencia.setHoraAsistencia(LocalDateTime.now());
        return asistenciaRepository.save(asistencia);
    }

    @Override
    public byte[] generarQRParaAsistencia(Long asistenciaId) throws Exception {
        Asistencia asistencia = obtenerAsistenciaPorId(asistenciaId);
        return qrCodeService.generateQRCodeImage(asistencia.getCodigoQr(), 250, 250);
    }
}