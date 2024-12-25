package ru.javawebinar.topjava;

import ru.javawebinar.topjava.util.TimeUtil;

import java.time.LocalDateTime;

/**
 * @see <a href="https://javaops-demo.ru/topjava">Demo application</a>
 * @see <a href="https://github.com/JavaOPs/topjava">Initial project</a>
 */
public class Main {
    public static void main(String[] args) {
        System.out.format("Hello TopJava Enterprise!");
        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        System.out.println("\nLocalDateTime now:" + nowLocalDateTime);
        System.out.println("Date now:" + TimeUtil.formatDate(nowLocalDateTime));

    }
}
