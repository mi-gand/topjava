package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration("classpath:spring-test-jdbc.xml")
@RunWith(SpringRunner.class)
@Sql(scripts = {"classpath:db/initDB.sql", "classpath:db/MealTestData.sql"}, config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal mealFromDB = service.get(MEAL_ID_FIRST_VALUE, USER_ID);
        assertTrue(isEqualsMeals(FIRST_MEAL, mealFromDB));
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID_FIRST_VALUE, USER_ID);
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID_FIRST_VALUE, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> allMealsFromDB = service.getBetweenInclusive(null, null, USER_ID);
        List<Meal> testMealList = allMealFirstUser();
        assertTrue(listsAreEquals(testMealList, allMealsFromDB));

        List<Meal> targetMealsFromDB = service.getBetweenInclusive(
                LocalDate.of(2020, 1, 29),
                LocalDate.of(2020, 1, 30),
                USER_ID
        );

        List<Meal> targetTestMealList = Arrays.asList(THIRD_MEAL, SECOND_MEAL, FIRST_MEAL);
        assertTrue(listsAreEquals(targetTestMealList, targetMealsFromDB));
    }

    @Test
    public void outsiderMealInjectionDelete() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID_FIRST_VALUE, ADMIN_ID));
    }

    @Test
    public void outsiderMealInjectionGet() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID_FIRST_VALUE, ADMIN_ID));
    }

    @Test
    public void outsiderMealInjectionUpdate() {
        assertThrows(NotFoundException.class, () -> service.update(FIRST_MEAL, ADMIN_ID));
    }

    @Test
    public void getAll() {
        List<Meal> mealsFromDB = service.getAll(USER_ID);
        List<Meal> testMealList = allMealFirstUser();
        assertTrue(listsAreEquals(testMealList, mealsFromDB));
    }

    @Test
    public void update() {
        Meal updatedMeal = SEVENTH_MEAL;
        updatedMeal.setCalories(777);
        service.update(SEVENTH_MEAL, USER_ID);
        Meal mealFromDB = service.get(MEAL_ID_SEVENTH_VALUE, USER_ID);
        assertTrue(isEqualsMeals(updatedMeal, mealFromDB));
    }

    @Test
    public void create() {
        Meal mealFromDB = service.create(NEW_MEAL_WITHOUT_ID, USER_ID);
        NEW_MEAL_WITHOUT_ID.setId(MEAL_ID_NEW_VALUE);
        assertTrue(isEqualsMeals(NEW_MEAL_WITHOUT_ID, mealFromDB));
    }
}