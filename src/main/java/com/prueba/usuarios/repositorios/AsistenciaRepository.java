package com.prueba.usuarios.repositorios;

import com.prueba.usuarios.entidades.Asistencia;
import com.prueba.usuarios.entidades.Evento;
import com.prueba.usuarios.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByUsuario(Usuario usuario);
    List<Asistencia> findByEvento(Evento evento);
}