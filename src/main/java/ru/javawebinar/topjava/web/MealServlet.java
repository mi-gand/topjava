package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repositories.DataRepository;
import ru.javawebinar.topjava.util.Const;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meals list");
        req.setAttribute("timeUtil", new TimeUtil());

        Map<LocalDate, Integer> caloriesSumByDate = DataRepository.meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
                );

        List<MealTo> mealTo = DataRepository.meals.stream()
                .map(meal -> new MealTo(meal.getDateTime(),meal.getDescription(), meal.getCalories(),
                        caloriesSumByDate.get(meal.getDate()) > Const.caloriesPerDay))
                .collect(Collectors.toList());

        req.setAttribute("mealsList", mealTo);
        req.getRequestDispatcher("meals.jsp").forward(req,resp);
    }

}
