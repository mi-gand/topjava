package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class SpringMain {

    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "adminName", "emailadmin@mail.ru", "password", Role.ADMIN));
            adminUserController.create(new User(null, "simpleName", "emailsimple@mail.ru", "password", Role.USER));
            adminUserController.create(new User(null, "John Smith", "emailsimple1@mail.ru", "password", Role.USER));
            adminUserController.create(new User(null, "John Smith", "emailsimple4@mail.ru", "password", Role.USER));
            adminUserController.create(new User(null, "John Smith", "emailsimple3@mail.ru", "password", Role.USER));
            adminUserController.create(new User(null, "John Smith", "emailsimple2@mail.ru", "password", Role.USER));

            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
/*            System.out.println("вывожу список пользователей");
            List<User> users = adminUserController.getAll();
            users.forEach(System.out::println);

            InMemoryMealRepository inMemoryMealRepository = appCtx.getBean(InMemoryMealRepository.class);
            System.out.println("User 1");
            inMemoryMealRepository.getAll(1).forEach(System.out::println);
            System.out.println("User 2");
            inMemoryMealRepository.getAll(2).forEach(System.out::println);

            MealRepository mealRepository = appCtx.getBean(MealRepository.class);


            Meal updateMeal = mealRepository.get(8, 2);

            System.out.println("___________________\nТест на чужой еде \n___________________");
            System.out.println("get method: " + mealRepository.get(8, 1));
            System.out.println("update method: " + mealRepository.save(updateMeal, 1));
            System.out.println("delete method: " + mealRepository.delete(8, 1));


            System.out.println("___________________\n Тест на своей еде \n___________________");
            System.out.println("get method: " + mealRepository.get(8, 2));
            System.out.println("update method: " + mealRepository.save(updateMeal, 2));
            System.out.println("delete method: " + mealRepository.delete(8, 2));


            System.out.println("___________________\nСохранение новой еды в user2 \n___________________");
            Meal newMeal = new Meal(LocalDateTime.of(2024, Month.JANUARY, 10, 10, 1), "ЗавтракNew", 1560);
            mealRepository.save(newMeal, 2);
            inMemoryMealRepository.getAll(2).forEach(System.out::println);


            System.out.println("___________________\nВывод еды user2 \n___________________");
            inMemoryMealRepository.getAll(2).forEach(System.out::println);
            inMemoryMealRepository.getAll(1).forEach(System.out::println);
            inMemoryMealRepository.getAll(2).forEach(System.out::println);

            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            System.out.println("Меняю User id на: " + changeUserId(1));
            mealRestController.getAll().forEach(System.out::println);
            System.out.println("Меняю User id на: " + changeUserId(2));
            mealRestController.getAll().forEach(System.out::println);
            System.out.println("Меняю User id на: " + changeUserId(1));
            mealRestController.getAll().forEach(System.out::println);*/

        }
    }
}
