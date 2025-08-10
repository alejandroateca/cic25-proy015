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
class VehiculoServiceIntegrationTest {

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private PlazaRepository plazaRepository;

    @BeforeEach
    void cleanDB() {
        vehiculoRepository.deleteAll();
        plazaRepository.deleteAll();
    }

    @Test
    void getAllVehiculosTest() {
        Plaza plaza = new Plaza();
        plaza.setDisponible(true);
        plaza = plazaRepository.save(plaza);

        Vehiculo v1 = new Vehiculo();
        v1.setMatricula("AAA111");
        v1.setTipo("Coche");
        v1.setPlazaAsignada(plaza);
        vehiculoRepository.save(v1);

        Vehiculo v2 = new Vehiculo();
        v2.setMatricula("BBB222");
        v2.setTipo("Moto");
        v2.setPlazaAsignada(plaza);
        vehiculoRepository.save(v2);

        List<Vehiculo> vehiculos = vehiculoService.getAllVehiculos();
        assertEquals(2, vehiculos.size());
    }

    @Test
    void getVehiculoTest() {
        Plaza plaza = new Plaza();
        plaza.setDisponible(true);
        plaza = plazaRepository.save(plaza);

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setMatricula("CCC333");
        vehiculo.setTipo("Coche");
        vehiculo.setPlazaAsignada(plaza);
        vehiculo = vehiculoRepository.save(vehiculo);

        Vehiculo encontrado = vehiculoService.getVehiculo(vehiculo.getId());
        assertNotNull(encontrado);
        assertEquals(vehiculo.getId(), encontrado.getId());
    }

    @Test
    void createVehiculoTest() {
        Plaza plaza = new Plaza();
        plaza.setDisponible(true);
        plaza = plazaRepository.save(plaza);

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setMatricula("DDD444");
        vehiculo.setTipo("Coche");
        vehiculo.setPlazaAsignada(plaza);

        Vehiculo creado = vehiculoService.createVehiculo(vehiculo);
        assertNotNull(creado.getId());
        assertEquals("DDD444", creado.getMatricula());
    }

    @Test
    void createVehiculoSinPlazaAsignadaTest() {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setMatricula("EEE555");
        vehiculo.setTipo("Moto");
        vehiculo.setPlazaAsignada(null);

        assertThrows(IllegalArgumentException.class, () -> vehiculoService.createVehiculo(vehiculo));
    }

    @Test
    void updateVehiculoTest() {
        Plaza plaza = new Plaza();
        plaza.setDisponible(true);
        plaza = plazaRepository.save(plaza);

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setMatricula("GGG777");
        vehiculo.setTipo("Coche");
        vehiculo.setPlazaAsignada(plaza);
        vehiculo = vehiculoRepository.save(vehiculo);

        vehiculo.setMatricula("GGG888");
        Vehiculo actualizado = vehiculoService.updateVehiculo(vehiculo);

        assertEquals("GGG888", actualizado.getMatricula());
    }

    @Test
    void deleteVehiculoTest() {
        Plaza plaza = new Plaza();
        plaza.setDisponible(true);
        plaza = plazaRepository.save(plaza);

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setMatricula("III111");
        vehiculo.setTipo("Coche");
        vehiculo.setPlazaAsignada(plaza);
        vehiculo = vehiculoRepository.save(vehiculo);

        vehiculoService.deleteVehiculo(vehiculo.getId());

        Optional<Vehiculo> eliminado = vehiculoRepository.findById(vehiculo.getId());
        assertTrue(eliminado.isEmpty());
    }
}
