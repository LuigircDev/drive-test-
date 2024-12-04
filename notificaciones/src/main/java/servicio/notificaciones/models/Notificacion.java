package servicio.notificaciones.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "NOTIFICACIONES")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "NUMERO_TELEFONO")
    private String numeroTelefono;

    @NotNull
    @Column(name = "MENSAJE")
    private String mensaje;

    @NotNull
    @Column(name = "FECHA_ENVIO")
    private LocalDateTime fechaEnvio;

    @NotNull
    @Column(name = "ESTADO")
    private String estado; // "ENVIADO", "FALLIDO"
}
