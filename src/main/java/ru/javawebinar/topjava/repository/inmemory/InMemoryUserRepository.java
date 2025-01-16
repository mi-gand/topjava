package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private final Map<Integer, User> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    // false if not found
    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    // null if not found, when updated
    @Override
    public User save(User user) {
        log.info("save {}", user);
        User entityUser;
        if (user.isNew()) {
            entityUser = new User(counter.incrementAndGet(), user.getName(), user.getEmail(),
                    user.getPassword(), user.getCaloriesPerDay(), user.isEnabled(), user.getRoles());
            repository.put(counter.intValue(), entityUser);
        } else {
            if (user.getId() > counter.intValue() || repository.get(user.getId()) == null) {
                return null;
            }
            entityUser = new User(user.getId(), user.getName(), user.getEmail(),
                    user.getPassword(), user.getCaloriesPerDay(), user.isEnabled(), user.getRoles());
            repository.put(user.getId(), entityUser);
        }
        return entityUser;
    }

    // null if not found
    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return repository.values().stream()
                .sorted(Comparator.comparing(AbstractNamedEntity::getName))
                .sorted(Comparator.comparing(User::getEmail))
                .collect(Collectors.toList());
    }

    // null if not found
    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return repository.values().stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst()
                .orElse(null);
    }
}