package bove.order.processing.service.dto.order;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class OrderStatusResponse {
    private String product;
    private Integer quantity;
    private Double price;
    private String side;
    private ArrayList<Execution> executions = new ArrayList<>();
    private String orderID;
    private String orderType;
    private Integer cumulatitiveQuantity;
    private Double cumulatitivePrice;


}

@Data
class Execution {
    private Date timestamp;
    private Double price;
    private Integer quantity;
}

