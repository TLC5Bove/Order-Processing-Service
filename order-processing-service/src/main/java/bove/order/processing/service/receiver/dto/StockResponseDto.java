package bove.order.processing.service.receiver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockResponseDto {
    private String product;
    private double price;
    private String side;
    private String exchange;

}
