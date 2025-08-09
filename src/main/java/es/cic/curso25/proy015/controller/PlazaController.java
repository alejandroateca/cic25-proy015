package es.cic.curso25.proy015.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.cic.curso25.proy015.exception.IdNotFoundException;
import es.cic.curso25.proy015.exception.PlazaOcupadaException;
import es.cic.curso25.proy015.model.Plaza;
import es.cic.curso25.proy015.model.Vehiculo;
import es.cic.curso25.proy015.service.PlazaService;
import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/plazas")
public class PlazaController {

    @Autowired
    private PlazaService plazaService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping
    public List<Plaza> listarTodas() {
        return plazaService.getAllPlazas();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Plaza> obtenerPorId(@PathVariable Long id) {
        Plaza plaza = plazaService.getPlaza(id);
        if (plaza == null) {
            throw new IdNotFoundException(id);
        }
        return ResponseEntity.ok(plaza);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Plaza> modificarPlaza(@PathVariable Long id, @Valid @RequestBody Plaza plazaModificada) {
        if (!id.equals(plazaModificada.getId())) {
            throw new IllegalArgumentException("El ID en la URL y el cuerpo no coinciden");
        }
        Plaza actualizada = plazaService.updatePlaza(plazaModificada);
        return ResponseEntity.ok(actualizada);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/deshabilitar")
    public ResponseEntity<Void> deshabilitarPlaza(@PathVariable Long id) {
        Plaza plaza = plazaService.getPlaza(id);
        if (plaza == null) {
            throw new IdNotFoundException(id);
        }
        if (plaza.getVehiculoActual() != null) {
            throw new PlazaOcupadaException("No se puede deshabilitar plaza ocupada");
        }
        plaza.setDisponible(false);
        plazaService.updatePlaza(plaza);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PutMapping("/{plazaId}/aparcar")
    public ResponseEntity<Plaza> aparcarVehiculo(@PathVariable Long plazaId, @Valid @RequestBody Vehiculo vehiculo) {
        Plaza plaza = plazaService.aparcarVehiculo(plazaId, vehiculo);
        return ResponseEntity.ok(plaza);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PutMapping("/{plazaId}/quitarVehiculo")
    public ResponseEntity<Plaza> quitarVehiculo(@PathVariable Long plazaId) {
        Plaza plaza = plazaService.removeVehiculo(plazaId);
        return ResponseEntity.ok(plaza);
    }
}
