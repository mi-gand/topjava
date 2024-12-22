package ru.javawebinar.topjava.repositories;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository  {
        void create(Meal meal);
        Meal read(int id);
        void update(int id, LocalDateTime dateTime, String description, int calories);
        boolean delete(int id);
        List<Meal> getAll();
    }