package servicio.gestionDePruebas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import servicio.gestionDePruebas.models.Empleado;
import servicio.gestionDePruebas.repositories.EmpleadoRepository;

import java.util.Optional;

@RestController
@RequestMapping("api/empleados")
public class EmpleadosController {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarEmpleado(@RequestBody Empleado empleado) {
        // verificar que el empleado no exista ya
        Optional<Empleado> existente = empleadoRepository.findById(empleado.getIdEmpleado());
        if (existente.isPresent()) {
            return ResponseEntity.badRequest().body("Ya existe un empleado con el ID " + empleado.getIdEmpleado());
        }

        Empleado nuevoEmpleado = empleadoRepository.save(empleado);
        return ResponseEntity.ok(nuevoEmpleado);
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarEmpleado(@RequestParam Integer id) {
        Optional<Empleado> empleado = empleadoRepository.findById(id);
        return empleado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // buscar todos los empleados
    @GetMapping("/listar-empleados")
    public ResponseEntity<?> listarEmpleados() {
        return ResponseEntity.ok(empleadoRepository.findAll());
    }
}
