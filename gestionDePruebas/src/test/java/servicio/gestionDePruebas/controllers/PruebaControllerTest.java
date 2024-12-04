package servicio.gestionDePruebas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import servicio.gestionDePruebas.models.Prueba;
import servicio.gestionDePruebas.repositories.PruebaRepository;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PruebaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PruebaRepository pruebaRepository;

    @Test
    public void testCrearPrueba_Success() throws Exception {
        // Crear un objeto Prueba
        Prueba prueba = new Prueba();
        prueba.setIdVehiculo(101);
        prueba.setIdInteresado(201);
        prueba.setIdEmpleado(301);
        prueba.setFechaHoraInicio(LocalDateTime.now());

        // Realizar la solicitud POST
        mockMvc.perform(post("/api/pruebas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prueba)))
                .andExpect(status().isOk());
    }

    @Test
    public void testFinalizarPrueba_Success() throws Exception {
        // Crear y guardar una prueba en la base de datos
        Prueba prueba = new Prueba();
        prueba.setIdVehiculo(101);
        prueba.setIdInteresado(201);
        prueba.setIdEmpleado(301);
        prueba.setFechaHoraInicio(LocalDateTime.now());
        pruebaRepository.save(prueba);

        // Realizar la solicitud PATCH para finalizar la prueba
        mockMvc.perform(post("/api/pruebas/" + prueba.getId() + "/finalizar")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Comentario de prueba finalizada."))
                .andExpect(status().isOk());
    }

    @Test
    public void testObtenerPruebaPorId_Success() throws Exception {
        // Crear y guardar una prueba en la base de datos
        Prueba prueba = new Prueba();
        prueba.setIdVehiculo(101);
        prueba.setIdInteresado(201);
        prueba.setIdEmpleado(301);
        prueba.setFechaHoraInicio(LocalDateTime.now());
        pruebaRepository.save(prueba);

        // Realizar la solicitud GET para obtener la prueba
        mockMvc.perform(get("/api/pruebas/" + prueba.getId())  // Cambié post() por get()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testFinalizarPrueba_Failure_NoExistente() throws Exception {
        mockMvc.perform(post("/api/pruebas/999/finalizar")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Intento de finalizar una prueba inexistente."))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testFinalizarPrueba_Failure_YaFinalizada() throws Exception {
        Prueba prueba = new Prueba();
        prueba.setIdVehiculo(101);
        prueba.setIdInteresado(201);
        prueba.setIdEmpleado(301);
        prueba.setFechaHoraInicio(LocalDateTime.now());
        prueba.setFechaHoraFin(LocalDateTime.now());
        pruebaRepository.save(prueba);

        mockMvc.perform(post("/api/pruebas/" + prueba.getId() + "/finalizar")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Intento de finalizar una prueba ya finalizada."))
                .andExpect(status().isBadRequest());
    }



}