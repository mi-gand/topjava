package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

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



/*    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> userMeals = repository.computeIfAbsent(userId, v -> new ConcurrentHashMap<>());

        synchronized (userMeals) {
            if (!meal.isNew() && !userMeals.containsKey(meal.getId())) {
                return null;
            }
            if (meal.isNew()) {
                meal.setId(counter.incrementAndGet());
            }
            userMeals.put(meal.getId(), meal);
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        if (isMealBelongsToUser(id, userId)) {
            return false;
        }
        return repository.get(userId).remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        if (isMealBelongsToUser(id, userId)) {
            return null;
        }
        return repository.get(userId).get(id);
    }

    private boolean isMealBelongsToUser(int mealId, int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals == null || !userMeals.containsKey(mealId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals == null) {
            return Collections.emptyList();
        }
        List<Meal> meals = new ArrayList<>(userMeals.values());
        meals.sort(Comparator.comparing(Meal::getDateTime).reversed());
        return meals;
    }*/
}