package com.prueba.usuarios.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.prueba.usuarios.entidades.InvitacionPresencial;
import com.prueba.usuarios.entidades.Usuario;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRUtil {

    /**
     * Genera un código QR único para cada usuario con su ID y datos del evento
     * @param usuario Usuario al que se le genera el QR
     * @param invitacion Datos de la invitación presencial
     * @return Ruta del archivo QR generado
     */
    public static String generateQRCodeForUser(Usuario usuario, InvitacionPresencial invitacion) {
        try {
            // Contenido del QR: ID del usuario + email + asunto del evento
            String qrContent = String.format("ASISTENCIA|ID:%d|EMAIL:%s|EVENTO:%s",
                usuario.getId(),
                usuario.getEmail(),
                invitacion.getAsunto());

            String filePath = "qr_" + usuario.getId() + "_" + System.currentTimeMillis() + ".png";

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300);

            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            return filePath;
        } catch (WriterException | IOException e) {
            System.err.println("Error generando QR para usuario " + usuario.getId() + ": " + e.getMessage());
            return null;
        }
    }
}

