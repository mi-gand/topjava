package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.meal.MealRestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcMealRepository implements MealRepository {
    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);
    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertMeal;
    private static final Logger log = LoggerFactory.getLogger(JdbcMealRepository.class);

    @Autowired
    public JdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.insertMeal = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Meal save(Meal meal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("dateTime", meal.getDateTime())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories())
                .addValue("user_id", userId);


        if (meal.isNew()) {
            Number newKey = insertMeal.executeAndReturnKey(map);
            meal.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE meals SET id=:id, date_time=:dateTime, description=:description, " +
                        "calories=:calories, user_id=:user_id WHERE id=:id", map) == 0) {
            return null;
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE id=? AND user_id=?", id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE id=? AND user_id=?",ROW_MAPPER, id, userId)
                .stream().findFirst().orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return new ArrayList<>(jdbcTemplate.query("SELECT id, date_time AS dateTime, description, " +
                "calories, user_id AS userId FROM meals WHERE user_id=? ORDER BY date_time DESC", ROW_MAPPER, userId));
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return new ArrayList<>(jdbcTemplate.query("SELECT * FROM meals WHERE id=? " +
                        "AND date_time > ? AND date_time < ?  ORDER BY date_time DESC",
                ROW_MAPPER, userId, startDateTime, endDateTime));
    }
}
