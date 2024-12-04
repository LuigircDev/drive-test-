package servicio.gestionDePruebas.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "EMPLEADOS")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEmpleado;

    private String nombre;
    private String apellido;

    @Column(name = "NUMERO_TELEFONO")
    private String numeroTelefono;
}
