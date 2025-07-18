package org.rl.apiService.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.BiPredicate;

public class Comparators {

    public static BiPredicate<LocalDateTime, LocalDateTime> areEqual() {
        return (a, b) -> ChronoUnit.MILLIS.between(a, b) < 1000;
    }
}
