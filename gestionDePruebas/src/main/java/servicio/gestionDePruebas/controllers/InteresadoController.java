package servicio.gestionDePruebas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import servicio.gestionDePruebas.models.Interesado;
import servicio.gestionDePruebas.repositories.InteresadoRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/interesados")
public class InteresadoController {
    
    @Autowired
    private InteresadoRepository interesadoRepository;
    
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarInteresado(@RequestBody Interesado interesado) {
        // Validar si ya existe un interesado con el mismo documento
        Optional<Interesado> existente = interesadoRepository
            .findByTipoDocumentoAndDocumento(
                interesado.getTipoDocumento(), 
                interesado.getDocumento()
            );
        
        if (existente.isPresent()) {
            return ResponseEntity.badRequest()
                .body("Ya existe un interesado con el documento " + 
                    interesado.getTipoDocumento() + " " + 
                    interesado.getDocumento());
        }
        
        // Por defecto, un nuevo interesado no está restringido
        if (interesado.getRestringido() == null) {
            interesado.setRestringido(false);
        }
        
        Interesado nuevoInteresado = interesadoRepository.save(interesado);
        return ResponseEntity.ok(nuevoInteresado);
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarInteresado(
            @RequestParam String tipoDocumento,
            @RequestParam String documento) {
        
        Optional<Interesado> interesado = interesadoRepository
            .findByTipoDocumentoAndDocumento(tipoDocumento, documento);
        
        return interesado
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}