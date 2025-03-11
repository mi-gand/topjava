package ru.javawebinar.topjava.repository.dataJPA;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

import static ru.javawebinar.topjava.Profiles.JPA;

@ActiveProfiles(JPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {
}
