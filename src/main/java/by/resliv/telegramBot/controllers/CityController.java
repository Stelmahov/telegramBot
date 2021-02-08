package by.resliv.telegramBot.controllers;

import by.resliv.telegramBot.dto.CityUpdateDto;
import by.resliv.telegramBot.entities.City;
import by.resliv.telegramBot.dto.CityDto;
import by.resliv.telegramBot.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {
    private CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping
    public City post(@RequestBody @Valid final CityDto cityDto) {
        City city = new City(null, cityDto.getName(), cityDto.getInfo());
        return cityService.create(city);
    }

    @PatchMapping("/{id}")
    public City patch(@RequestBody @Valid final CityUpdateDto cityUpdateDto,
                      @PathVariable final Integer id) {
        City cityToUpdate = new City(id, cityUpdateDto.getName(), cityUpdateDto.getInfo());
        return cityService.update(cityToUpdate);
    }

    @GetMapping
    public List<City> get() {
        return cityService.getAll();
    }

    @GetMapping("/{id}")
    public City get(@PathVariable final Integer id) {
        return cityService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        cityService.delete(id);
    }

}
