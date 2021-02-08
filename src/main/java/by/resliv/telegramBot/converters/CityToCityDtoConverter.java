package by.resliv.telegramBot.converters;

import by.resliv.telegramBot.dto.CityDto;
import by.resliv.telegramBot.entities.City;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CityToCityDtoConverter implements Converter<City, CityDto> {
    @Override
    public CityDto convert(City source) {
        return new CityDto(source.getId(), source.getName(), source.getInfo());
    }
}
