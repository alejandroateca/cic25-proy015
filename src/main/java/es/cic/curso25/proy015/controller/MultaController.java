package es.cic.curso25.proy015.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.cic.curso25.proy015.exception.IdNotFoundException;
import es.cic.curso25.proy015.model.Multa;
import es.cic.curso25.proy015.service.MultaService;

@RestController
@RequestMapping("/multas")
public class MultaController {

    @Autowired
    private MultaService multaService;

    @GetMapping
    public List<Multa> getAllMultas() {
        return multaService.getAllMultas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Multa> getMulta(@PathVariable Long id) {
        Multa multa = multaService.getMulta(id);
        if (multa == null) {
            throw new IdNotFoundException(id);
        }
        return ResponseEntity.ok(multa);
    }

    @PostMapping
    public ResponseEntity<Multa> createMulta(@RequestBody Multa multa) {
        Multa creada = multaService.createMulta(multa);
        return ResponseEntity.ok(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Multa> updaterMulta(@PathVariable Long id, @RequestBody Multa multa) {
        if (!id.equals(multa.getId())) {
            throw new IllegalArgumentException("El ID en la URL y el cuerpo no coinciden");
        }
        Multa actualizada = multaService.updateMulta(multa);
        return ResponseEntity.ok(actualizada);
    }

    @PutMapping("/{id}/calcularImporte")
    public ResponseEntity<Multa> calcularYGuardarImporteFinal(@PathVariable Long id, @RequestBody Multa multa) {
        if (!id.equals(multa.getId())) {
            throw new IllegalArgumentException("El ID en la URL y el cuerpo no coinciden");
        }
        Multa multaActualizada = multaService.calcularYGuardarImporteFinal(multa);
        return ResponseEntity.ok(multaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMulta(@PathVariable Long id) {
        multaService.deleteMulta(id);
        return ResponseEntity.ok().build();
    }
}
