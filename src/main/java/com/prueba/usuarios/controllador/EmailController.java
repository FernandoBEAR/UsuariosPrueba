package com.prueba.usuarios.controllador;

import com.prueba.usuarios.entidades.EmailConfig;
import com.prueba.usuarios.servicios.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.prueba.usuarios.dto.RecordatorioRequest;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

//    @PostMapping("/send")
//    public ResponseEntity<?> sendEmails(@RequestBody EmailConfig config) {
//        try {
//            emailService.sendEmails(config);
//            return ResponseEntity.ok("Proceso de envío iniciado");
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
//        }
//    }

    @PostMapping("/recordatorio")
    public ResponseEntity<?> enviarRecordatorio(@RequestBody RecordatorioRequest request) {
        try {
            emailService.enviarRecordatorio(
                request.getAsunto(),
                request.getMensaje(),
                //request.getFlyerPath(),
                request.getResumenEvento(),
                request.getDescripcionEvento(),
                request.getInicio(),
                request.getFin(),
                request.getLugar()
            );
            return ResponseEntity.ok("Recordatorios enviados correctamente");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}