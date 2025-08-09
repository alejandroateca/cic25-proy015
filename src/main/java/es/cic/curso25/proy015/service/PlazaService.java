package es.cic.curso25.proy015.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.cic.curso25.proy015.exception.IdNotFoundException;
import es.cic.curso25.proy015.exception.PlazaOcupadaException;
import es.cic.curso25.proy015.model.Multa;
import es.cic.curso25.proy015.model.Plaza;
import es.cic.curso25.proy015.model.Vehiculo;
import es.cic.curso25.proy015.repository.PlazaRepository;

@Service
public class PlazaService {

    private static final Logger logger = LoggerFactory.getLogger(PlazaService.class);

    @Autowired
    private PlazaRepository plazaRepository;

    @Autowired
    private MultaService multaService;

    public List<Plaza> getAllPlazas() {
        logger.info("Obteniendo todas las plazas");
        return plazaRepository.findAll();
    }

    public Plaza getPlaza(Long id) {
        logger.info("Buscando plaza con id {}", id);
        Plaza plaza = plazaRepository.findById(id).orElse(null);
        logger.info("Resultado de búsqueda de plaza con id {}: {}", id, plaza != null ? "encontrada" : "no encontrada");
        return plaza;
    }

    public Plaza updatePlaza(Plaza plaza) {
        logger.info("Actualizando plaza con id {}", plaza.getId());
        if (!plazaRepository.existsById(plaza.getId())) {
            throw new IdNotFoundException(plaza.getId());
        }
        Plaza updated = plazaRepository.save(plaza);
        logger.info("Plaza con id {} actualizada correctamente", plaza.getId());
        return updated;
    }

    public void deletePlaza(Long id) {
        logger.info("Eliminando plaza con id {}", id);
        plazaRepository.deleteById(id);
        logger.info("Plaza con id {} eliminada", id);
    }

    public Plaza aparcarVehiculo(Long plazaId, Vehiculo vehiculo) {
        logger.info("Intentando aparcar vehículo id {} en plaza id {}", vehiculo.getId(), plazaId);

        Plaza plaza = plazaRepository.findById(plazaId)
            .orElseThrow(() -> new IdNotFoundException(plazaId));

        if (!plaza.isDisponible() || plaza.getVehiculoActual() != null) {
            throw new PlazaOcupadaException("Plaza no disponible o ya ocupada");
        }

        if (!vehiculo.getPlazaAsignada().getId().equals(plazaId)) {
            logger.info("Vehículo id {} aparcando en plaza distinta a la asignada ({}), se crea multa", vehiculo.getId(), plazaId);
            Multa multa = new Multa();
            multa.setVehiculo(vehiculo);
            multa.setDias(0);
            multa.setImporte(0);
            multaService.createMulta(multa);
            logger.info("Multa creada para vehículo id {}", vehiculo.getId());
        }

        plaza.setVehiculoActual(vehiculo);
        Plaza saved = plazaRepository.save(plaza);

        logger.info("Vehículo id {} aparcado correctamente en plaza id {}", vehiculo.getId(), plazaId);
        return saved;
    }

    public Plaza removeVehiculo(Long plazaId) {
        logger.info("Intentando remover vehículo de plaza id {}", plazaId);

        Plaza plaza = plazaRepository.findById(plazaId)
            .orElseThrow(() -> new IdNotFoundException(plazaId));

        plaza.setVehiculoActual(null);
        Plaza saved = plazaRepository.save(plaza);

        logger.info("Vehículo removido de plaza id {}", plazaId);
        return saved;
    }
}
