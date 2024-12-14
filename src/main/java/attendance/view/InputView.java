package attendance.view;

import attendance.ErrorMessage;
import camp.nextstep.edu.missionutils.Console;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class InputView {

    public String inputChoice() {
        return Console.readLine();
    }

    public LocalTime inputEnterTime() {
        System.out.println("등교 시간을 입력해 주세요.");
        try {
             return LocalTime.parse(Console.readLine());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(ErrorMessage.WRONG_FORM_ERROR.getMsg());
        }
    }

    public String inputEditCrewNickname() {
        System.out.println("출석을 수정하려는 크루의 닉네임을 입력해 주세요.");
        return Console.readLine();
    }

    public String inputEditDay() {
        System.out.println("수정하려는 날짜(일)를 입력해 주세요.");
        return Console.readLine();
    }

    public String inputNickName() {
        System.out.println("닉네임을 입력해 주세요.");
        return Console.readLine();
    }
}
