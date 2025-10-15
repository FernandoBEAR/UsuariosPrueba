package com.prueba.usuarios.servicios;


import com.prueba.usuarios.entidades.EmailConfig;
import com.prueba.usuarios.entidades.Usuario;
import com.prueba.usuarios.repositorios.UsuarioRepository;
import com.prueba.usuarios.util.CalendarUtil;
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

//    public void sendEmails(EmailConfig config) throws IOException {
//        // Verificar que el script de Python existe
//        String pythonScriptPath = "python_mailer/mail_service.py";
//        if (!new File(pythonScriptPath).exists()) {
//            throw new IOException("Script de Python no encontrado");
//        }
//
//        ProcessBuilder processBuilder = new ProcessBuilder(
//                "python",
//                pythonScriptPath,
//                "--subject", config.getSubject(),
//                "--message", config.getMessage(),
//                "--attachment", config.getAttachmentPath()
//        );
//
//        Process process = processBuilder.start();
//        // Manejo asíncrono del proceso
//    }

    public void enviarRecordatorio(String asunto, String mensaje, //String flyerPath,
                                   String resumenEvento, String descripcionEvento,
                                   ZonedDateTime inicio, ZonedDateTime fin, String lugar) throws Exception {
        List<Usuario> usuarios = usuarioRepository.findAll();
        String icsPath = CalendarUtil.crearArchivoICS(resumenEvento, descripcionEvento, inicio, fin, lugar);
        //File flyer = new File(flyerPath);
        File icsFile = new File(icsPath);

        for (Usuario usuario : usuarios) {
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
        }
    }
}
