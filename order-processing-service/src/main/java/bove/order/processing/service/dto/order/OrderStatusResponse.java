package bove.order.processing.service.dto.order;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class OrderStatusResponse {
    private String product;
    private int quantity;
    private double price;
    private String side;
    private List<Execution> executions;
    private String orderID;
    private String orderType;
    private int cumulatitiveQuantity;
    private Double cumulatitivePrice;
}

