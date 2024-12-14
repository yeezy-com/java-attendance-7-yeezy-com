package attendance.controller;

import attendance.AttendanceStatus;
import attendance.CsvFileReader;
import attendance.ErrorMessage;
import attendance.FileConverter;
import attendance.Status;
import attendance.view.InputView;
import attendance.view.OutputView;
import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.DateTimes;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.ArrayList;
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

        LocalDate now = LocalDate.of(2024, 12, 13);
        while (true) {
            String dayOfWeek = now.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);
            outputView.printWelcomeMessage(now);

            String input = inputView.inputChoice();

            System.out.println();
            if (input.equals("Q")) {
                break;
            }

            if (input.equals("1")) { // 출석 확인
                if (now.getDayOfWeek().getValue() == 6 || now.getDayOfWeek().getValue() == 7) {
                    throw new IllegalArgumentException(String.format("[ERROR] %d월 %d일 %s은 등교일이 아닙니다.", now.getMonth().getValue(), now.getDayOfMonth(), dayOfWeek));
                }

                System.out.println("닉네임을 입력해 주세요.");
                String nickname = Console.readLine();

                validateContainsNickname(attendanceRecord, nickname);

                LocalTime enterTime = inputView.inputEnterTime();
                validateIsNotAcceptTime(enterTime);

                List<AttendanceStatus> values = attendanceRecord.get(nickname);
                AttendanceStatus attendanceStatus = values.stream()
                        .filter(value -> value.isSameDate(now))
                        .findFirst()
                        .orElse(null);

                System.out.println();
                if (attendanceStatus == null) {
                    AttendanceStatus attendance = makeAS(nickname, LocalDate.from(now), enterTime);
                    attendanceRecord.get(nickname).add(attendance);

                    String displayName = attendance.getDate().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);
                    System.out.printf("%d월 %d일 %s %s (%s)%n", attendance.getDate().getMonth().getValue(),
                            attendance.getDate().getDayOfMonth(), displayName, enterTime, attendance.getStatus().getStatus());
                    System.out.println();
                    continue;
                }

                throw new IllegalArgumentException(ErrorMessage.ALREADY_ATTENDANCE.getMsg());
            }

            if (input.equals("2")) { // 출석 수정
                String nickname = inputView.inputEditCrewNickname();
                validateContainsNickname(attendanceRecord, nickname);

                int day = Integer.parseInt(inputView.inputEditDay());
                if (day > now.getDayOfMonth()) {
                    throw new IllegalArgumentException("[ERROR] 아직 수정할 수 없습니다.");
                }
                LocalDate date = LocalDate.of(2024, 12, day);

                AttendanceStatus attendance = attendanceRecord.get(nickname).stream()
                        .filter(attendanceStatus -> attendanceStatus.isSameDate(date))
                        .findFirst()
                        .orElse(null);

                if (isDayOff(day)) {
                    String displayName = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);
                    throw new IllegalArgumentException(String.format("[ERROR] %s월 %02d일 %s은 등교일이 아닙니다.", 12, day, displayName));
                }

                System.out.println("언제로 변경하겠습니까?");
                LocalTime time;
                try {
                    time = LocalTime.parse(Console.readLine());
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.");
                }

                System.out.println();
                if (attendance == null) {
                    AttendanceStatus attendanceStatus = makeAS(nickname, LocalDate.from(now), time);
                    attendanceRecord.get(nickname).add(attendanceStatus);

                    String displayName = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);
                    System.out.printf("%d월 %d일 %s %s (%s)%n", attendanceStatus.getDate().getMonth().getValue(),
                            attendanceStatus.getDate().getDayOfMonth(), displayName, time, attendanceStatus.getStatus().getStatus());
                    System.out.println();
                    continue;
                }

                String displayName = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);
                System.out.printf("%d월 %d일 %s %s (%s) -> ", 12, attendance.getDate().getDayOfMonth(),
                        displayName, attendance.getDate().toLocalTime(), attendance.getStatus().getStatus());
                attendance.updateTime(time);
                System.out.printf("%s (%s) 수정 완료!%n", attendance.getDate().toLocalTime(), attendance.getStatus().getStatus());
                System.out.println();
                continue;
            }

            if (input.equals("3")) { // 3. 크루별 출석 기록 확인
                String nickname = inputView.inputNickName();

                validateContainsNickname(attendanceRecord, nickname);
                outputView.printNickNameAttendanceStatusIntroduceMessage(nickname);

                int[] attendanceCount = new int[3];
                for (int day = 1; day < now.getDayOfMonth(); day++) {
                    int finalDay = day;
                    AttendanceStatus attendanceStatus = attendanceRecord.get(nickname).stream()
                            .filter(value -> value.isSameDate(finalDay))
                            .findFirst()
                            .orElse(null);

                    if (isDayOff(day)) {
                        continue;
                    }

                    if (attendanceStatus == null) {
                        String displayName = LocalDate.of(2024, 12, day).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);
                        System.out.printf("%d월 %02d일 %s --:-- (결석)%n", 12, day, displayName);
                        attendanceCount[2]++;
                        continue;
                    }

                    String displayName = attendanceStatus.getDate().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);
                    attendanceCount[findAttendanceIndex(attendanceStatus)]++;
                    System.out.printf("%d월 %02d일 %s %s (%s)%n", 12, day, displayName, LocalTime.from(attendanceStatus.getDate()), attendanceStatus.getStatus().getStatus());
                }

                attendanceCount[2] += attendanceCount[1] / 3;
                attendanceCount[1] = attendanceCount[1] % 3;
                outputView.printStatus(attendanceCount);
                continue;
            }

            if (input.equals("4")) {
                Map<String, Integer> checkCount = new HashMap<>();
                attendanceRecord.keySet()
                        .forEach(name -> checkCount.put(name, 0));

                attendanceRecord.values().stream()
                        .forEach(value -> value.stream()
                                .forEach(attendanceStatus -> {
                                    if (attendanceStatus.getStatus() == Status.TARDY) {

                                    }
                                }));
            }

            throw new IllegalArgumentException(ErrorMessage.WRONG_FORM_ERROR.getMsg());
        }
    }

    private void validateIsNotAcceptTime(LocalTime enterTime) {
        if (enterTime.isBefore(LocalTime.of(8, 0)) || enterTime.isAfter(LocalTime.of(23, 0))) {
            throw new IllegalArgumentException(ErrorMessage.NOT_ACCEPT_TIME.getMsg());
        }
    }

    private void validateContainsNickname(Map<String, List<AttendanceStatus>> attendanceRecord, String nickname) {
        if (!attendanceRecord.containsKey(nickname)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_ENROLL_NICKNAME.getMsg());
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

    private AttendanceStatus makeAS(String name, LocalDate now, LocalTime time) {
        LocalTime standardTime = LocalTime.of(10, 0);
        if (now.getDayOfWeek().getValue() == 1) {
            time = LocalTime.of(13, 0);
        }

        Duration between = Duration.between(standardTime, time);

        if (between.getSeconds() > 1800) {
            return new AttendanceStatus(name, LocalDateTime.of(now, time), Status.ABSENCE);
        }

        if (between.getSeconds() > 300) {
            return new AttendanceStatus(name, LocalDateTime.of(now, time), Status.TARDY);
        }

        return new AttendanceStatus(name, LocalDateTime.of(now, time), Status.ATTENDANCE);
    }

    private AttendanceStatus attendanceStatusConvert(String[] rawData) {
        String name = rawData[0];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime date = LocalDateTime.parse(rawData[1], formatter);

        LocalTime attendanceTime = date.toLocalTime();
        LocalTime time = LocalTime.of(10, 0);
        if (date.getDayOfWeek().getValue() == 1) {
            time = LocalTime.of(13, 0);
        }

        Duration between = Duration.between(time, attendanceTime);

        if (between.getSeconds() > 1800) {
            return new AttendanceStatus(name, date, Status.ABSENCE);
        }

        if (between.getSeconds() > 300) {
            return new AttendanceStatus(name, date, Status.TARDY);
        }

        return new AttendanceStatus(name, date, Status.ATTENDANCE);
    }
}
