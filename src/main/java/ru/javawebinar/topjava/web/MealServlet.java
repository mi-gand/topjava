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
import java.util.ArrayList;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    public static final Integer CALORIES_PER_DAY = 2000;
    private static final Logger log = getLogger(MealServlet.class);
    private MealRepository mealRepository;

    @Override
    public void init() {
        this.mealRepository = new RamMealRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("1.0: in doGet servlet");

        String method = req.getParameter("methodSelect");
        if (method == null) {
            req.getRequestDispatcher("meals.jsp").forward(prepareMealsForView(req), resp);
        } else if (method.equalsIgnoreCase("createForm")) {
            log.debug("1.2: in doGet - redirect to the form for creating a new meal");
            req.getRequestDispatcher("mealEdit.jsp").forward(req, resp);
        } else if (method.equalsIgnoreCase("updateForm")) {
            log.debug("1.3: in doGet - forwarding with the parameters to the form for updating the meal");
            Meal mealToUpdate = mealRepository.read(Integer.parseInt(req.getParameter("id")));
            req.setAttribute("meal", mealToUpdate);
            req.getRequestDispatcher("mealEdit.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("meals.jsp").forward(prepareMealsForView(req), resp);
        }
    }

    private HttpServletRequest prepareMealsForView(HttpServletRequest req){
        log.debug("1.1: in doGet - show meals list");
        req.setAttribute("mealsList", MealsUtil.filteredByStreams(new ArrayList<>(mealRepository.getAll()),
                null, null, CALORIES_PER_DAY));
        return req;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        log.debug("2.0: in doPost servlet");

        String method = req.getParameter("methodSelect");
        if ("delete".equalsIgnoreCase(method)) {
            log.debug("2.1: in doPost - delete meal");

            String idParam = req.getParameter("id");
            if (idParam != null) {
                mealRepository.delete(Integer.parseInt(idParam));
                resp.sendRedirect("meals");
            } else {
                throw new UnsupportedOperationException("something wrong");
            }
        } else if ("update".equalsIgnoreCase(method)) {
            log.debug("2.2: in doPost-update meal");

            LocalDateTime localDateTime = LocalDateTime.parse(req.getParameter("dateTime"));
            String description = req.getParameter("description");
            int calories = Integer.parseInt(req.getParameter("calories"));
            Integer id = req.getParameter("id").isEmpty() ? null : Integer.parseInt(req.getParameter("id"));
            mealRepository.save(new Meal(id, localDateTime, description, calories));
            resp.sendRedirect("meals");
        } else {
            throw new UnsupportedOperationException("something wrong");
        }
    }
}
