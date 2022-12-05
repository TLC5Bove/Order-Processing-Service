package bove.order.processing.service.receiver.entity;

import jakarta.persistence.Id;

public class StockData {
    @Id
    private int id;
    private String product;
    private String exchange;
    private double price;
    private String side;
    private String orderType;
}
