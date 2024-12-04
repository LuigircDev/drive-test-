package servicio.gestionDePruebas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "INTERESADOS")
public class Interesado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "TIPO_DOCUMENTO")
    private String tipoDocumento;

    @NotNull
    @Column(name = "DOCUMENTO")
    private String documento;

    @NotNull
    @Column(name = "NOMBRE")
    private String nombre;

    @NotNull
    @Column(name = "APELLIDO")
    private String apellido;

    @Column(name = "RESTRINGIDO")
    private Boolean restringido;

    @NotNull
    @Column(name = "NRO_LICENCIA")
    private String numeroLicencia;

    @NotNull
    @Column(name = "FECHA_VENCIMIENTO_LICENCIA")
    private LocalDate fechaVencimientoLicencia;
}

