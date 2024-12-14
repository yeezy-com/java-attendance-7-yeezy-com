package attendance.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {

    public String inputChoice() {
        return Console.readLine();
    }

    public String inputEditCrewNickname() {
        System.out.println("출석을 수정하려는 크루의 닉네임을 입력해 주세요.");
        return Console.readLine();
    }

    public String inputEditDay() {
        System.out.println("수정하려는 날짜(일)를 입력해 주세요.");
        return Console.readLine();
    }
}
