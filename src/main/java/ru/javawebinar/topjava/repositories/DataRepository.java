package ru.javawebinar.topjava.repositories;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DataRepository implements MealRepository {

    private static DataRepository instance;
    private static Integer counterId = 0;



    public static final List<Meal> meals = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

    private final List<Meal> localRepository = new CopyOnWriteArrayList<>(meals);

    private DataRepository(){}

    public static DataRepository getInstance(){
        if(instance == null){
            synchronized (DataRepository.class){
                if(instance == null){
                    instance = new DataRepository();
                }
            }
        }
        return instance;
    }

    private Integer incrementId(){
        return ++counterId;
    }

    @Override
    public void create(Meal meal) {
        localRepository.add(meal);
    }

    @Override
    public Meal read(int id) {
        return localRepository.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(int id, LocalDateTime dateTime, String description, int calories) {
        Meal meal = localRepository.stream().filter(m -> m.getId()==id).findFirst().orElseThrow();
        meal.setDateTime(dateTime);
        meal.setDescription(description);
        meal.setCalories(calories);
    }

    @Override
    public boolean delete(int id) {
        return localRepository.removeIf(m -> m.getId() == id);
    }

    @Override
    public List<Meal> getAll() {
        return localRepository;
    }
}
