package com.prueba.usuarios.controllador;

import com.prueba.usuarios.entidades.Evento;
import com.prueba.usuarios.servicios.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @PostMapping("/crear")
    public Evento crearEvento(@RequestBody Evento evento) {
        return eventoService.crearEvento(evento);
    }

    @GetMapping("/{id}")
    public Evento obtenerEvento(@PathVariable Long id) {
        return eventoService.obtenerEventoPorId(id);
    }

    @PutMapping("/{id}")
    public Evento actualizarEvento(@PathVariable Long id, @RequestBody Evento evento) {
        return eventoService.actualizarEvento(id, evento);
    }

    @DeleteMapping("/{id}")
    public void eliminarEvento(@PathVariable Long id) {
        eventoService.eliminarEvento(id);
    }
}