package bove.order.processing.service.dto.order;

import lombok.Data;

@Data
public class CompleteOrder {
    private String OSID;
    private Double cummPrice;
}
