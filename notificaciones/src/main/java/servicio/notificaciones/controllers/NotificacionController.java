package servicio.notificaciones.controllers;


import servicio.notificaciones.models.Notificacion;
import servicio.notificaciones.services.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @PostMapping("/enviar")
    public ResponseEntity<Notificacion> enviarNotificacion(
            @RequestBody Map<String, String> payload) {
        String numeroTelefono = payload.get("numeroTelefono");
        String mensaje = payload.get("mensaje");
        Notificacion notificacion = notificacionService.enviarNotificacion(numeroTelefono, mensaje);
        return ResponseEntity.ok(notificacion);
    }


    @GetMapping("/status")
    public ResponseEntity <String> getStatus() {
        return ResponseEntity.ok("OK");
    }

    // endpoint para listar todas las notificaciones historicas
    @GetMapping("/historico")
    public ResponseEntity <Iterable<Notificacion>> getHistorico() {
        Iterable<Notificacion> notificaciones = notificacionService.getHistorico();
        return ResponseEntity.ok(notificaciones);
    }
}


