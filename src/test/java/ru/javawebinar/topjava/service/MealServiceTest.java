package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@ContextConfiguration("classpath:spring-test-jdbc.xml")
@RunWith(SpringRunner.class)
public class MealServiceTest {
    @Autowired
    private MealRepository repository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final static int USER_ID = 100000;
    private final static int MEAL_ID_FIRST_VALUE = 100003;
    private final static int MEAL_ID_LAST_VALUE = 100009;
    private final static int MEAL_ID_NEW_VALUE = 100010;

    public static final Meal meal = new Meal(MEAL_ID_FIRST_VALUE,
            LocalDateTime.of(2020,1,30,10,0),
            "Завтрак тестовый", 500);
/*    @Sql(scripts = {"/db/initDB-test.sql", "/db/MealTestData.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)*/

    @Before
    public void init() throws IOException {
        String initDbScript = new String(Files.readAllBytes(Paths.get("src/test/resources/db/initDB-test.sql")));
        String testDataScript = new String(Files.readAllBytes(Paths.get("src/test/resources/db/MealTestData.sql")));

        jdbcTemplate.execute(initDbScript);
        jdbcTemplate.execute(testDataScript);
    }

    @Test
    public void get() {
        Meal mealFromDB = repository.get(MEAL_ID_FIRST_VALUE, USER_ID);
        assertTrue(isEqualsMeals(meal, mealFromDB));
    }

    @Test
    public void delete() {
        assertTrue(repository.delete(MEAL_ID_FIRST_VALUE, USER_ID));
        assertFalse(repository.delete(MEAL_ID_FIRST_VALUE, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {

    }

    @Test
    public void outsiderMealInjectionDelete(){

    }

    @Test
    public void outsiderMealInjectionGet(){

    }

    @Test
    public void outsiderMealInjectionUpdate(){

    }

    @Test
    public void getAll() {
        List <Meal> mealsFromDB = repository.getAll(USER_ID);
        List <Meal> testMealList = testMealList();

        assertEquals(mealsFromDB.size(), testMealList.size());

        for (int i = 0; i < testMealList.size(); i++) {
            Meal sample = testMealList.get(i);
            Meal fromDB = mealsFromDB.get(i);
            assertTrue(isEqualsMeals(sample, fromDB));
        }
    }

    @Test
    public void update() {
        Meal updatedMeal = new Meal(MEAL_ID_LAST_VALUE, LocalDateTime.of(2024, 11, 30, 10, 0),
                "Тест", 500);
        assertNotNull(repository.save(updatedMeal, USER_ID));
        Meal mealFromDB = repository.get(MEAL_ID_LAST_VALUE, USER_ID);
        assertTrue(isEqualsMeals(updatedMeal, mealFromDB));
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.of(2024, 11, 30, 10, 0),
                "Тест", 500);
        Meal mealFromDB = repository.save(newMeal, USER_ID);
        newMeal.setId(MEAL_ID_NEW_VALUE);
        assertTrue(isEqualsMeals(newMeal, mealFromDB));
    }

    private boolean isEqualsMeals(Meal sample, Meal fromDB){
        return sample.getId().equals(fromDB.getId()) &&
                sample.getDateTime().equals(fromDB.getDateTime()) &&
                sample.getDescription().equals(fromDB.getDescription()) &&
                sample.getCalories() == fromDB.getCalories();
    }

    private List<Meal> testMealList(){
        return new ArrayList<>(Arrays.asList(
                new Meal(100009, LocalDateTime.of(2020, 1, 31, 20, 0),
                        "Ужин тестовый", 410),
                new Meal(100008, LocalDateTime.of(2020, 1, 31, 13, 0),
                        "Обед тестовый", 500),
                new Meal(100007, LocalDateTime.of(2020, 1, 31, 10, 0),
                        "Завтрак тестовый", 1000),
                new Meal(100006, LocalDateTime.of(2020, 1, 31, 0, 0),
                        "Еда на граничное значение тестовый", 100),
                new Meal(100005, LocalDateTime.of(2020, 1, 30, 20, 0),
                        "Ужин тестовый", 500),
                new Meal(100004, LocalDateTime.of(2020, 1, 30, 13, 0),
                        "Обед тестовый", 1000),
                new Meal(100003, LocalDateTime.of(2020, 1, 30, 10, 0),
                        "Завтрак тестовый", 500)
        ));
    }
}