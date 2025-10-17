package com.prueba.usuarios.servicios;

import com.prueba.usuarios.entidades.Asistencia;

import java.util.List;

public interface AsistenciaService {
    Asistencia crearAsistencia(Asistencia asistencia);
    Asistencia obtenerAsistenciaPorId(Long id);
    List<Asistencia> obtenerAsistenciasPorUsuario(Long usuarioId);
    List<Asistencia> obtenerAsistenciasPorEvento(Long eventoId);
    Asistencia actualizarAsistencia(Long id, Asistencia asistencia);
    Asistencia registrarAsistencia(Long id);
    void eliminarAsistencia(Long id);
}