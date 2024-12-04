package servicio.gestionDePruebas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import servicio.gestionDePruebas.models.*;
import servicio.gestionDePruebas.repositories.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pruebas")
public class PruebaController {
    @Autowired
    private PruebaRepository pruebaRepository;

    @Autowired
    private InteresadoRepository interesadoRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    EmpleadoRepository empleadoRepository;

    @Autowired
    private RestTemplate restTemplate;

//    @PostMapping("/registrar")
//    public ResponseEntity<?> crearPrueba(@RequestBody Prueba prueba) {
//        // Buscar si el interesado ya existe
//        Optional<Interesado> interesadoExistente = interesadoRepository
//                .findByTipoDocumentoAndDocumento(
//                        prueba.getInteresado().getTipoDocumento(),
//                        prueba.getInteresado().getDocumento()
//                );
//
//        Interesado interesado;
//        if (interesadoExistente.isPresent()) {
//            interesado = interesadoExistente.get();
//        } else {
//            // Si no existe, registrar el nuevo interesado
//            interesado = prueba.getInteresado();
//            if (interesado.getRestringido() == null) {
//                interesado.setRestringido(false);
//            }
//            interesado = interesadoRepository.save(interesado);
//        }
//
//        // Validar si el cliente está restringido
//        if (Boolean.TRUE.equals(interesado.getRestringido())) {
//            return ResponseEntity.badRequest()
//                    .body("El cliente está restringido para realizar pruebas.");
//        }
//
//        // Validar si la licencia está vencida
//        if (interesado.getFechaVencimientoLicencia().isBefore(LocalDate.now())) {
//            return ResponseEntity.badRequest()
//                    .body("El cliente tiene la licencia vencida.");
//        }
//
//        // Validar si el vehículo existe
//        if (!vehiculoRepository.existsById(prueba.getVehiculo().getId())) {
//            return ResponseEntity.badRequest()
//                    .body("El vehículo especificado no existe.");
//        }
//
//        // Validar si el vehículo ya está en uso
//        boolean vehiculoEnUso = pruebaRepository.findByFechaHoraFinIsNull()
//                .stream()
//                .anyMatch(p -> p.getVehiculo().getId().equals(prueba.getVehiculo().getId()));
//
//        if (vehiculoEnUso) {
//            return ResponseEntity.badRequest()
//                    .body("El vehículo ya está siendo utilizado en otra prueba.");
//        }
//
//        // Actualizar la prueba con el interesado (sea nuevo o existente)
//        prueba.setInteresado(interesado);
//
//        // Crear la nueva prueba
//        Prueba nuevaPrueba = pruebaRepository.save(prueba);
//        return ResponseEntity.ok(nuevaPrueba);
//    }

    @PostMapping("/registrar")
    public ResponseEntity<?> crearPrueba(@RequestBody Map<String, Object> payload) {
        try {
            // Validar campos obligatorios
            if (!payload.containsKey("tipoDocumento") || !payload.containsKey("documento")
                    || !payload.containsKey("idVehiculo") || !payload.containsKey("idEmpleado")
                    || !payload.containsKey("fechaHoraInicio")) {
                return ResponseEntity.badRequest().body("Faltan campos obligatorios en el payload.");
            }

            // Extraer valores del payload
            String tipoDocumento = (String) payload.get("tipoDocumento");
            String documento = (String) payload.get("documento");
            Integer idVehiculo = (Integer) payload.get("idVehiculo");
            Integer idEmpleado = (Integer) payload.get("idEmpleado");
            LocalDateTime fechaHoraInicio = LocalDateTime.parse((String) payload.get("fechaHoraInicio"));

            // Validar y buscar interesado
            Interesado interesado = interesadoRepository.findByTipoDocumentoAndDocumento(tipoDocumento, documento)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "El interesado con documento " + tipoDocumento + " " + documento + " no existe."));

            if (Boolean.TRUE.equals(interesado.getRestringido())) {
                return ResponseEntity.badRequest().body("El cliente está restringido para realizar pruebas.");
            }
            if (interesado.getFechaVencimientoLicencia().isBefore(LocalDate.now())) {
                return ResponseEntity.badRequest().body("El cliente tiene la licencia vencida.");
            }

