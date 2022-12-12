package bove.order.processing.service.dto.order.enums;

public enum Side {
    BUY("BUY"),
    SELL("SELL");

    private final String side;

    private Side(String side){
        this.side = side;
    }

    public String getSide() {
        return side;
    }
}