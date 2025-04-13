package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
public class JspMealController extends AbstractMealController {

    protected JspMealController(MealService service) {
        super(service);
    }

    @GetMapping(value = "/meals", params = "action=create")
    public String create(Model model) {
        model.addAttribute("meal", new Meal());
        return "mealForm";
    }

    @GetMapping(value = "/meals")
    public String getUser(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping(value = "/meals", params = "action=update")
    public String updateMeal(@RequestParam int id, Model model) {
        model.addAttribute("meal", super.get(id));
        return "mealForm";
    }

    @PostMapping(value = "/meals")
    public String saveMeal(@RequestParam(required = false) Integer id,
                           @RequestParam String dateTime,
                           @RequestParam String description,
                           @RequestParam int calories,
                           Model model) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime);
        if (id == null) {
            super.create(new Meal(localDateTime, description, calories));
        } else {
            Meal meal = new Meal(id, localDateTime, description, calories);
            super.update(meal, id);
        }
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping(value = "/meals", params = "action=filter")
    public String filter(@RequestParam String startDate,
                         @RequestParam String endDate,
                         @RequestParam String startTime,
                         @RequestParam String endTime,
                         Model model) {
        LocalDate startLocalDate = startDate.isEmpty() ? null : LocalDate.parse(startDate);
        LocalDate endLocalDate = endDate.isEmpty() ? null : LocalDate.parse(endDate);
        LocalTime startLocalTime = startTime.isEmpty() ? null : LocalTime.parse(startTime);
        LocalTime endLocalTime = endTime.isEmpty() ? null : LocalTime.parse(endTime);

        List<MealTo> mealTos = super.getBetween(startLocalDate, startLocalTime, endLocalDate, endLocalTime);
        model.addAttribute("meals", mealTos);
        return "meals";
    }
}