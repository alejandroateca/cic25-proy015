package es.cic.curso25.proy015.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Plaza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean disponible; // habilitada o no

    @OneToMany(mappedBy = "plazaAsignada")
    private List<Vehiculo> vehiculosAutorizados;

    @OneToOne
    @JoinColumn(name = "vehiculo_actual_id")
    private Vehiculo vehiculoActual; // null si está libre

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public List<Vehiculo> getVehiculosAutorizados() {
        return vehiculosAutorizados;
    }

    public void setVehiculosAutorizados(List<Vehiculo> vehiculosAutorizados) {
        this.vehiculosAutorizados = vehiculosAutorizados;
    }

    public Vehiculo getVehiculoActual() {
        return vehiculoActual;
    }

    public void setVehiculoActual(Vehiculo vehiculoActual) {
        this.vehiculoActual = vehiculoActual;
    }

    
}
