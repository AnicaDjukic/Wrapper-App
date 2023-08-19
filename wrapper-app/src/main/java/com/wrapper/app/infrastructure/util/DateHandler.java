package com.wrapper.app.infrastructure.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class DateHandler {

    public static Date getLocalDate() {
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
