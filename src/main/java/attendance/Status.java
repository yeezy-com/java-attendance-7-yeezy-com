package attendance;

public enum Status {
    ATTENDANCE("출석"),
    TARDY("지각"),
    ABSENCE("결석");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
