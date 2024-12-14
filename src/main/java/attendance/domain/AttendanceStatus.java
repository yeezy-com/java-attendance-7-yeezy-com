package attendance.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AttendanceStatus {

    private final String name;
    private LocalDateTime date;
    private Status status;

    public AttendanceStatus(String name, LocalDateTime date, Status status) {
        this.name = name;
        this.date = date;
        this.status = status;
    }

    public boolean isSameDate(LocalDate now) {
        LocalDate originalDate = date.toLocalDate();

        return originalDate.isEqual(now);
    }

    public boolean isSameDate(final int day) {
        LocalDate originalDate = date.toLocalDate();
        LocalDate originalNowDate = LocalDate.of(2024, 12, day);

        return originalDate.equals(originalNowDate);
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isDayOff() {
        if (date.getDayOfWeek().getValue() == 6 || date.getDayOfWeek().getValue() == 7) {
            return true;
        }
        return false;
    }

    public void updateTime(LocalTime time) {
        date = LocalDateTime.of(date.toLocalDate(), time);
        LocalTime standardTime = LocalTime.of(10, 0);
        if (date.getDayOfWeek().getValue() == 1) {
            time = LocalTime.of(13, 0);
        }

        Duration between = Duration.between(standardTime, time);

        if (between.getSeconds() > 1800) {
            this.status = Status.ABSENCE;
            return;
        }

        if (between.getSeconds() > 300) {
            this.status = Status.TARDY;
            return;
        }

        this.status = Status.ATTENDANCE;
    }
}
