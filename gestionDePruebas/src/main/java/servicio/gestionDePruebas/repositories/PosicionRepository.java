package servicio.gestionDePruebas.repositories;

import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface PosicionRepository {
    @Query("SELECT SUM(DISTANCIA) FROM (calculo_de_distancias_subquery)")
    Double calcularKilometrosRecorridos(Integer idVehiculo, LocalDateTime inicio, LocalDateTime fin);
    //(Se asume que tienes un mecanismo para registrar las distancias en cada movimiento).
}
