package bove.order.processing.service.dto.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity(name = "T_order")
@RequiredArgsConstructor
@Getter
@Setter
public class Order {
    private final Date orderDate = new Date();
    @Id
    @Column(name = "id", nullable = false)
    private String orderID;
    private String product;
    private int quantity;
    private Double price;
    private String side;
    private String type;
    private Date dateCreated;
    private Date dateClosed;
    private Date dateUpdated;
    private String status;

    private String osId;
    private String exchange;
    private Long userId;
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
                 Long userId, String osId) {
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
        this.osId = osId;
    }

    public void add(Execution execution){
        executions.add(execution);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return quantity == order.quantity && Objects.equals(userId, order.userId) && cumulatitiveQuantity == order.cumulatitiveQuantity && Double.compare(order.cumulatitivePrice, cumulatitivePrice) == 0 && orderDate.equals(order.orderDate) && orderID.equals(order.orderID) && product.equals(order.product) && price.equals(order.price) && side.equals(order.side) && type.equals(order.type) && dateCreated.equals(order.dateCreated) && Objects.equals(dateClosed, order.dateClosed) && Objects.equals(dateUpdated, order.dateUpdated) && status.equals(order.status) && exchange.equals(order.exchange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderDate, orderID, product, quantity, price, side, type, dateCreated, dateClosed, dateUpdated, status, exchange, userId, cumulatitiveQuantity, cumulatitivePrice);
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
                ", osId=" + osId +
                ", cumulatitiveQuantity=" + cumulatitiveQuantity +
                ", cumulatitivePrice=" + cumulatitivePrice +
                '}';
    }
}