            // Validar y buscar vehículo
            Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "El vehículo con ID " + idVehiculo + " no existe."));

            boolean vehiculoEnUso = pruebaRepository.findByFechaHoraFinIsNull()
                    .stream()
                    .anyMatch(p -> p.getVehiculo().getId().equals(idVehiculo));
            if (vehiculoEnUso) {
                return ResponseEntity.badRequest().body("El vehículo ya está siendo utilizado en otra prueba.");
            }

            // Validar y buscar empleado
            Empleado empleado = empleadoRepository.findById(idEmpleado)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "El empleado con ID " + idEmpleado + " no existe."));

            boolean empleadoEnUso = pruebaRepository.findByFechaHoraFinIsNull()
                    .stream()
                    .anyMatch(p -> p.getEmpleado().getIdEmpleado().equals(idEmpleado));
            if (empleadoEnUso) {
                return ResponseEntity.badRequest().body("El empleado ya está asignado a otra prueba activa.");
            }

            // Crear y guardar la prueba
            Prueba prueba = new Prueba();
            prueba.setInteresado(interesado);
            prueba.setVehiculo(vehiculo);
            prueba.setEmpleado(empleado);
            prueba.setFechaHoraInicio(fechaHoraInicio);

            Prueba nuevaPrueba = pruebaRepository.save(prueba);
            return ResponseEntity.ok(nuevaPrueba);

        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al registrar la prueba: " + e.getMessage());
        }
    }



    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizarPrueba(@PathVariable Integer id, @RequestBody String comentario) {
        Prueba prueba = pruebaRepository.findById(id)
                .orElse(null);

        if (prueba == null) {
            return ResponseEntity.badRequest()
                    .body("Error: La prueba con ID " + id + " no existe.");
        }

        if (prueba.getFechaHoraFin() != null) {
            return ResponseEntity.badRequest()
                    .body("Error: La prueba con ID " + id + " ya fue finalizada.");
        }

        prueba.setFechaHoraFin(LocalDateTime.now()); // Usar fecha/hora actual
        prueba.setComentarios(comentario);
        pruebaRepository.save(prueba);

        return ResponseEntity.ok("La prueba con ID " + id + " ha sido finalizada con éxito.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPruebaPorId(@PathVariable Integer id) {
        Prueba prueba = pruebaRepository.findById(id)
                .orElse(null);

        if (prueba == null) {
            return ResponseEntity.badRequest()
                    .body("Error: No se encontró la prueba con ID " + id);
        }

        return ResponseEntity.ok(prueba);
    }

    @GetMapping("/en-curso")
    public ResponseEntity<List<Prueba>> listarPruebasEnCurso() {
        List<Prueba> pruebasEnCurso = pruebaRepository.findByFechaHoraFinIsNull();

        if (pruebasEnCurso.isEmpty()) {
            return ResponseEntity.ok(List.of()); // Retornar una lista vacía en lugar de un error
        }

        return ResponseEntity.ok(pruebasEnCurso);
    }

    @GetMapping("/en-curso/detalles")
    public ResponseEntity<?> listarPruebasEnCursoConDetalles() {
        List<Prueba> pruebasEnCurso = pruebaRepository.findByFechaHoraFinIsNull();

        if (pruebasEnCurso.isEmpty()) {
            return ResponseEntity.ok("No hay pruebas en curso actualmente.");
        }

        List<Map<String, Object>> detalles = pruebasEnCurso.stream().map(prueba -> {
            Map<String, Object> detalle = new HashMap<>();
            detalle.put("id", prueba.getIdPrueba());
            detalle.put("vehiculo", prueba.getVehiculo());
            detalle.put("cliente", prueba.getInteresado());
            detalle.put("empleado", prueba.getEmpleado());
            detalle.put("fechaInicio", prueba.getFechaHoraInicio());
            return detalle;
        }).toList();

        return ResponseEntity.ok(detalles);
    }

    @PostMapping("/reportar-incidente")
    public ResponseEntity<?> reportarIncidente(@RequestBody Map<String, Object> payload){

        Integer idPrueba = (Integer) payload.get("idPrueba");
        String mensaje = (String) payload.get("mensaje");

        Optional<Prueba> pruebaOptional = pruebaRepository.findById(idPrueba);

        if(pruebaOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("La prueba con ID " + idPrueba + " no existe.");
        }

        Prueba prueba = pruebaOptional.get();

        try {
            // notificar via whatsapp el incidente
            if(prueba.getEmpleado() != null) {
                enviarNotificacion(prueba.getEmpleado().getNumeroTelefono(), mensaje);
            }
            Incidente incidente = new Incidente(mensaje, true);
            prueba.getIncidentes().add(incidente);
//            pruebaRepository.save(prueba);
            return ResponseEntity.ok("El incidente ha sido reportado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al reportar el incidente.");
        }
    }


    private void enviarNotificacion(String numeroTelefono, String mensaje) {
        if (numeroTelefono == null) {
            // Manejar el caso en el que numeroTelefono es null
            throw new RuntimeException("El número de teléfono es requerido");
        }

        String urlNotificaciones = "http://localhost:8081/api/notificaciones/enviar";
        Map<String, Object> payload = new HashMap<>();
        payload.put("numeroTelefono", numeroTelefono);
        payload.put("mensaje", mensaje);

        restTemplate.postForEntity(urlNotificaciones, payload, String.class);
        // Mensajes predeterminados posibles:
        // aviso_ingreso_zona_peligrosa
        // aviso_radio_limite_excedido
        // check_service
    }


}

