package servicio.geolocalizacion.repositorios;


import org.springframework.data.jpa.repository.JpaRepository;
import servicio.geolocalizacion.models.Posicion;

public interface PosicionRepository extends JpaRepository<Posicion, Integer> {




}

