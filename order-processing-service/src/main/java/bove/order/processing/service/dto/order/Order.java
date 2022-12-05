package bove.order.processing.service.dto.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "T_order")
@Getter
@Setter
@RequiredArgsConstructor

public class Order {
    private final Date orderDate = new Date();
    @Id
    @Column(name = "id", nullable = false)
    @NotNull
    private String orderID;
    //    private String id;
    private String product;
    private int quantity;
    private Double price;
    private String side;
    private String type;
    private Date dateCreated;
    private Date dateClosed;
    private Date dateUpdated;
    private String status;
    private String exchange;
    private int userId;
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Execution> executions;
    private int cumulatitiveQuantity;
    private double cumulatitivePrice;

    public Order(String id,
                 String product,
                 int quantity,
                 double price,
                 String side,
                 String type,
                 Date dateCreated,
                 String exchange,
                 int userId) {
        this.orderID = id;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.side = side;
        this.type = type;
        this.dateCreated = dateCreated;
        this.exchange = exchange;
        this.userId = userId;
        this.status = "pending";
    }

    public Order (){

    }

    @Override
    public String toString() {
        return "Order{" +
                "orderDate=" + orderDate +
                ", orderID='" + orderID + '\'' +
                ", product='" + product + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", side='" + side + '\'' +
                ", type='" + type + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateClosed=" + dateClosed +
                ", dateUpdated=" + dateUpdated +
                ", status='" + status + '\'' +
                ", exchange='" + exchange + '\'' +
                ", userId=" + userId +
                ", executions=" + executions +
                ", cumulatitiveQuantity=" + cumulatitiveQuantity +
                ", cumulatitivePrice=" + cumulatitivePrice +
                '}';
    }
}
