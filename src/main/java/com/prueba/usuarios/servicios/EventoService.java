package com.prueba.usuarios.servicios;

import com.prueba.usuarios.entidades.Evento;

public interface EventoService {
    Evento crearEvento(Evento evento);
    Evento obtenerEventoPorId(Long id)throws RuntimeException;
    Evento actualizarEvento(Long id, Evento evento);
    void eliminarEvento(Long id);
}