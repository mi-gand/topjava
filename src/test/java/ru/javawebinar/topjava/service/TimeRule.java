package ru.javawebinar.topjava.service;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TimeRule implements TestRule {
    private static final Logger log = LoggerFactory.getLogger(TimeRule.class);
    public static Map<String, Long> testsTime = new HashMap<>();

    @Override
    public Statement apply(Statement test, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long startTime = System.nanoTime();
                test.evaluate();
                long endTime = System.nanoTime();
                String testName = description.getMethodName();
                long durationTest = (endTime - startTime) / 1_000_000;
                log.info("Evaluation time of test \"{}\"  {} {}", testName, durationTest, "ms");
                testsTime.put(testName, durationTest);
            }
        };
    }
}
