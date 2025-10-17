package com.prueba.usuarios.controllador;

import com.prueba.usuarios.entidades.InvitacionVirtual;
import com.prueba.usuarios.entidades.InvitacionPresencial;
import com.prueba.usuarios.servicios.InvitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invitacion")
public class InvitacionController {

    @Autowired
    private InvitacionService invitacionService;

    @PostMapping("/virtual")
    public ResponseEntity<?> enviarInvitacionVirtual(@RequestBody InvitacionVirtual invitacion) {
        try {
            invitacionService.enviarInvitacionesVirtuales(invitacion);
            return ResponseEntity.ok("Invitaciones virtuales enviadas exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al enviar invitaciones: " + e.getMessage());
        }
    }

    @PostMapping("/presencial")
    public ResponseEntity<?> enviarInvitacionPresencial(@RequestBody InvitacionPresencial invitacion) {
        try {
            invitacionService.enviarInvitacionesPresenciales(invitacion);
            return ResponseEntity.ok("Invitaciones presenciales enviadas exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al enviar invitaciones: " + e.getMessage());
        }
    }
}
