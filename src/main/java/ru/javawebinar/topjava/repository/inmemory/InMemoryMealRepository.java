package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import org.springframework.util.comparator.Comparators;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository= new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));

        List<Meal> mealsForSecondUser = Arrays.asList(
                new Meal(LocalDateTime.of(2024, Month.JANUARY, 10, 10, 1), "Завтрак", 1500)
                );
        mealsForSecondUser.forEach(meal -> save(meal, 2));

    }

    // null if updated meal does not belong to userId TODO
    @Override
    public Meal save(Meal meal, int userId){
        Map<Integer, Meal> userMeals = repository.computeIfAbsent(userId, v -> new ConcurrentHashMap<>());
        if(meal.isNew()){
            meal.setId(counter.incrementAndGet());
            return userMeals.put(meal.getId(), meal);
        }else{
            return userMeals.computeIfPresent(meal.getId(),  (id, oldMeal) -> meal);
        }
    }

    // false if meal does not belong to userId TODO
    @Override
    public boolean delete(int id, int userId){
        Map<Integer, Meal> userMeals = repository.get(userId);
        if(userMeals != null){
            return userMeals.remove(id) != null;
        }else{
            return false;
        }
    }

    // null if meal does not belong to userId TODO
    @Override
    public Meal get(int id, int userId){
        Map<Integer, Meal> userMeals = repository.get(userId);
        if(userMeals != null){
            return userMeals.get(id);
        }else{
            return null;
        }
    }

    // ORDERED dateTime desc
    @Override
    public Collection<Meal> getAll(int userId){
        Map<Integer, Meal> userMeals = repository.get(userId);
        List<Meal> meals = new ArrayList<>(userMeals.values());
        meals.sort(Comparator.comparing(Meal::getDateTime).reversed());
        return meals;
    }
}