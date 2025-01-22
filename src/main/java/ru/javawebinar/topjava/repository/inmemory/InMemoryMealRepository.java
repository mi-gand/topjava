package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));

        List<Meal> mealsForSecondUser = Arrays.asList(
                new Meal(LocalDateTime.of(2024, Month.JANUARY, 10, 10, 1), "Завтрак", 1500)
        );
        mealsForSecondUser.forEach(meal -> save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        return repository.computeIfAbsent(userId, v -> new ConcurrentHashMap<>())
                .compute(meal.isNew() ? counter.incrementAndGet() : meal.getId(), (id, existingMeal) -> {
                    if (!meal.isNew() && existingMeal == null) {
                        return null;
                    }
                    meal.setId(id);
                    return meal;
                });
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals == null) {
            return false;
        }
        return userMeals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals == null ? null : userMeals.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals == null) {
            return Collections.emptyList();
        }
        return userMeals.values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getFilteredByDate(LocalDate startDate, LocalDate endDate, int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        LocalDate notNullStartDate = (startDate == null) ? LocalDate.MIN : startDate;
        LocalDate notNullEndDate = (endDate == null) ? LocalDate.MAX : endDate;

        return userMeals.values().stream()
                .filter(meal -> DateTimeUtil.isBetweenCloseDate(meal.getDate(), notNullStartDate, notNullEndDate))
                .collect(Collectors.toList());
    }
}