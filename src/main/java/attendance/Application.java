package attendance;

import attendance.config.AppConfig;
import attendance.controller.AttendanceController;

public class Application {
    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();
        AttendanceController attendanceController = appConfig.attendanceController();
        attendanceController.start();
    }
}
