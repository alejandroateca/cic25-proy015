package es.cic.curso25.proy015.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.cic.curso25.proy015.model.Plaza;
import es.cic.curso25.proy015.model.Vehiculo;
import es.cic.curso25.proy015.repository.PlazaRepository;
import es.cic.curso25.proy015.repository.VehiculoRepository;

@SpringBootTest
class PlazaServiceIntegrationTest {

    @Autowired
    private PlazaService plazaService;

    @Autowired
    private PlazaRepository plazaRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @BeforeEach
    void cleanDB() {
        vehiculoRepository.deleteAll();
        plazaRepository.deleteAll();
    }

    @Test
    void getAllPlazasTest() {
        Plaza p1 = new Plaza();
        p1.setDisponible(true);
        plazaRepository.save(p1);

        Plaza p2 = new Plaza();
        p2.setDisponible(false);
        plazaRepository.save(p2);

        List<Plaza> plazas = plazaService.getAllPlazas();
        assertEquals(2, plazas.size());
    }

    @Test
    void getPlazaTest() {
        Plaza plaza = new Plaza();
        plaza.setDisponible(true);
        plaza = plazaRepository.save(plaza);

        Plaza encontrada = plazaService.getPlaza(plaza.getId());
        assertNotNull(encontrada);
        assertEquals(plaza.getId(), encontrada.getId());
    }

    @Test
    void updatePlazaTest() {
        Plaza plaza = new Plaza();
        plaza.setDisponible(true);
        plaza = plazaRepository.save(plaza);

        plaza.setDisponible(false);
        Plaza actualizada = plazaService.updatePlaza(plaza);

        assertFalse(actualizada.isDisponible());
    }

    @Test
    void deletePlazaTest() {
        Plaza plaza = new Plaza();
        plaza.setDisponible(true);
        plaza = plazaRepository.save(plaza);

        plazaService.deletePlaza(plaza.getId());

        Optional<Plaza> eliminada = plazaRepository.findById(plaza.getId());
        assertTrue(eliminada.isEmpty());
    }

    @Test
    void aparcarVehiculoTest() {
        Plaza plaza = new Plaza();
        plaza.setDisponible(true);
        plaza = plazaRepository.save(plaza);

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setMatricula("ABC123");
        vehiculo.setTipo("Coche");
        vehiculo.setPlazaAsignada(plaza);
        vehiculo = vehiculoRepository.save(vehiculo);

        Plaza result = plazaService.aparcarVehiculo(plaza.getId(), vehiculo);

        assertNotNull(result.getVehiculoActual());
        assertEquals("ABC123", result.getVehiculoActual().getMatricula());
    }

    @Test
    void removeVehiculoTest() {
        Plaza plaza = new Plaza();
        plaza.setDisponible(true);
        plaza = plazaRepository.save(plaza);

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setMatricula("REMOVE1");
        vehiculo.setTipo("Coche");
        vehiculo.setPlazaAsignada(plaza);
        vehiculo = vehiculoRepository.save(vehiculo);

        plaza.setVehiculoActual(vehiculo);
        plazaRepository.save(plaza);

        Plaza result = plazaService.removeVehiculo(plaza.getId());

        assertNull(result.getVehiculoActual());
    }
}
