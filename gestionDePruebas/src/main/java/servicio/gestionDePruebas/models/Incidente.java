package servicio.gestionDePruebas.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "INCIDENTES")
@Data
public class Incidente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INCIDENTE")
    private Integer id;
    private String mensaje;
    private Boolean notificado;
    @ManyToOne
    @JoinColumn(name = "ID_PRUEBA")
    private Prueba prueba;
    public Incidente(String mensaje, Boolean notificado) {
        this.mensaje = mensaje;
        this.notificado = notificado;
    }


}