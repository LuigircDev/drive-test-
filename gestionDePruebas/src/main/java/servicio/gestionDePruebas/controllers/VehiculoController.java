package servicio.gestionDePruebas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import servicio.gestionDePruebas.models.Vehiculo;
import servicio.gestionDePruebas.repositories.VehiculoRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {
    
    @Autowired
    private VehiculoRepository vehiculoRepository;
    
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarVehiculo(@RequestBody Vehiculo vehiculo) {
        // Validar si ya existe un vehículo con la misma patente
        Optional<Vehiculo> existente = vehiculoRepository
            .findByPatente(vehiculo.getPatente());
        
        if (existente.isPresent()) {
            return ResponseEntity.badRequest()
                .body("Ya existe un vehículo con la patente " + vehiculo.getPatente());
        }
        
        Vehiculo nuevoVehiculo = vehiculoRepository.save(vehiculo);
        return ResponseEntity.ok(nuevoVehiculo);
    }
    
    @GetMapping("/buscar/{patente}")
    public ResponseEntity<?> buscarVehiculo(@PathVariable String patente) {
        Optional<Vehiculo> vehiculo = vehiculoRepository.findByPatente(patente);
        
        return vehiculo
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}