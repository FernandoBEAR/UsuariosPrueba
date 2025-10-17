package com.prueba.usuarios.controllador;

import com.prueba.usuarios.entidades.Asistencia;
import com.prueba.usuarios.servicios.AsistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/asistencias")
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;

    @PostMapping("/crear")
    public Asistencia crearAsistencia(@RequestBody Asistencia asistencia) {
        return asistenciaService.crearAsistencia(asistencia);
    }

    @GetMapping("/{id}")
    public Asistencia obtenerAsistencia(@PathVariable Long id) {
        return asistenciaService.obtenerAsistenciaPorId(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Asistencia> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return asistenciaService.obtenerAsistenciasPorUsuario(usuarioId);
    }

    @GetMapping("/evento/{eventoId}")
    public List<Asistencia> obtenerPorEvento(@PathVariable Long eventoId) {
        return asistenciaService.obtenerAsistenciasPorEvento(eventoId);
    }

    @PutMapping("/{id}")
    public Asistencia actualizarAsistencia(@PathVariable Long id, @RequestBody Asistencia asistencia) {
        return asistenciaService.actualizarAsistencia(id, asistencia);
    }

    @PutMapping("/{id}/registrar")
    public Asistencia registrarAsistencia(@PathVariable Long id) {
        return asistenciaService.registrarAsistencia(id);
    }

    @DeleteMapping("/{id}")
    public void eliminarAsistencia(@PathVariable Long id) {
        asistenciaService.eliminarAsistencia(id);
    }
}