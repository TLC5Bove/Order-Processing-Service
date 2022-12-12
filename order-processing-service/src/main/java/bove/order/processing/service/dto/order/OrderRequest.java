package bove.order.processing.service.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
//@RequiredArgsConstructor
@ToString
@NoArgsConstructor
public class OrderRequest {
    @JsonProperty
    private String product;
    private Integer quantity;
    private Double price;
    private String side;
    private String type;
    private Long portfolioId;
    private Long userId;
    private String osId;
    private Date orderDate;
}


