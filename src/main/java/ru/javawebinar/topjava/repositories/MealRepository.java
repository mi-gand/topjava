package ru.javawebinar.topjava.repositories;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealRepository  {
        Meal save(Meal meal);
        Meal read(int id);
        boolean delete(int id);
        List<Meal> getAll();
    }