package bove.order.processing.service.dto.order;

import bove.order.processing.service.dto.order.enums.OrderType;
import bove.order.processing.service.dto.order.enums.Side;
import bove.order.processing.service.dto.order.enums.Ticker;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class OrderDTO {
    private Ticker product;
    private Integer quantity;
    private Double price;
    private Side side;
    private OrderType type;
    private Long userId;
    private Long portId;
    private String OSID;
}
