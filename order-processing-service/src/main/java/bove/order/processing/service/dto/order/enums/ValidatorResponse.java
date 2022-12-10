package bove.order.processing.service.dto.order.enums;

public enum ValidatorResponse {
    SUCCESS_BOTH("split"),
    SUCCESS_EXCHANGE1("exchange1"),
    SUCCESS_EXCHANGE2("exchange2"),
    FAIL("fail");

    ValidatorResponse(String message) {
    }
}
