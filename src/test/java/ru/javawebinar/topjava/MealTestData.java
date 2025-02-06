package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public final static int MEAL_ID_FIRST_VALUE = START_SEQ + 3;
    public final static int MEAL_ID_SECOND_VALUE = START_SEQ + 4;
    public final static int MEAL_ID_THIRD_VALUE = START_SEQ + 5;
    public final static int MEAL_ID_FOURTH_VALUE = START_SEQ + 6;
    public final static int MEAL_ID_FIFTH_VALUE = START_SEQ + 7;
    public final static int MEAL_ID_SIXTH_VALUE = START_SEQ + 8;
    public final static int MEAL_ID_SEVENTH_VALUE = START_SEQ + 9;
    public final static int MEAL_ID_FIRST_VALUE_SECOND_USER = START_SEQ + 10;
    public final static int MEAL_ID_NEW_VALUE = START_SEQ + 17;

    public final static Meal NEW_MEAL_WITHOUT_ID = new Meal(LocalDateTime.of(2024, 11, 30, 10, 0),
            "Тест", 500);

    public final static Meal FIRST_MEAL = new Meal(MEAL_ID_FIRST_VALUE,
            LocalDateTime.of(2020, 1, 29, 10, 0),
            "Завтрак тестовый", 500);

    public final static Meal SECOND_MEAL = new Meal(MEAL_ID_SECOND_VALUE,
            LocalDateTime.of(2020, 1, 29, 13, 0),
            "Обед тестовый", 1000);

    public final static Meal THIRD_MEAL = new Meal(MEAL_ID_THIRD_VALUE,
            LocalDateTime.of(2020, 1, 29, 20, 0),
            "Ужин тестовый", 500);

    public final static Meal FOURTH_MEAL = new Meal(MEAL_ID_FOURTH_VALUE,
            LocalDateTime.of(2020, 1, 31, 0, 0),
            "Еда на граничное значение тестовый", 100);

    public final static Meal FIFTH_MEAL = new Meal(MEAL_ID_FIFTH_VALUE,
            LocalDateTime.of(2020, 1, 31, 10, 0),
            "Завтрак тестовый", 1000);

    public final static Meal SIXTH_MEAL = new Meal(MEAL_ID_SIXTH_VALUE,
            LocalDateTime.of(2020, 1, 31, 13, 0),
            "Обед тестовый", 500);

    public final static Meal SEVENTH_MEAL = new Meal(MEAL_ID_SEVENTH_VALUE,
            LocalDateTime.of(2020, 1, 31, 20, 0),
            "Ужин тестовый", 410);
    public final static Meal FIRST_MEAL_SECOND_USER = new Meal(MEAL_ID_FIRST_VALUE_SECOND_USER,
            LocalDateTime.of(2020, 3, 30, 10, 0),
            "Завтрак второго тестовый", 500);


    public static boolean isEqualsMeals(Meal sample, Meal fromDB){
        return sample.getId().equals(fromDB.getId()) &&
                sample.getDateTime().equals(fromDB.getDateTime()) &&
                sample.getDescription().equals(fromDB.getDescription()) &&
                sample.getCalories() == fromDB.getCalories();
    }

    public static boolean listsAreEquals(List<Meal> sample, List<Meal> fromDB){
        if(sample.size()!= fromDB.size()) return false;
        for (int i = 0; i < sample.size(); i++) {
            Meal s = sample.get(i);
            Meal f = fromDB.get(i);
            if(!isEqualsMeals(s, f)) return false;
        }
        return true;
    }

    public static List<Meal> allMealFirstUser() {
        return new ArrayList<>(Arrays.asList(
                SEVENTH_MEAL, SIXTH_MEAL, FIFTH_MEAL, FOURTH_MEAL, THIRD_MEAL, SECOND_MEAL, FIRST_MEAL
        ));
    }


}
