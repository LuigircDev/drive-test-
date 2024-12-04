package servicio.geolocalizacion.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "POSICIONES")
public class Posicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPosicion;

    @NotNull
    @Column(name = "ID_PRUEBA")
    private Integer idPrueba;

    @NotNull
    @Column(name = "FECHA_HORA")
    private LocalDateTime fechaHora;

    @NotNull
    @Column(name = "LATITUD")
    private Double latitud;

    @NotNull
    @Column(name = "LONGITUD")
    private Double longitud;

}
