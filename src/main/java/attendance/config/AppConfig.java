package attendance.config;

import attendance.CsvFileReader;
import attendance.controller.AttendanceController;

public class AppConfig {

    private CsvFileReader csvFileReader;
    private AttendanceController attendanceController;

    public AttendanceController attendanceController() {
        if (attendanceController == null) {
            this.attendanceController = new AttendanceController();
        }
        return attendanceController;
    }

}
