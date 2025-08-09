package es.cic.curso25.proy015.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.cic.curso25.proy015.exception.IdNotFoundException;
import es.cic.curso25.proy015.model.Multa;
import es.cic.curso25.proy015.repository.MultaRepository;

@Service
public class MultaService {

    private static final Logger logger = LoggerFactory.getLogger(MultaService.class);
    private static final double IMPORTE_POR_DIA = 5.0;

    @Autowired
    private MultaRepository multaRepository;

    public List<Multa> getAllMultas() {
        logger.info("Obteniendo todas las multas");
        return multaRepository.findAll();
    }

    public Multa getMulta(Long id) {
        logger.info("Buscando multa con id {}", id);
        Multa multa = multaRepository.findById(id).orElse(null);
        logger.info("Resultado de búsqueda de multa con id {}: {}", id, multa != null ? "encontrada" : "no encontrada");
        return multa;
    }

    public Multa updateMulta(Multa multa) {
        logger.info("Actualizando multa con id {}", multa.getId());
        if (!multaRepository.existsById(multa.getId())) {
            throw new IdNotFoundException(multa.getId());
        }
        Multa updated = multaRepository.save(multa);
        logger.info("Multa con id {} actualizada correctamente", multa.getId());
        return updated;
    }

    public Multa createMulta(Multa multa) {
        logger.info("Creando nueva multa para vehículo id {}", multa.getVehiculo().getId());
        Multa created = multaRepository.save(multa);
        logger.info("Multa creada con id {}", created.getId());
        return created;
    }

    public void deleteMulta(Long id) {
        logger.info("Eliminando multa con id {}", id);
        multaRepository.deleteById(id);
        logger.info("Multa con id {} eliminada", id);
    }
    
    public Multa calcularYGuardarImporteFinal(Multa multa) {
        logger.info("Calculando importe final para multa id {}", multa.getId());
        double importeFinal = calcularImporte(multa.getDias());
        multa.setImporte(importeFinal);
        Multa saved = multaRepository.save(multa);
        logger.info("Importe final {} guardado para multa id {}", importeFinal, multa.getId());
        return saved;
    }

    private double calcularImporte(int dias) {
        if (dias <= 0) {
            return 0;
        }
        return dias * IMPORTE_POR_DIA;
    }
}
