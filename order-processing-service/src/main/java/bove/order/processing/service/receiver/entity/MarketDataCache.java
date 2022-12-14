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
public class MarketDataCache implements Serializable {

    @Serial
    private static final long serialVersionUID = -7817224776021728682L;

    private Double MAX_PRICE_SHIFT;
    private Double LAST_TRADED_PRICE;
    private Double BID_PRICE;
    private Integer SELL_LIMIT;
    private Double ASK_PRICE;
    private Integer BUY_LIMIT;
    private String TICKER;
}