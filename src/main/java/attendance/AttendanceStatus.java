package attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AttendanceStatus {

    private final String name;
    private final LocalDateTime date;
    private final Status status;

    public AttendanceStatus(String name, LocalDateTime date, Status status) {
        this.name = name;
        this.date = date;
        this.status = status;
    }

    public boolean isSameDate(LocalDateTime now) {
        LocalDate originalDate = date.toLocalDate();
        LocalDate originalNowDate = now.toLocalDate();

        return originalDate.isEqual(originalNowDate);
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
}
