package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int FIRST_MEAL_ID = START_SEQ + 3;
    public static final int SECOND_MEAL_ID = START_SEQ + 4;
    public static final int THIRD_MEAL_ID = START_SEQ + 5;
    public static final int FOURTH_MEAL_ID = START_SEQ + 6;
    public static final int FIFTH_MEAL_ID = START_SEQ + 7;
    public static final int SIXTH_MEAL_ID = START_SEQ + 8;
    public static final int SEVENTH_MEAL_ID = START_SEQ + 9;

    public static final Meal firstMeal = new Meal(FIRST_MEAL_ID,
            LocalDateTime.of(2020, 1, 29, 10, 0),
            "Завтрак тестовый", 500);

    public static final Meal secondMeal = new Meal(SECOND_MEAL_ID,
            LocalDateTime.of(2020, 1, 29, 13, 0),
            "Обед тестовый", 1000);

    public static final Meal thirdMeal = new Meal(THIRD_MEAL_ID,
            LocalDateTime.of(2020, 1, 29, 20, 0),
            "Ужин тестовый", 500);

    public static final Meal fourthMeal = new Meal(FOURTH_MEAL_ID,
            LocalDateTime.of(2020, 1, 31, 0, 0),
            "Еда на граничное значение тестовый", 100);

    public static final Meal fifthMeal = new Meal(FIFTH_MEAL_ID,
            LocalDateTime.of(2020, 1, 31, 10, 0),
            "Завтрак тестовый", 1000);

    public static final Meal sixthMeal = new Meal(SIXTH_MEAL_ID,
            LocalDateTime.of(2020, 1, 31, 13, 0),
            "Обед тестовый", 500);

    public static final Meal seventhMeal = new Meal(SEVENTH_MEAL_ID,
            LocalDateTime.of(2020, 1, 31, 20, 0),
            "Ужин тестовый", 410);

    public static final Meal newMealWithoutId = new Meal(LocalDateTime.of(2024, 11, 30, 10, 0),
            "Тест", 500);


    public static List<Meal> allMealFirstUser() {
        return new ArrayList<>(Arrays.asList(
                seventhMeal, sixthMeal, fifthMeal, fourthMeal, thirdMeal, secondMeal, firstMeal
        ));
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(List<Meal> actual, List<Meal> expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

}