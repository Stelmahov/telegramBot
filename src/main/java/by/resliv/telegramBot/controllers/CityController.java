package by.resliv.telegramBot.controllers;

import by.resliv.telegramBot.dto.CityUpdateDto;
import by.resliv.telegramBot.entities.City;
import by.resliv.telegramBot.dto.CityDto;
import by.resliv.telegramBot.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cities")
public class CityController {
    private CityService cityService;
    private ConversionService conversionService;

    @Autowired
    public CityController(CityService cityService, ConversionService conversionService) {
        this.cityService = cityService;
        this.conversionService = conversionService;
    }

    @PostMapping
    public CityDto post(@RequestBody @Valid final CityDto cityDto) {
        City city = new City(null, cityDto.getName(), cityDto.getInfo());
        City createdCity = cityService.create(city);
        return conversionService.convert(createdCity, CityDto.class);
    }

    @PatchMapping("/{id}")
    public CityDto patch(@RequestBody @Valid final CityUpdateDto cityUpdateDto,
                         @PathVariable final Integer id) {
        City cityToUpdate = new City(id, cityUpdateDto.getName(), cityUpdateDto.getInfo());
        City updatedCity = cityService.update(cityToUpdate);
        return conversionService.convert(updatedCity, CityDto.class);
    }

    @GetMapping
    public List<CityDto> get() {
        return cityService.getAll().stream().map(city ->
                conversionService.convert(city, CityDto.class)).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CityDto get(@PathVariable final Integer id) {
        City returnedCity = cityService.getById(id);
        return conversionService.convert(returnedCity, CityDto.class);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        cityService.delete(id);
    }

}
