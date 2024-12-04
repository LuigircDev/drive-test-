package servicio.gestionDePruebas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import servicio.gestionDePruebas.models.Prueba;

import java.util.List;

public interface PruebaRepository extends JpaRepository<Prueba, Integer> {

    List<Prueba> findByFechaHoraFinIsNull(); // Para listar pruebas en curso
//    List<Prueba> findByTieneIncidenteTrue();
//    List<Prueba> findByIdEmpleadoAndTieneIncidenteTrue(Integer idEmpleado);
//    List<Prueba> findByIdVehiculo(Integer idVehiculo);

}
