package bove.order.processing.service.receiver.entity;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MarketData implements Serializable {

    @Serial
    private static final long serialVersionUID = -7817224776021728682L;

    private String ticker;
    private Integer sellLimit;
    private Integer buyLimit;
    private Double lastTradedPrice;
    private Double askPrice;
    private Double bidPrice;
    private Double maxPriceShift;
}