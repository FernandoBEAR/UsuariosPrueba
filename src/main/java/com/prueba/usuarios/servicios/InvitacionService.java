package com.prueba.usuarios.servicios;

import com.prueba.usuarios.entidades.InvitacionVirtual;
import com.prueba.usuarios.entidades.InvitacionPresencial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvitacionService {

    @Autowired
    private EmailService emailService;

    public void enviarInvitacionesVirtuales(InvitacionVirtual invitacion) throws Exception {
        emailService.sendInvitacionVirtual(invitacion);
    }

    public void enviarInvitacionesPresenciales(InvitacionPresencial invitacion) throws Exception {
        emailService.sendInvitacionPresencial(invitacion);
    }
}
