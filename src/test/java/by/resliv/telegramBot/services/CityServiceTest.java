package by.resliv.telegramBot.services;

import by.resliv.telegramBot.entities.City;
import by.resliv.telegramBot.repositories.CityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(CityService.class)
class CityServiceTest {
    private City createdCity = new City(1, "Москва", "Не забудьте посетить Красную Площадь. Ну а в ЦУМ можно и не заходить)))");
    private CityService cityService;
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CityRepository cityRepository;

    @BeforeEach
    private void init() {
       cityService = new CityService(cityRepository);
    }

    @Test
    public void testCreate() {
        City cityToCreate = new City(null, "Москва", "Не забудьте посетить Красную Площадь. Ну а в ЦУМ можно и не заходить)))");
        Mockito.when(cityRepository.save(Mockito.eq(cityToCreate))).thenReturn(createdCity);
        Assertions.assertEquals(createdCity, cityService.create(cityToCreate));
    }

    @Test
    public void testCreate_existingName() {
        Optional<City> cityWithSameName = Optional.of(createdCity);
        City cityToCreate = new City(null, "Москва", "Не забудьте посетить Красную Площадь. Ну а в ЦУМ можно и не заходить)))");
        Mockito.when(cityRepository.findByNameIgnoreCase("Москва")).thenReturn(cityWithSameName);
        BadRequestException thrown = Assertions.assertThrows(BadRequestException.class, () -> cityService.create(cityToCreate));
        Assertions.assertEquals("City with name Москва already exists", thrown.getMessage());
    }

    @Test
    public void testUpdate() {
        Mockito.when(cityRepository.findById(1)).thenReturn(Optional.of(createdCity));
        Mockito.when(cityRepository.save(Mockito.eq(createdCity))).thenReturn(createdCity);
        Assertions.assertEquals(createdCity, cityService.update(createdCity));
    }

    @Test
    public void testUpdate_wrongId() {
        NotFoundException wrongId = Assertions.assertThrows(NotFoundException.class, () -> cityService.update(createdCity));
        Assertions.assertEquals("City with id 1 does not exists", wrongId.getMessage());
    }

    @Test
    public void testUpdateName_sameNameAsAnotherCity() {
        City cityWithSameName = new City(2, "Минск", null);
        Mockito.when(cityRepository.findById(1)).thenReturn(Optional.of(createdCity));
        Mockito.when(cityRepository.findByNameIgnoreCase("Минск")).thenReturn(Optional.of(cityWithSameName));
        BadRequestException wrongName = Assertions.assertThrows(BadRequestException.class,
                () -> cityService.update(new City(1, "Минск", "some info")));
        Assertions.assertEquals("City with name Минск already exists", wrongName.getMessage());
    }

    @Test
    public void testGetAll() {
        final List<City> cityList = cityService.getAll();
        Mockito.verify(cityRepository, Mockito.times(1)).findAllByOrderByName();
        Assertions.assertEquals(new ArrayList<>(), cityList);
    }

    @Test
    public void testGetById() {
        Mockito.when(cityRepository.findById(1)).thenReturn(Optional.of(createdCity));
        Assertions.assertEquals(createdCity, cityService.getById(1));
    }

    @Test
    public void testGetById_wrongId() {
        NotFoundException wrongId = Assertions.assertThrows(NotFoundException.class, () -> cityService.getById(1));
        Assertions.assertEquals("City with id 1 does not exists", wrongId.getMessage());
    }

    @Test
    public void testGetByName() {
        Mockito.when(cityRepository.findByNameIgnoreCase("Москва")).thenReturn(Optional.of(createdCity));
        Assertions.assertEquals(Optional.of(createdCity), cityService.getByName("Москва"));

        Mockito.when(cityRepository.findByNameIgnoreCase("Минск")).thenReturn(Optional.empty());
        Assertions.assertEquals(Optional.empty(), cityService.getByName("Минск"));
    }

    @Test
    public void testDelete() {
        Mockito.when(cityRepository.existsById(1)).thenReturn(true);
        cityService.delete(1);
        Mockito.verify(cityRepository, Mockito.times(1)).deleteById(Mockito.eq(1));
    }

    @Test
    public void testDelete_wrongId() {
        Mockito.when(cityRepository.existsById(1)).thenReturn(false);
        NotFoundException wrongId = Assertions.assertThrows(NotFoundException.class, () -> cityService.delete(1));
        Assertions.assertEquals("City with id 1 does not exists", wrongId.getMessage());
    }

}