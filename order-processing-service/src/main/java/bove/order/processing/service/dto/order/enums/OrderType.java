package bove.order.processing.service.dto.order.enums;

public enum OrderType {
    LIMIT("LIMIT"),
    MARKET("MARKET");

    private final String type;

    private OrderType(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
