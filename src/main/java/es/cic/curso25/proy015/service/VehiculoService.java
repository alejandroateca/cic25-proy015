package es.cic.curso25.proy015.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.cic.curso25.proy015.exception.IdNotFoundException;
import es.cic.curso25.proy015.model.Plaza;
import es.cic.curso25.proy015.model.Vehiculo;
import es.cic.curso25.proy015.repository.VehiculoRepository;

@Service
public class VehiculoService {

    private static final Logger logger = LoggerFactory.getLogger(VehiculoService.class);

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private PlazaService plazaService;

    public List<Vehiculo> getAllVehiculos() {
        logger.info("Obteniendo todos los vehículos");
        return vehiculoRepository.findAll();
    }

    public Vehiculo getVehiculo(Long id) {
        logger.info("Buscando vehículo con id {}", id);
        Vehiculo vehiculo = vehiculoRepository.findById(id)
            .orElseThrow(() -> new IdNotFoundException(id));
        logger.info("Vehículo con id {} encontrado", id);
        return vehiculo;
    }

    public Vehiculo createVehiculo(Vehiculo vehiculo) {
        logger.info("Creando vehículo para plaza asignada id {}", vehiculo.getPlazaAsignada() != null ? vehiculo.getPlazaAsignada().getId() : null);

        Plaza plaza = vehiculo.getPlazaAsignada();
        if (plaza == null) {
            throw new IllegalArgumentException("El vehículo debe tener una plaza asignada");
        }

        if (plaza.getVehiculosAutorizados() != null && plaza.getVehiculosAutorizados().size() >= 5) {
            throw new IllegalStateException("La plaza ya tiene 5 vehículos autorizados");
        }

        Vehiculo created = vehiculoRepository.save(vehiculo);
        logger.info("Vehículo creado con id {}", created.getId());
        return created;
    }

    public Vehiculo updateVehiculo(Vehiculo vehiculo) {
        logger.info("Actualizando vehículo con id {}", vehiculo.getId());
        if (!vehiculoRepository.existsById(vehiculo.getId())) {
            throw new IdNotFoundException(vehiculo.getId());
        }
        Vehiculo updated = vehiculoRepository.save(vehiculo);
        logger.info("Vehículo con id {} actualizado correctamente", vehiculo.getId());
        return updated;
    }

    public void deleteVehiculo(Long id) {
        logger.info("Eliminando vehículo con id {}", id);
        Vehiculo vehiculo = getVehiculo(id);

        if (vehiculo.getPlazaAsignada() != null && vehiculo.getPlazaAsignada().getVehiculoActual() != null
                && vehiculo.getPlazaAsignada().getVehiculoActual().getId().equals(vehiculo.getId())) {
            logger.info("Removiendo vehículo id {} de su plaza asignada id {}", vehiculo.getId(), vehiculo.getPlazaAsignada().getId());
            plazaService.removeVehiculo(vehiculo.getPlazaAsignada().getId());
        }

        vehiculoRepository.deleteById(id);
        logger.info("Vehículo con id {} eliminado", id);
    }
}
