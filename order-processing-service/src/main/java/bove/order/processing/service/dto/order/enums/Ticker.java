package bove.order.processing.service.dto.order.enums;

public enum Ticker {
    IBM("IBM"),
    GOOGL("GOOGL"),
    AAPL("AAPL"),
    TSLA("TSLA"),
    NFLX("NFLX"),
    MSFT("MSFT"),
    ORCL("ORCL"),
    AMZN("AMZN");

    private final String productName;

    private Ticker(String productName){
        this.productName = productName;
    }

    public String getProductName(){
        return productName;
    }
}