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
        log.debug("in doGet servlet");

        String method = req.getParameter("methodSelect");
        if(method == null){
            req.setAttribute("mealsList", MealsUtil.filteredByStreams(new ArrayList<>(mealRepository.getAll()),
                    null, null, CALORIES_PER_DAY));
            req.getRequestDispatcher("meals.jsp").forward(req, resp);
        }else if(method != null &&
                method.equalsIgnoreCase("createForm")){
            resp.sendRedirect("mealEdit.jsp");
        }else if(method != null &&
                method.equalsIgnoreCase("updateForm")){
            req.getRequestDispatcher("mealEdit.jsp").forward(req, resp);
        }else{
            throw new UnsupportedOperationException("something wrong");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        log.debug("in doPost servlet");

        String method = req.getParameter("methodSelect");
        if ("delete".equalsIgnoreCase(method)) {
            log.debug("in doPost - delete meal");

            String idParam = req.getParameter("id");
            if (idParam != null) {
                mealRepository.delete(Integer.parseInt(idParam));
                resp.sendRedirect("meals");
            } else {
                throw new UnsupportedOperationException("something wrong");
            }
        } else if ("update".equalsIgnoreCase(method)) {
            log.debug("in doPost-update meal");

            LocalDateTime localDateTime = LocalDateTime.parse(req.getParameter("dateTime"));
            String description = req.getParameter("description");
            int calories = Integer.parseInt(req.getParameter("calories"));
            Integer id = req.getParameter("id").isEmpty() ? null : Integer.parseInt(req.getParameter("id"));
            mealRepository.save(new Meal(id, localDateTime, description, calories));
            resp.sendRedirect("meals");
        }else{
            throw new UnsupportedOperationException("something wrong");
        }
    }
}
