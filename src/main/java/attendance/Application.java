package attendance;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        CsvFileReader csv = new CsvFileReader();
        List<String> attendances = csv.read("attendances");
    }
}
