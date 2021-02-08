package by.resliv.telegramBot.repositories;

import by.resliv.telegramBot.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    Optional<City> findByNameIgnoreCase(String name);
    List<City> findAllByOrderByName();
}
