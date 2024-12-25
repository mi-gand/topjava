package ru.javawebinar.topjava.repositories;

import ru.javawebinar.topjava.model.Meal;
import java.util.Map;

public interface MealRepository  {
        public Meal save(Meal meal);
        Meal update(Meal meal);
        Meal read(int id);
        boolean delete(int id);
        Map<Integer, Meal> getAll();
    }