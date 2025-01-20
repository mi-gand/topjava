package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;


@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    private MealService service;

    public List<Meal> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll");
        return new ArrayList<>(service.getAll(userId));
    }

    public List<Meal> getAllFiltered(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        log.info("getAllFiltered");

        List<Meal> allResult = service.getAll(userId);

        LocalDate nullStartDate = (startDate == null) ? LocalDate.MIN : startDate;
        LocalTime nullStartTime = (startTime == null) ? LocalTime.MIN : startTime;
        LocalDate nullEndDate = (endDate == null) ? LocalDate.MAX : endDate;
        LocalTime nullEndTime = (endTime == null) ? LocalTime.MAX : endTime;

        return allResult.stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpenTime(meal.getTime(), nullStartTime, nullEndTime))
                .filter(meal -> DateTimeUtil.isBetweenCloseDate(meal.getDate(), nullStartDate, nullEndDate))
                .collect(Collectors.toList());
    }

    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get {}", id);
        return service.get(id, userId);
    }

    public Meal create(Meal meal) {
        int userId = SecurityUtil.authUserId();
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, userId);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete {}", id);
        service.delete(id, userId);
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        int userId = SecurityUtil.authUserId();
        service.update(meal, userId);
    }
}