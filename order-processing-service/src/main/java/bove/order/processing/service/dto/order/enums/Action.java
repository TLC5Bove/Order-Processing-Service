package bove.order.processing.service.dto.order.enums;

public enum Action {
    SPLIT("split"),
    EXCHANGE1("exchange1"),
    EXCHANGE2("exchange2"),
    INVALID_LIMIT("The limit does match what was prescribed"),
    RANGE_EXCEEDED("The price range was exceeded"),

    BOTH("Invalid price and quantity limit");

    private final String action;

    Action(String action) {
        this.action = action;
    }

    public String value() {
        return action;
    }
}
