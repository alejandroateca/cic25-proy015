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

@RestController
@RequestMapping("/api/plazas")
public class PlazaController {

    @Autowired
    private PlazaService plazaService;

    @GetMapping
    public List<Plaza> listarTodas() {
        return plazaService.getAllPlazas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plaza> obtenerPorId(@PathVariable Long id) {
        Plaza plaza = plazaService.getPlaza(id);
        if (plaza == null) {
            throw new IdNotFoundException(id);
        }
        return ResponseEntity.ok(plaza);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Plaza> modificarPlaza(@PathVariable Long id, @RequestBody Plaza plazaModificada) {
        if (!id.equals(plazaModificada.getId())) {
            throw new IllegalArgumentException("El ID en la URL y el cuerpo no coinciden");
        }
        Plaza actualizada = plazaService.updatePlaza(plazaModificada);
        return ResponseEntity.ok(actualizada);
    }

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

    @PutMapping("/{plazaId}/aparcar")
    public ResponseEntity<Plaza> aparcarVehiculo(@PathVariable Long plazaId, @RequestBody Vehiculo vehiculo) {
        Plaza plaza = plazaService.aparcarVehiculo(plazaId, vehiculo);
        return ResponseEntity.ok(plaza);
    }

    @PutMapping("/{plazaId}/quitarVehiculo")
    public ResponseEntity<Plaza> quitarVehiculo(@PathVariable Long plazaId) {
        Plaza plaza = plazaService.removeVehiculo(plazaId);
        return ResponseEntity.ok(plaza);
    }
}
