package attendance.config;

import attendance.CsvFileReader;
import attendance.controller.AttendanceController;
import attendance.view.InputView;
import attendance.view.OutputView;

public class AppConfig {

    private CsvFileReader csvFileReader;
    private AttendanceController attendanceController;
    private InputView inputView;
    private OutputView outputView;

    public InputView inputView() {
        if (inputView == null) {
            this.inputView = new InputView();
        }
        return inputView;
    }

    public OutputView outputView() {
        if (outputView == null) {
            this.outputView = new OutputView();
        }
        return outputView;
    }

    public AttendanceController attendanceController() {
        if (attendanceController == null) {
            this.attendanceController = new AttendanceController(inputView(), outputView());
        }
        return attendanceController;
    }

}
