package com.prueba.usuarios.servicios;


import com.prueba.usuarios.entidades.EmailConfig;
import com.prueba.usuarios.entidades.InvitacionPresencial;
import com.prueba.usuarios.entidades.InvitacionVirtual;
import com.prueba.usuarios.entidades.Usuario;
import com.prueba.usuarios.entidades.TipoAsistencia;
import com.prueba.usuarios.repositorios.UsuarioRepository;
import com.prueba.usuarios.util.CalendarUtil;
import com.prueba.usuarios.util.QRUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class EmailService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private JavaMailSender mailSender;

    public void enviarRecordatorio(String asunto, String mensaje, //String flyerPath,
                                   String resumenEvento, String descripcionEvento,
                                   ZonedDateTime inicio, ZonedDateTime fin, String lugar) throws Exception {
        List<Usuario> usuarios = usuarioRepository.findAll();
        String icsPath = CalendarUtil.crearArchivoICS(resumenEvento, descripcionEvento, inicio, fin, lugar);
        //File flyer = new File(flyerPath);
        File icsFile = new File(icsPath);

        for (Usuario usuario : usuarios) {
            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setTo(usuario.getEmail());
                helper.setSubject(asunto);
                helper.setText(mensaje.replace("{nombre}", usuario.getNombre()), true);
//            if (flyer.exists()) {
//                helper.addAttachment(flyer.getName(), new FileSystemResource(flyer));
//            }
                if (icsFile.exists()) {
                    helper.addAttachment(icsFile.getName(), new FileSystemResource(icsFile));
                }
                mailSender.send(mimeMessage);
            } catch (Exception e) {
                // log and continue
                System.err.println("Error enviando recordatorio a " + usuario.getEmail() + ": " + e.getMessage());
            }
        }
    }

    public void sendInvitacionVirtual(InvitacionVirtual invitacion) throws Exception {
        List<Usuario> usuarios = usuarioRepository.findAll();
        // Filtrar usuarios VIRTUAL o MIXTO
//        String icsPath = CalendarUtil.crearArchivoICS(
//                invitacion.getAsunto(),
//                invitacion.getMensaje(),
//                invitacion.getInicio(),
//                invitacion.getFin(),
//                invitacion.getLugar()
//        );
        //File icsFile = new File(icsPath);

        for (Usuario usuario : usuarios) {
            if (usuario.getTipoAsistencia() == TipoAsistencia.VIRTUAL || usuario.getTipoAsistencia() == TipoAsistencia.MIXTO) {
                try {
                    MimeMessage mimeMessage = mailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                    helper.setTo(usuario.getEmail());
                    helper.setSubject(invitacion.getAsunto());

                    // Incluir el link de Google Meet en el mensaje
                    String mensajeConLink = invitacion.getMensaje().replace("{nombre}", usuario.getNombre())
                        + "\n\n🔗 Enlace de reunión: " + invitacion.getGoogleMeetLink();
                    helper.setText(mensajeConLink, true);

                    if (invitacion.getFlyerPath() != null) {
                        File flyer = new File(invitacion.getFlyerPath());
                        if (flyer.exists()) {
                            helper.addAttachment(flyer.getName(), new FileSystemResource(flyer));
                        }
                    }

//                    if (icsFile.exists()) {
//                        helper.addAttachment(icsFile.getName(), new FileSystemResource(icsFile));
//                    }

                    mailSender.send(mimeMessage);
                } catch (Exception e) {
                    System.err.println("Error enviando invitación virtual a " + usuario.getEmail() + ": " + e.getMessage());
                }
            }
        }
    }

    public void sendInvitacionPresencial(InvitacionPresencial invitacion) throws Exception {
        List<Usuario> usuarios = usuarioRepository.findAll();
        String icsPath = CalendarUtil.crearArchivoICS(
                invitacion.getAsunto(),
                invitacion.getMensaje(),
                invitacion.getInicio(),
                invitacion.getFin(),
                invitacion.getLugar()
        );
        File icsFile = new File(icsPath);

        for (Usuario usuario : usuarios) {
            if (usuario.getTipoAsistencia() == TipoAsistencia.PRESENCIAL || usuario.getTipoAsistencia() == TipoAsistencia.MIXTO) {
                try {
                    MimeMessage mimeMessage = mailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                    helper.setTo(usuario.getEmail());
                    helper.setSubject(invitacion.getAsunto());
                    helper.setText(invitacion.getMensaje().replace("{nombre}", usuario.getNombre()), true);

                    // Adjuntar flyer si existe
                    if (invitacion.getFlyerPath() != null) {
                        File flyer = new File(invitacion.getFlyerPath());
                        if (flyer.exists()) {
                            helper.addAttachment(flyer.getName(), new FileSystemResource(flyer));
                        }
                    }

                    // Generar QR único por usuario y adjuntarlo
                    String qrPath = QRUtil.generateQRCodeForUser(usuario, invitacion);
                    if (qrPath != null) {
                        File qrFile = new File(qrPath);
                        if (qrFile.exists()) {
                            helper.addAttachment(qrFile.getName(), new FileSystemResource(qrFile));
                        }
                    }

                    if (icsFile.exists()) {
                        helper.addAttachment(icsFile.getName(), new FileSystemResource(icsFile));
                    }

                    mailSender.send(mimeMessage);
                } catch (Exception e) {
                    System.err.println("Error enviando invitación presencial a " + usuario.getEmail() + ": " + e.getMessage());
                }
            }
        }
    }
}
