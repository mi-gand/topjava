package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = {
        "classpath:db/initDB.sql",
        "classpath:db/MealTestData.sql"},
        config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal actualMeal = service.get(FIRST_MEAL_ID, USER_ID);
        assertThat(actualMeal).usingRecursiveComparison().isEqualTo(firstMeal);
    }

    @Test
    public void delete() {
        service.delete(FIRST_MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.delete(FIRST_MEAL_ID, USER_ID));
    }

    @Test
    public void getBetweenInclusiveAll() {
        List<Meal> actualMeals = service.getBetweenInclusive(null, null, USER_ID);
        List<Meal> expectedMeals = allMealFirstUser();
        assertThat(actualMeals).usingRecursiveComparison().isEqualTo(expectedMeals);
    }

    @Test
    public void getBetweenInclusiveInterval() {
        List<Meal> actualMeals = service.getBetweenInclusive(
                LocalDate.of(2020, 1, 29),
                LocalDate.of(2020, 1, 30),
                USER_ID
        );
        List<Meal> expectedMeals = Arrays.asList(thirdMeal, secondMeal, firstMeal);
        assertThat(actualMeals).usingRecursiveComparison().isEqualTo(expectedMeals);
    }

    @Test
    public void outsiderMealInjectionDelete() {
        assertThrows(NotFoundException.class, () -> service.delete(FIRST_MEAL_ID, ADMIN_ID));
    }

    @Test
    public void outsiderMealInjectionGet() {
        assertThrows(NotFoundException.class, () -> service.get(FIRST_MEAL_ID, ADMIN_ID));
    }

    @Test
    public void outsiderMealInjectionUpdate() {
        assertThrows(NotFoundException.class, () -> service.update(firstMeal, ADMIN_ID));
    }

    @Test
    public void getAll() {
        List<Meal> actualMeals = service.getAll(USER_ID);
        List<Meal> expectedMeals = allMealFirstUser();
        assertThat(actualMeals).usingRecursiveComparison().isEqualTo(expectedMeals);
    }

    @Test
    public void update() {
        Meal updatedMeal = new Meal(seventhMeal.getId(), seventhMeal.getDateTime(),
                seventhMeal.getDescription(), seventhMeal.getCalories());
        service.update(updatedMeal, USER_ID);
        Meal actualMeal = service.get(SEVENTH_MEAL_ID, USER_ID);
        Meal expectedMeal = new Meal(seventhMeal.getId(), seventhMeal.getDateTime(),
                seventhMeal.getDescription(), seventhMeal.getCalories());
        assertThat(actualMeal).usingRecursiveComparison().isEqualTo(expectedMeal);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(newMealWithoutId.getDateTime(),
                newMealWithoutId.getDescription(), newMealWithoutId.getCalories());
        Meal createdMeal = service.create(newMeal, USER_ID);
        Meal actualMealFromDB = service.get(createdMeal.getId(), USER_ID);
        assertThat(actualMealFromDB).usingRecursiveComparison().isEqualTo(createdMeal);
    }
}