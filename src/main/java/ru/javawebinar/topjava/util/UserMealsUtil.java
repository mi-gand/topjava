package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {

    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                        "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
                        "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0),
                        "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0),
                        "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0),
                        "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0),
                        "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0),
                        "Ужин", 410)
        );

        //до начала работы методов происходит сортировка по дате
        Collections.sort(meals);

        System.out.println("Значения filteredByCycles");
        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(0, 0), LocalTime.of(
                13, 10), 2000);
        mealsTo.forEach(System.out::println);
        System.out.println("Значения filteredByStreams");
        filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(
                13, 10), 2000).forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesPerDay) {
        // предполагается что List<UserMeal> meals пришли отсортированными по дате
        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();

        // создаю мапу по дням с превышениями по калориям
        Map<LocalDate, Boolean> daysExcessesMap = checkDaysExcesses(meals, caloriesPerDay);

        for (UserMeal meal : meals) {
            LocalTime timeOfMeal = meal.getDateTime().toLocalTime();
            // делаю в условии полуоткрытый интервал времени [,)
            if (!startTime.isAfter(timeOfMeal) && endTime.isAfter(timeOfMeal)) {
                userMealWithExcesses.add(new UserMealWithExcess(meal.getDateTime(),
                        meal.getDescription(), meal.getCalories(), daysExcessesMap.getOrDefault(
                                meal.getDateTime().toLocalDate(),false)));
            }
        }
        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime,
                                                             LocalTime endTime, int caloriesPerDay) {
        // предполагается что List<UserMeal> meals пришли отсортированными по дате
        // подсчет калорий по дням
        Map<LocalDate, Integer> dayCalories = meals.stream()
                .collect(Collectors.groupingBy(
                        meal -> meal.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)
                ));

        // преобразую в мапу с превышениями калорий по дням
        Map<LocalDate, Boolean> daysExcessesMap = dayCalories.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue() <= caloriesPerDay
                ));

        // фильтрую и преобразую Entity в Transfer Object
        return meals.stream()
                .filter(meal -> {
                    LocalTime timeOfMeal = meal.getDateTime().toLocalTime();
                    return !startTime.isAfter(timeOfMeal) && endTime.isAfter(timeOfMeal);
                })
                .map(meal -> new UserMealWithExcess(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        daysExcessesMap.getOrDefault(meal.getDateTime().toLocalDate(), false)
                ))
                .collect(Collectors.toList());
    }

    private static Map<LocalDate, Boolean> checkDaysExcesses(List<UserMeal> userMeal, int caloriesPerDay) {
        // предполагается что List<UserMeal> meals пришли отсортированными по дате
        Map<LocalDate, Boolean> daysExcesses = new HashMap<>();
        int caloriesCounter = 0;
        LocalDate day = LocalDate.MIN;
        for (UserMeal meal : userMeal) {
            if (meal.getDateTime().toLocalDate().isAfter(day)) {
                caloriesCounter = 0;
                day = meal.getDateTime().toLocalDate();
                caloriesCounter += meal.getCalories();
            } else if (meal.getDateTime().toLocalDate().isEqual(day)) {
                caloriesCounter += meal.getCalories();
            }
            if (caloriesPerDay < caloriesCounter) {
                daysExcesses.put(day, false);
            } else {
                daysExcesses.put(day, true);
            }
        }
        return daysExcesses;
    }
}
