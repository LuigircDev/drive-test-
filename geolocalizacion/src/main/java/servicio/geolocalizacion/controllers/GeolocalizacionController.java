package servicio.geolocalizacion.controllers;


import org.springframework.web.client.RestTemplate;
import servicio.geolocalizacion.config.Configuracion;
import servicio.geolocalizacion.models.Posicion;
import servicio.geolocalizacion.repositorios.PosicionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import servicio.geolocalizacion.services.ConfiguracionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posiciones")
public class GeolocalizacionController {

    @Autowired
    private ConfiguracionService configuracionService;

    @Autowired
    private PosicionRepository posicionRepository;

    @Autowired
    private RestTemplate restTemplate;

    private boolean estaFueraDelRadio(Double latitud, Double longitud, Double centroLatitud, Double centroLongitud, Double radio) {
        double distancia = Math.sqrt(Math.pow(latitud - centroLatitud, 2) + Math.pow(longitud - centroLongitud, 2));
        return distancia > radio;
    }


    private boolean estaEnZonaPeligrosa(Posicion posicion, List<Configuracion.ZonaRestringida> zonasRestringidas) {
        return zonasRestringidas.stream()
                .anyMatch(zona -> {
                    // Calcular los límites reales de la zona
                    double latMin = Math.min(zona.getNoroeste().getLat(), zona.getSureste().getLat());
                    double latMax = Math.max(zona.getNoroeste().getLat(), zona.getSureste().getLat());
                    double lonMin = Math.min(zona.getNoroeste().getLon(), zona.getSureste().getLon());
                    double lonMax = Math.max(zona.getNoroeste().getLon(), zona.getSureste().getLon());

                    // Verificar si la posición está dentro de los límites calculados
                    return posicion.getLatitud() >= latMin && posicion.getLatitud() <= latMax &&
                            posicion.getLongitud() >= lonMin && posicion.getLongitud() <= lonMax;
                });
    }




    @PostMapping("/registrar")
    public ResponseEntity<?> registrarPosicion(@RequestBody @Valid Posicion posicion) {
        // Obtener la configuración externa
        Configuracion configuracion = configuracionService.getConfiguracion();

        // Calcular si se está dentro del radio admitido
        Double centroLatitud = configuracion.getCoordenadasAgencia().getLat();
        Double centroLongitud = configuracion.getCoordenadasAgencia().getLon();
        Double radioPermitido = configuracion.getRadioAdmitidoKm();
        boolean fueraDelRadio = estaFueraDelRadio(posicion.getLatitud(), posicion.getLongitud(), centroLatitud, centroLongitud, radioPermitido);

        // Calcular si se está en una zona restringida
        List<Configuracion.ZonaRestringida> zonasRestringidas = configuracion.getZonasRestringidas();
        boolean enZonaPeligrosa = zonasRestringidas.stream().anyMatch(zona ->
                estaEnZonaPeligrosa(posicion, zonasRestringidas)
        );

        // Si hay un incidente, notificar
        if (enZonaPeligrosa) {
            String mensaje = "aviso_ingreso_zona_peligrosa";
            reportarIncidente(posicion.getIdPrueba(), mensaje);
        }

        if (fueraDelRadio) {
            String mensaje = "aviso_radio_limite_excedido";
            reportarIncidente(posicion.getIdPrueba(), mensaje);
        }

        // Mensajes predeterminados posibles:
        // aviso_ingreso_zona_peligrosa
        // aviso_radio_limite_excedido
        // check_service
        posicionRepository.save(posicion);

        return ResponseEntity.ok(posicion);
    }


    private void reportarIncidente(Integer idPrueba, String mensaje) {
        // Reportar al servicio de gestion de pruebas el incidente
        String urlGestionPruebas = "http://localhost:8080/api/pruebas/reportar-incidente";
        Map<String, Object> payload = new HashMap<>();
        payload.put("idPrueba", idPrueba);
        payload.put("mensaje", mensaje);

        restTemplate.postForEntity(urlGestionPruebas, payload, String.class);
    }

    @GetMapping("/listar-posiciones")
    public ResponseEntity<List<Posicion>> listarPosiciones() {
        List<Posicion> posiciones = posicionRepository.findAll();
        return ResponseEntity.ok(posiciones);
    }

    @GetMapping("/zonas-restringidas")
    public ResponseEntity<List<Configuracion.ZonaRestringida>> listarZonasRestringidas() {
        Configuracion configuracion = configuracionService.getConfiguracion();
        List<Configuracion.ZonaRestringida> zonasRestringidas = configuracion.getZonasRestringidas();
        return ResponseEntity.ok(zonasRestringidas);
    }




}
