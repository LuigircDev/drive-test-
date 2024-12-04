package servicio.gestionDePruebas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "VEHICULOS")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "PATENTE")
    private String patente;

    @NotNull
    @Column(name = "ID_MODELO")
    private Integer idModelo;
}
