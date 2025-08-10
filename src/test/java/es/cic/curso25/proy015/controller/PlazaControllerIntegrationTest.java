package es.cic.curso25.proy015.controller;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.cic.curso25.proy015.model.Plaza;
import es.cic.curso25.proy015.model.Vehiculo;
import es.cic.curso25.proy015.repository.PlazaRepository;
import es.cic.curso25.proy015.repository.VehiculoRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@SpringBootTest
@AutoConfigureMockMvc
public class PlazaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlazaRepository plazaRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Test
    void getAllPlazasTest() throws Exception {
        mockMvc.perform(get("/plazas")
                .with(httpBasic("user", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getPlazaTest() throws Exception {
        Plaza plaza = new Plaza();
        plaza.setDisponible(true);
        plaza = plazaRepository.save(plaza);

        mockMvc.perform(get("/plazas/" + plaza.getId())
                .with(httpBasic("user", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updatePlazaTest() throws Exception {
        Plaza plaza = new Plaza();
        plaza.setDisponible(true);
        plaza = plazaRepository.save(plaza);

        plaza.setDisponible(false);
        String json = objectMapper.writeValueAsString(plaza);

        mockMvc.perform(put("/plazas/" + plaza.getId())
                .with(httpBasic("user", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Plaza actualizada = objectMapper.readValue(result.getResponse().getContentAsString(), Plaza.class);
                    assertTrue(!actualizada.isDisponible());
                });
    }

    @Test
    void deshabilitarPlazaTest() throws Exception {
        Plaza plaza = new Plaza();
        plaza.setDisponible(true);
        plaza.setVehiculoActual(null);
        plaza = plazaRepository.save(plaza);

        mockMvc.perform(put("/plazas/" + plaza.getId() + "/deshabilitar")
                .with(httpBasic("user", "password")))
                .andExpect(status().isOk());

        Optional<Plaza> actualizada = plazaRepository.findById(plaza.getId());
        assertTrue(actualizada.isPresent() && !actualizada.get().isDisponible());
    }

    @Test
    void aparcarVehiculoTest() throws Exception {
        Plaza plaza = new Plaza();
        plaza.setDisponible(true);
        plaza.setVehiculosAutorizados(new ArrayList<>()); // inicializa lista para evitar errores de validación
        plaza = plazaRepository.save(plaza);

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlazaAsignada(plaza);
        vehiculo = vehiculoRepository.save(vehiculo);

        String json = objectMapper.writeValueAsString(vehiculo);

        mockMvc.perform(put("/plazas/" + plaza.getId() + "/aparcar")
                .with(httpBasic("user", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Plaza actualizada = objectMapper.readValue(result.getResponse().getContentAsString(), Plaza.class);
                    assertTrue(actualizada.getVehiculoActual() != null);
                });
    }

    @Test
    void quitarVehiculoTest() throws Exception {
        Plaza plaza = new Plaza();
        plaza.setDisponible(true);
        plaza = plazaRepository.save(plaza); // guardamos la plaza primero

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlazaAsignada(plaza);
        vehiculo = vehiculoRepository.save(vehiculo);

        plaza.setVehiculoActual(vehiculo);
        plaza = plazaRepository.save(plaza);

        mockMvc.perform(put("/plazas/" + plaza.getId() + "/quitarVehiculo")
                .with(httpBasic("user", "password")))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Plaza actualizada = objectMapper.readValue(result.getResponse().getContentAsString(), Plaza.class);
                    assertNull(actualizada.getVehiculoActual());
                });
    }

}
