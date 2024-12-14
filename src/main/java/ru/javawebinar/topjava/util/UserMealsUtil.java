package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public final class UserMealsUtil {

    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                        "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0),
                        "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
                        "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0),
                        "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0),
                        "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0),
                        "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0),
                        "Ужин", 401)
        );

        System.out.println("Значения filteredByCycles");
        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(0, 0), LocalTime.of(
                23, 10), 2000);
        mealsTo.forEach(System.out::println);
        System.out.println("Значения filteredByStreams");
        filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(
                23, 10), 2000).forEach(System.out::println);
    }

    private UserMealsUtil() {
        throw new UnsupportedOperationException("Util class");
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDay = new HashMap<>();
        for (UserMeal meal : meals) {
            LocalDate date = meal.getDateTime().toLocalDate();
            caloriesByDay.put(date, caloriesByDay.merge(date, meal.getCalories(), Integer::sum));
        }

        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
        for (UserMeal meal : meals) {
            LocalTime timeOfMeal = meal.getDateTime().toLocalTime();
            if (TimeUtil.isBetweenHalfOpen(timeOfMeal, startTime, endTime)) {
                userMealWithExcesses.add(new UserMealWithExcess(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        caloriesByDay.get(meal.getDateTime().toLocalDate()) > caloriesPerDay
                ));
            }
        }
        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime,
                                                             LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dayCalories = meals.stream()
                .collect(Collectors.groupingBy(
                        meal -> meal.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)
                ));

        return meals.stream()
                .filter(meal -> {
                    LocalTime timeOfMeal = meal.getDateTime().toLocalTime();
                    return TimeUtil.isBetweenHalfOpen(timeOfMeal, startTime, endTime);
                })
                .map(meal -> {
                    LocalDate localDate = meal.getDateTime().toLocalDate();

                    return new UserMealWithExcess(
                            meal.getDateTime(),
                            meal.getDescription(),
                            meal.getCalories(),
                            dayCalories.get(localDate) > caloriesPerDay);
                })
                .collect(Collectors.toList());
    }
}