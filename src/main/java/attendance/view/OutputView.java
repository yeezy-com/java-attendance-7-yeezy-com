package attendance.view;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class OutputView {

    public static final int DISMISSAL_TARGET_STANDARD = 5;
    public static final int DISCUSSION_TARGET_STANDARD = 3;
    public static final int WARNING_TARGET_STANDARD = 2;

    public void printWelcomeMessage(LocalDate now) {
        String dayOfWeek = now.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);
        System.out.printf("오늘은 %d월 %d일 %s입니다. 기능을 선택해 주세요.%n", now.getMonth().getValue(), now.getDayOfMonth(), dayOfWeek);
        System.out.println("1. 출석 확인");
        System.out.println("2. 출석 수정");
        System.out.println("3. 크루별 출석 기록 확인");
        System.out.println("4. 제적 위험자 확인");
        System.out.println("Q. 종료");
    }

    public void printStatus(int[] attendanceCount) {
        System.out.println();
        System.out.printf("출석: %d회%n", attendanceCount[0]);
        System.out.printf("지각: %d회%n", attendanceCount[1]);
        System.out.printf("결석: %d회%n", attendanceCount[2]);
        System.out.println();

        printStatusTarget(attendanceCount);
        System.out.println();
    }

    public void printNickNameAttendanceStatusIntroduceMessage(String nickname) {
        System.out.println();
        System.out.printf("이번 달 %s의 출석 기록입니다.%n", nickname);
        System.out.println();
    }

    private void printStatusTarget(int[] attendanceCount) {
        if (attendanceCount[2] > DISMISSAL_TARGET_STANDARD) {
            System.out.println("제적 대상자입니다.");
            return;
        }

        if (attendanceCount[2] >= DISCUSSION_TARGET_STANDARD) {
            System.out.println("면담 대상자입니다.");
            return;
        }

        if (attendanceCount[2] >= WARNING_TARGET_STANDARD) {
            System.out.println("경고 대상자입니다.");
        }
    }
}
