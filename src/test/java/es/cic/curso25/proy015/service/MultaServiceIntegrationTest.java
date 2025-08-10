package es.cic.curso25.proy015.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.cic.curso25.proy015.exception.IdNotFoundException;
import es.cic.curso25.proy015.model.Multa;
import es.cic.curso25.proy015.model.Plaza;
import es.cic.curso25.proy015.model.Vehiculo;
import es.cic.curso25.proy015.repository.MultaRepository;
import es.cic.curso25.proy015.repository.PlazaRepository;
import es.cic.curso25.proy015.repository.VehiculoRepository;

@SpringBootTest
class MultaServiceIntegrationTest {

    @Autowired
    private MultaService multaService;

    @Autowired
    private MultaRepository multaRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private PlazaRepository plazaRepository;

    @BeforeEach
    void cleanDB() {
        multaRepository.deleteAll();
        vehiculoRepository.deleteAll();
        plazaRepository.deleteAll();
    }

    // Método auxiliar para crear y guardar una plaza
    private Plaza crearPlazaTest() {
        Plaza plaza = new Plaza();
        plaza.setDisponible(true);
        return plazaRepository.save(plaza);
    }

    // Método auxiliar para crear y guardar un vehículo con plaza asignada
    private Vehiculo crearVehiculoTest() {
        Plaza plaza = crearPlazaTest();

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setMatricula("TEST");
        vehiculo.setTipo("Coche");
        vehiculo.setPlazaAsignada(plaza);
        return vehiculoRepository.save(vehiculo);
    }

    @Test
    void getAllMultasTest() {
        Vehiculo vehiculo = crearVehiculoTest();

        Multa multa1 = new Multa();
        multa1.setDias(3);
        multa1.setVehiculo(vehiculo);
        multaRepository.save(multa1);

        Multa multa2 = new Multa();
        multa2.setDias(1);
        multa2.setVehiculo(vehiculo);
        multaRepository.save(multa2);

        List<Multa> multas = multaService.getAllMultas();
        assertEquals(2, multas.size());
    }

    @Test
    void getMultaTest() {
        Vehiculo vehiculo = crearVehiculoTest();

        Multa multa = new Multa();
        multa.setDias(2);
        multa.setVehiculo(vehiculo);
        multa = multaRepository.save(multa);

        Multa encontrada = multaService.getMulta(multa.getId());
        assertNotNull(encontrada);
        assertEquals(multa.getId(), encontrada.getId());
    }

    @Test
    void updateMultaTest() {
        Vehiculo vehiculo = crearVehiculoTest();

        Multa multa = new Multa();
        multa.setDias(5);
        multa.setVehiculo(vehiculo);
        multa = multaRepository.save(multa);

        multa.setDias(10);
        Multa actualizada = multaService.updateMulta(multa);

        assertEquals(10, actualizada.getDias());
    }

    @Test
    void updateMultaNotFoundTest() {
        Multa multa = new Multa();
        multa.setId(999L);
        multa.setDias(1);
        multa.setVehiculo(crearVehiculoTest());

        assertThrows(IdNotFoundException.class, () -> multaService.updateMulta(multa));
    }

    @Test
    void deleteMultaTest() {
        Vehiculo vehiculo = crearVehiculoTest();

        Multa multa = new Multa();
        multa.setDias(2);
        multa.setVehiculo(vehiculo);
        multa = multaRepository.save(multa);

        multaService.deleteMulta(multa.getId());

        Optional<Multa> eliminada = multaRepository.findById(multa.getId());
        assertTrue(eliminada.isEmpty());
    }

    @Test
    void calcularYGuardarImporteFinalTest() {
        Vehiculo vehiculo = crearVehiculoTest();

        Multa multa = new Multa();
        multa.setDias(4);
        multa.setVehiculo(vehiculo);
        multa = multaRepository.save(multa);

        Multa actualizada = multaService.calcularYGuardarImporteFinal(multa);

        assertEquals(20.0, actualizada.getImporte());
    }

}
