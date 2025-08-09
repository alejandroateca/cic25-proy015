package es.cic.curso25.proy015.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Entity
public class Plaza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean disponible; // habilitada o no

    @OneToMany(mappedBy = "plazaAsignada")
    @Valid
    @JsonManagedReference
    @NotNull(message = "La lista de vehículos autorizados no puede ser nula")
    private List<Vehiculo> vehiculosAutorizados = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "vehiculo_actual_id")
    @Valid
    @JsonManagedReference
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
