package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.Profiles.HSQL_DB;

@Repository
@Profile(HSQL_DB)
public class HsqlJdbcMealRepository extends JdbcMealRepository{

    @Autowired
    public HsqlJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId){
        Timestamp mappedStartDateTime = Timestamp.valueOf(startDateTime);
        Timestamp mappedEndDateTime = Timestamp.valueOf(endDateTime);
        return jdbcTemplate.query(
                "SELECT * FROM meal WHERE user_id=?  AND date_time >=  ? AND date_time < ? ORDER BY date_time DESC",
                rowMapper, userId, mappedStartDateTime, mappedEndDateTime);
    }

    protected Object actualDateFoDb(LocalDateTime localDateTime){
        return Timestamp.valueOf(localDateTime);
    }
}
