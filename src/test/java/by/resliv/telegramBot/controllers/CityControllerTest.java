package by.resliv.telegramBot.controllers;

import by.resliv.telegramBot.entities.City;
import by.resliv.telegramBot.services.CityService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.ws.rs.BadRequestException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CityController.class)
class CityControllerTest {

    private City createdCity = new City(1, "Москва", "Не забудьте посетить Красную Площадь. Ну а в ЦУМ можно и не заходить)))");

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CityService cityService;

    @Test
    public void testPost() throws Exception {
        City cityToCreate = new City(null, "Москва", "Не забудьте посетить Красную Площадь. Ну а в ЦУМ можно и не заходить)))");
        when(cityService.create(Mockito.eq(cityToCreate))).thenReturn(createdCity);

        String actual = mvc.perform(post("/api/cities")
                .content("{\"name\":\"Москва\",\"info\":\"Не забудьте посетить Красную Площадь. Ну а в ЦУМ можно и не заходить)))\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertEquals("{\"id\":1,\"name\":\"Москва\",\"info\":\"Не забудьте посетить Красную Площадь. Ну а в ЦУМ можно и не заходить)))\"}", actual);
    }

    @Test
    public void testPostBlankName() throws Exception {
        mvc.perform(post("/api/cities")
                .content("{\"name\":\"\",\"info\":\"some info\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPostIncorrectData() throws Exception {
        mvc.perform(post("/api/cities")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPostWithExistsName() throws Exception {
        City cityToCreate = new City(null, "Москва", null);
        Mockito.when(cityService.create(Mockito.eq(cityToCreate))).thenThrow(new BadRequestException());
        mvc.perform(post("/api/cities")
                .content("{\"name\" : \"Москва\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPatch() throws Exception {
        City cityToUpdate = createdCity;
        when(cityService.update(Mockito.eq(cityToUpdate))).thenReturn(cityToUpdate);

        String actual = mvc.perform(patch("/api/cities/1")
                .content("{\"name\":\"Москва\",\"info\":\"Не забудьте посетить Красную Площадь. Ну а в ЦУМ можно и не заходить)))\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertEquals("{\"id\":1,\"name\":\"Москва\",\"info\":\"Не забудьте посетить Красную Площадь. Ну а в ЦУМ можно и не заходить)))\"}", actual);
    }

    @Test
    public void testGetAll() throws Exception {
        Mockito.when(cityService.getAll()).thenReturn(Collections.emptyList());

        final String actual = mvc.perform(get("/api/cities")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertEquals("[]", actual);
    }

    @Test
    public void testGetById() throws Exception {
        Mockito.when(cityService.getById(1)).thenReturn(createdCity);

        final String actual = mvc.perform(get("/api/cities/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        Assertions.assertEquals("{\"id\":1,\"name\":\"Москва\",\"info\":\"Не забудьте посетить Красную Площадь. Ну а в ЦУМ можно и не заходить)))\"}", actual);
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(delete("/api/cities/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(cityService, Mockito.times(1)).delete(Mockito.eq(1));
    }
}