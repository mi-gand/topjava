package ru.javawebinar.topjava.repositories;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class DataRepository implements MealRepository {

    private static volatile DataRepository instance;

    public static final List<Meal> meals = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

    private List<Meal> localRepository = List.copyOf(meals);

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
    public void update(int id) {

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
