package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repositories.RamMealRepository;
import ru.javawebinar.topjava.repositories.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    public static final Integer CALORIES_PER_DAY = 2000;
    private MealRepository mealRepository;
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    public void init() throws ServletException {
        super.init();
        this.mealRepository = new RamMealRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("show meals list");

        req.setAttribute("mealsList", MealsUtil.filteredByStreams(new ArrayList<>(mealRepository.getAll().values()),
                null, null, CALORIES_PER_DAY));

        mealRepository.getAll().values().forEach(m -> log.debug(LocalTime.now() + ".Meal: " + m));
        req.getRequestDispatcher("meals.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");

        log.debug("in doPost servlet");
        String method = req.getParameter("methodSelect");

        if ("delete".equalsIgnoreCase(method)) {
            doDelete(req, resp);
        } else if ("createOrUpdate".equalsIgnoreCase(method)) {
            if (req.getParameter("id").equalsIgnoreCase("newMeal")) {
                log.debug("in doPost-create new meal");
                resp.sendRedirect("mealEdit.jsp");
            } else {
                log.debug("in doPost-update meal");
                LocalDateTime localDateTime = LocalDateTime.parse(req.getParameter("dateTime"));
                String description = req.getParameter("description");
                int calories = Integer.parseInt(req.getParameter("calories"));
                Integer id = req.getParameter("id").isEmpty() ? null : Integer.parseInt(req.getParameter("id"));
                mealRepository.save(new Meal(id, localDateTime, description, calories));
                resp.sendRedirect("meals");
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");

        log.debug("in doDelete servlet");
        String idParam = req.getParameter("id");

        if (idParam != null) {
            boolean isDeleted = mealRepository.delete(Integer.parseInt(idParam));
            if (isDeleted) {
                resp.setStatus(204);
                resp.sendRedirect("meals");
            } else {
                resp.setStatus(404);
            }
        } else {
            resp.setStatus(400);
        }
    }
}
