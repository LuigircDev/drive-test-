package servicio.gestionDePruebas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import servicio.gestionDePruebas.models.Empleado;
import servicio.gestionDePruebas.models.Interesado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository  extends JpaRepository<Empleado, Integer> {
    Optional<Empleado> findById(Integer id);
    List<Empleado> findAll();
}
