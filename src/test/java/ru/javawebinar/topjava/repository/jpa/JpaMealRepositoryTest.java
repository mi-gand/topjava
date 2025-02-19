package ru.javawebinar.topjava.repository.jpa;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MatcherFactory;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

@ContextConfiguration({
        "classpath:spring/jpaDB.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = {"classpath:db/populateDB.sql"}, config = @SqlConfig(encoding = "UTF-8"))
public class JpaMealRepositoryTest {
    public static final MatcherFactory.Matcher<Meal> MEAL_MATCHER = MatcherFactory.usingIgnoringFieldsComparator();

    @Autowired
    MealRepository mealRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    public void create() {
        User user = userRepository.get(USER_ID);
        Meal mealToDB = MealTestData.getNew();
        mealToDB.setUser(user);
        Meal actualMeal = mealRepository.save(mealToDB, USER_ID);
        Meal expectedMeal = MealTestData.getNew();
        expectedMeal.setId(START_SEQ + 12);
        expectedMeal.setUser(user);
        MEAL_MATCHER.assertMatch(actualMeal, expectedMeal);
    }

    @Test
    public void update() {
        User user = userRepository.get(USER_ID);
        Meal mealToDB = meal1;
        mealToDB.setUser(user);
        Meal actualMeal = mealRepository.save(mealToDB, USER_ID);
        Meal expectedMeal = meal1;
        expectedMeal.setUser(user);
        MEAL_MATCHER.assertMatch(actualMeal, expectedMeal);
    }

    @Test
    public void delete() {
        Assert.assertTrue(mealRepository.delete(MEAL1_ID, USER_ID));
    }

    @Test
    public void get() {
        User user = userRepository.get(USER_ID);
        Meal expectedMeal = meal1;
        expectedMeal.setUser(user);
        Meal actualMeal = mealRepository.get(MEAL1_ID, meal1.getUser().getId());
        MEAL_MATCHER.assertMatch(actualMeal, expectedMeal);
    }

    @Test
    public void getAll() {
        List<Meal> actualMeal = mealRepository.getAll(USER_ID);
        List<Meal> expectedMeal = MealTestData.meals;
        User user = userRepository.get(USER_ID);
        expectedMeal.forEach(u -> u.setUser(user));
        MEAL_MATCHER.assertMatch(actualMeal, expectedMeal);
    }

    @Test
    public void getBetweenHalfOpen() {
        List<Meal> actualMeals = mealRepository.getBetweenHalfOpen(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0),
                USER_ID);

        List<Meal> expectedMeals = Arrays.asList(meal3, meal2, meal1);
        User user = userRepository.get(USER_ID);
        expectedMeals.forEach(m -> m.setUser(user));
        MEAL_MATCHER.assertMatch(actualMeals, expectedMeals);
    }
}