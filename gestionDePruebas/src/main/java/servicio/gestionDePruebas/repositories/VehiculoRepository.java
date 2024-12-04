package servicio.gestionDePruebas.repositories;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import servicio.gestionDePruebas.models.Vehiculo;

import java.util.Optional;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {

    Optional<Vehiculo> findByPatente(@NotNull String patente);
}
