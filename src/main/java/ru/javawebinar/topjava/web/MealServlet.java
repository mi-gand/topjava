package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repositories.DataRepository;
import ru.javawebinar.topjava.repositories.MealRepository;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    public final static Integer caloriesPerDay = 2000;
    private static final MealRepository mealRepository = DataRepository.getInstance();
    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        log.debug("show meals list");
        req.setAttribute("timeUtil", new TimeUtil());

        Map<LocalDate, Integer> caloriesSumByDate = mealRepository.getAll().stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
                );

        List<MealTo> mealTo = mealRepository.getAll().stream()
                .map(meal -> new MealTo(meal.getId(), meal.getDateTime(),meal.getDescription(), meal.getCalories(),
                        caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());

        req.setAttribute("mealsList", mealTo);
        req.getRequestDispatcher("meals.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        log.debug("in doPost servlet");
        String method = req.getParameter("methodSelect");

        if ("delete".equalsIgnoreCase(method)) {
            doDelete(req, resp);
        } else if("createOrUpdate".equalsIgnoreCase(method)) {
            LocalDateTime localDateTime = LocalDateTime.parse(req.getParameter("dateTime"));
            String description = req.getParameter("description");
            int calories = Integer.parseInt(req.getParameter("calories"));
            if(req.getParameter("id") == null){
                mealRepository.create(new Meal(localDateTime, description, calories));
                resp.sendRedirect("meals");
            }else {
                int id = Integer.parseInt(req.getParameter("id"));
                mealRepository.update(id, localDateTime, description, calories);
                resp.sendRedirect("meals");
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        log.debug("in doDelete servlet");
        String idParam = req.getParameter("id");

        if(idParam != null){
            boolean isDeleted = mealRepository.delete(Integer.parseInt(idParam));
            if(isDeleted){
                resp.setStatus(204);
                resp.sendRedirect("meals");
            }else{
                resp.setStatus(404);
            }
        }else{
            resp.setStatus(400);
        }
    }
}
