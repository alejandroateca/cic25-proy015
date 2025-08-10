package es.cic.curso25.proy015.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.cic.curso25.proy015.model.Vehiculo;
import es.cic.curso25.proy015.service.VehiculoService;

@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @GetMapping
    public List<Vehiculo> getAllVehiculos() {
        return vehiculoService.getAllVehiculos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> getVehiculo(@PathVariable Long id) {
        Vehiculo vehiculo = vehiculoService.getVehiculo(id);
        return ResponseEntity.ok(vehiculo);
    }

    @PostMapping
    public ResponseEntity<Vehiculo> createVehiculo(@RequestBody Vehiculo vehiculo) {
        Vehiculo creado = vehiculoService.createVehiculo(vehiculo);
        return ResponseEntity.ok(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> updateVehiculo(@PathVariable Long id, @RequestBody Vehiculo vehiculo) {
        if (!id.equals(vehiculo.getId())) {
            throw new IllegalArgumentException("El ID en la URL y el cuerpo no coinciden");
        }
        Vehiculo actualizado = vehiculoService.updateVehiculo(vehiculo);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehiculo(@PathVariable Long id) {
        vehiculoService.deleteVehiculo(id);
        return ResponseEntity.ok().build();
    }
}
