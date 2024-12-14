package attendance.controller;

import attendance.AttendanceStatus;
import attendance.Crew;
import attendance.CsvFileReader;
import attendance.FileConverter;
import attendance.Status;
import attendance.view.InputView;
import attendance.view.OutputView;
import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.DateTimes;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AttendanceController {

    private final FileConverter fileConverter = new FileConverter();
    private final InputView inputView;
    private final OutputView outputView;

    public AttendanceController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void start() {
        Map<String, List<AttendanceStatus>> attendanceRecord = init();

        LocalDateTime now = DateTimes.now();
        while (true) {
            String dayOfWeek = now.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);
            outputView.welcomeMessage(now);

            String input = inputView.inputChoice();

            System.out.println();
            if (input.equals("Q")) {
                break;
            }
        }
    }

    private Map<String, List<AttendanceStatus>> init() {
        CsvFileReader csv = new CsvFileReader();
        List<String> attendances = csv.read("attendances");

        List<String> names = fileConverter.getNames(attendances);
        Map<String, List<AttendanceStatus>> attendanceRecord = new HashMap<>();
        for (String name : names) {
            attendanceRecord.put(name, new ArrayList<>());
        }

        attendances.stream()
                .skip(1)
                .map(rawData -> rawData.split(","))
                .forEach(nameAndDate -> attendanceRecord.get(nameAndDate[0]).add(attendanceStatusConvert(nameAndDate)));
        return attendanceRecord;
    }

    private int findAttendanceIndex(AttendanceStatus attendanceStatus) {
        if (attendanceStatus.getStatus() == Status.ATTENDANCE) {
            return 0;
        }

        if (attendanceStatus.getStatus() == Status.TARDY) {
            return 1;
        }

        return 2;
    }

    private boolean isDayOff(int day) {
        LocalDate localDate = LocalDate.of(2024, 12, day);
        return (localDate.getDayOfWeek().getValue() == 6 || localDate.getDayOfWeek().getValue() == 7);
    }

    private AttendanceStatus makeAS(String name, LocalDateTime now, LocalTime time) {
        LocalTime standardTime = LocalTime.of(10, 0);

        Duration between = Duration.between(standardTime, time);
        System.out.println(between.getSeconds());

        if (between.getSeconds() > 1800) {
            return new AttendanceStatus(name, now, Status.ABSENCE);
        }

        if (between.getSeconds() > 300) {
            return new AttendanceStatus(name, now, Status.TARDY);
        }

        return new AttendanceStatus(name, now, Status.ATTENDANCE);
    }

    private AttendanceStatus attendanceStatusConvert(String[] rawData) {
        String name = rawData[0];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime date = LocalDateTime.parse(rawData[1], formatter);

        LocalTime attendanceTime = date.toLocalTime();
        LocalTime time = LocalTime.of(10, 0);

        Duration between = Duration.between(time, attendanceTime);
        System.out.println(between.getSeconds());

        if (between.getSeconds() > 1800) {
            return new AttendanceStatus(name, date, Status.ABSENCE);
        }

        if (between.getSeconds() > 300) {
            return new AttendanceStatus(name, date, Status.TARDY);
        }

        return new AttendanceStatus(name, date, Status.ATTENDANCE);
    }
}
