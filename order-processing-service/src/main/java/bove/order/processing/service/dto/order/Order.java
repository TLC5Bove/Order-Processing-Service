package bove.order.processing.service.dto.order;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity(name = "T_order")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Order {
    private final Date orderDate = new Date();
    @Id
    private String id;
    private String product;
    private Integer quantity;
    private Double price;
    private String side;
    private String type;
    private Date dateCreated;
    private Date dateClosed;
    private Date dateUpdated;
    private String status;
    private String exchange;
    private Integer stockId;
    private Integer userId;

    public Order(String id, String product, Integer quantity, Double price, String side, String type, Date dateCreated) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.side = side;
        this.type = type;
        this.dateCreated = dateCreated;

    }
}
