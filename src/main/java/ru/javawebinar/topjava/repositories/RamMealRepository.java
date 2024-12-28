package ru.javawebinar.topjava.repositories;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class RamMealRepository implements MealRepository {
    private static final Logger log = getLogger(RamMealRepository.class);
    private final AtomicInteger counterId = new AtomicInteger(0);
    private final Map<Integer, Meal> localRepositoryMap = new ConcurrentHashMap<>();

    {
        localRepositoryMap.put(1, new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        localRepositoryMap.put(2, new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        localRepositoryMap.put(3, new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        localRepositoryMap.put(4, new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        localRepositoryMap.put(5, new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        localRepositoryMap.put(6, new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        localRepositoryMap.put(7, new Meal(7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));

        counterId.set(7);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.getId() == null) {
            int newId = incrementId();
            localRepositoryMap.put(newId, new Meal(newId, meal.getDateTime(), meal.getDescription(),
                    meal.getCalories()));
        } else {
            return localRepositoryMap.computeIfPresent(meal.getId(), (id, existingMeal) ->
                    new Meal(id, meal.getDateTime(), meal.getDescription(), meal.getCalories()));
        }
        log.debug("Meal object to repository: {}", meal);
        return meal;
    }

    private int incrementId() {
        return counterId.incrementAndGet();
    }

    @Override
    public Meal read(int id) {
        return localRepositoryMap.get(id);
    }

    @Override
    public boolean delete(int id) {
        return localRepositoryMap.remove(id) != null;
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(localRepositoryMap.values());
    }
}
