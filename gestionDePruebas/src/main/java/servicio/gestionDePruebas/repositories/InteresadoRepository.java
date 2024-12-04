package servicio.gestionDePruebas.repositories;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import servicio.gestionDePruebas.models.Interesado;

import java.util.Optional;

public interface InteresadoRepository extends JpaRepository<Interesado, Integer> {
    Optional<Interesado> findByTipoDocumentoAndDocumento(@NotNull String tipoDocumento, @NotNull String documento);
}

