package by.resliv.telegramBot.services;

import by.resliv.telegramBot.entities.City;
import by.resliv.telegramBot.repositories.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class CityService {
    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public City create(City city) {
        if (cityRepository.findByNameIgnoreCase(city.getName()).isPresent()) {
            throw new BadRequestException(String.format("City with name %s already exists", city.getName()));
        }
        return cityRepository.save(city);
    }

    @CachePut(value = "cities", key = "cityToUpdate.id")
    public City update(City cityToUpdate) {
        final Integer id = cityToUpdate.getId();
        final String name = cityToUpdate.getName();
        final String info = cityToUpdate.getInfo();
        if (name == null && info == null) {
            throw new BadRequestException("At least one parameter must not be null");
        }
        City city = getById(id);
        if (name != null) {
            if (name.trim().equals("")) {
                throw new BadRequestException("Name should not be blank");
            }
            if (!name.equals(city.getName()) && cityRepository.findByNameIgnoreCase(name).isPresent()) {
                throw new BadRequestException(String.format("City with name %s already exists", name));
            }
            city.setName(name);
        }
        if (info != null) {
            city.setInfo(info);
        }
        return cityRepository.save(city);
    }

    public List<City> getAll() {
        return cityRepository.findAllByOrderByName();
    }

    @Cacheable(value = "cities")
    public City getById(Integer id) {
        return cityRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("City with id %s does not exists", id)));
    }

    public Optional<City> getByName(String name) {
        return cityRepository.findByNameIgnoreCase(name);
    }

    @CacheEvict
    public void delete(Integer id) {
        if (!cityRepository.existsById(id)) {
            throw new NotFoundException(String.format("City with id %s does not exists", id));
        }
        cityRepository.deleteById(id);
    }
}
