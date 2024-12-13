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

    public static void main(String[] args) throws InterruptedException {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                        "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0),
                        "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0),
                        "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0),
                        "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0),
                        "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
                        "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0),
                        "Ужин", 410)
        );

        System.out.println("Значения filteredByCycles");
        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(0, 0), LocalTime.of(
                13, 10), 2000);
        mealsTo.forEach(System.out::println);
        System.out.println("Значения filteredByStreams");
        filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(
                13, 10), 2000).forEach(System.out::println);
    }

    private UserMealsUtil() {
        throw new UnsupportedOperationException("Util class");
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Boolean> daysExcessesMap = checkDaysExcesses(meals, caloriesPerDay);

        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();

        for (UserMeal meal : meals) {
            LocalTime timeOfMeal = meal.getDateTime().toLocalTime();
            if (TimeUtil.isBetweenHalfOpen(timeOfMeal, startTime, endTime)) {
                LocalDate localDate = meal.getDateTime().toLocalDate();
                Boolean isExcess = daysExcessesMap.get(localDate);

                if (isExcess == null) {
                    throw new IllegalStateException("Illegal state");
                }

                userMealWithExcesses.add(new UserMealWithExcess(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        isExcess));
            }
        }
        return userMealWithExcesses;
    }

    private static Map<LocalDate, Boolean> checkDaysExcesses(List<UserMeal> userMeal, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDay = new HashMap<>();
        for (UserMeal meal : userMeal) {
            LocalDate date = meal.getDateTime().toLocalDate();
            caloriesByDay.put(date, caloriesByDay.getOrDefault(date, -1) + meal.getCalories());
        }

        Map<LocalDate, Boolean> daysExcesses = new HashMap<>();
        for (Map.Entry<LocalDate, Integer> entry : caloriesByDay.entrySet()) {
            daysExcesses.put(entry.getKey(), entry.getValue() > caloriesPerDay);
        }
        return daysExcesses;
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
                    Boolean isExcess = dayCalories.getOrDefault(localDate, -1) > caloriesPerDay;

                    return new UserMealWithExcess(
                            meal.getDateTime(),
                            meal.getDescription(),
                            meal.getCalories(),
                            isExcess);
                })
                .collect(Collectors.toList());
    }
}
