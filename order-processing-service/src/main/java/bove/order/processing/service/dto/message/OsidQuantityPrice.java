package bove.order.processing.service.dto.message;

import lombok.Data;

@Data
public class OsidQuantityPrice {
    private String osId;
    private Integer quantity;
    private Double price;
}
