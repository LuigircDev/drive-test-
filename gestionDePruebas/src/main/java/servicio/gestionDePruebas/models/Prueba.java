package servicio.gestionDePruebas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "PRUEBAS")
public class Prueba {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRUEBA")
    private Integer idPrueba;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_INTERESADO", nullable = false)
    private Interesado interesado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_VEHICULO", nullable = false)
    private Vehiculo vehiculo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_EMPLEADO", nullable = false)
    private Empleado empleado;

    @NotNull
    @Column(name = "FECHA_HORA_INICIO")
    private LocalDateTime fechaHoraInicio;

    @Column(name = "FECHA_HORA_FIN")
    private LocalDateTime fechaHoraFin;

    @Column(name = "COMENTARIOS")
    private String comentarios;

    // tabla intermedia para relacionar una prueba con muchos incidentes pertenecientes a esa prueba
    @OneToMany(mappedBy = "prueba", fetch = FetchType.EAGER)
    private List<Incidente> incidentes;

    public void addIncidente(Incidente incidente) {
        incidentes.add(incidente);
}

}