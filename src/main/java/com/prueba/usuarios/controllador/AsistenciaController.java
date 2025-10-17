package com.prueba.usuarios.controllador;

import com.prueba.usuarios.entidades.Asistencia;
import com.prueba.usuarios.servicios.AsistenciaService;
import com.prueba.usuarios.servicios.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/asistencias")
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;
    @Autowired
    private QrCodeService qrCodeService;

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

    //Nuevos metodos para QR:

    @PostMapping("/registrar-qr")
    public ResponseEntity<?> registrarAsistenciaPorQR(@RequestBody Map<String, String> request) {
        try {
            String codigoQr = request.get("codigoQr");
            if (codigoQr == null || codigoQr.isEmpty()) {
                return ResponseEntity.badRequest().body("El código QR es requerido");
            }

            Asistencia asistencia = asistenciaService.registrarAsistenciaPorQR(codigoQr);
            return ResponseEntity.ok(asistencia);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/qrcode")
    public ResponseEntity<byte[]> obtenerQRCode(@PathVariable Long id) {
        try {
            byte[] qrCode = asistenciaService.generarQRParaAsistencia(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            return new ResponseEntity<>(qrCode, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Decodificar codigo QR en el endpoint

    @PostMapping("/validar-qr-imagen")
    public ResponseEntity<?> validarQrDesdeImagen(@RequestParam("imagen") MultipartFile imagen) {
        try {
            // 1. Decodificar la imagen para obtener el texto del QR
            String codigoQr = qrCodeService.decodeQRCode(imagen.getBytes());

            // 2. Registrar la asistencia usando el código obtenido
            Asistencia asistencia = asistenciaService.registrarAsistenciaPorQR(codigoQr);

            return ResponseEntity.ok(asistencia);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la imagen QR: " + e.getMessage());
        }
    }
}