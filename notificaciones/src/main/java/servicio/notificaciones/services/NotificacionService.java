package servicio.notificaciones.services;


import servicio.notificaciones.models.Notificacion;
import servicio.notificaciones.repositories.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${whatsapp.api.url}")
    private String whatsappApiUrl;

    @Value("${whatsapp.api.token}")
    private String whatsappApiToken;

    public Notificacion enviarNotificacion(String numeroTelefono, String mensaje) {
        Notificacion notificacion = new Notificacion();
        notificacion.setNumeroTelefono(numeroTelefono);
        notificacion.setMensaje(mensaje);
        notificacion.setFechaEnvio(LocalDateTime.now());

        try {
            // Crear el payload para la API de WhatsApp
            // Mensaje es el nombre de un mensaje predefinido en el servicio de whatsapp
            Map<String, Object> payload = new HashMap<>();
            payload.put("messaging_product", "whatsapp");
            payload.put("to", numeroTelefono);

            Map<String, Object> template = new HashMap<>();
            template.put("name", mensaje);

            Map<String, String> language = new HashMap<>();
            language.put("code", "es_AR");
            template.put("language", language);

            payload.put("type", "template");
            payload.put("template", template);

            // Configurar encabezados de la solicitud
            var headers = new org.springframework.http.HttpHeaders();
            headers.setBearerAuth(whatsappApiToken);
            headers.set("Content-Type", "application/json");

            // Enviar la solicitud a la API de WhatsApp
            var request = new org.springframework.http.HttpEntity<>(payload, headers);
            restTemplate.postForEntity(whatsappApiUrl, request, String.class);

            notificacion.setEstado("ENVIADO");
        } catch (HttpClientErrorException e) {
            notificacion.setEstado("FALLIDO");
            System.err.println("Error al enviar mensaje: " + e.getMessage());
        }

        return notificacionRepository.save(notificacion);
    }

    public Iterable<Notificacion> getHistorico() {
        return notificacionRepository.findAll();
    }
}


