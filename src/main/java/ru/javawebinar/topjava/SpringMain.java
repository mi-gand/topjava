package ru.javawebinar.topjava;

import static org.mockito.Mockito.*;

import org.mockito.MockedStatic;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.util.Arrays;

public class SpringMain {
    static MockedStatic<SecurityUtil> mockSecurityUtil = mockStatic(SecurityUtil.class);

    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            //System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "adminName", "emailadmin@mail.ru", "password", Role.ADMIN));
            adminUserController.create(new User(null, "simpleName", "emailsimple@mail.ru", "password", Role.USER));

            InMemoryMealRepository inMemoryMealRepository = appCtx.getBean(InMemoryMealRepository.class);
            System.out.println("User 1");
            inMemoryMealRepository.getAll(1).forEach(System.out::println);
            System.out.println("User 2");
            inMemoryMealRepository.getAll(2).forEach(System.out::println);

            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            System.out.println("Меняю User id на: " + changeUserId(1));
            mealRestController.getAll().forEach(System.out::println);
            System.out.println("Меняю User id на: " + changeUserId(2));
            mealRestController.getAll().forEach(System.out::println);
            System.out.println("Меняю User id на: " + changeUserId(1));
            mealRestController.getAll().forEach(System.out::println);

        }
    }

    static int changeUserId(int newUserId){
        mockSecurityUtil.when(SecurityUtil::authUserId).thenReturn(newUserId);
        return newUserId;
    }
}
