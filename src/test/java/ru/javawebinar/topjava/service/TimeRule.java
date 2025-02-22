package ru.javawebinar.topjava.service;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeRule implements TestRule {
    private static final Logger log = LoggerFactory.getLogger(TimeRule.class);
    @Override
    public Statement apply(Statement test, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long startTime = System.currentTimeMillis();
                try{
                    test.evaluate();
                }finally {
                    long endTime = System.currentTimeMillis();
                    String testName = description.getMethodName();
                    log.info("Evaluation time of test \"{}\"  {} {}",testName, (endTime - startTime), "ms");
                }
            }
        };
    }
}
