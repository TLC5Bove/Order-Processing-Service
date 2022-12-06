package bove.order.processing.service.dto.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;

@Entity(name = "T_order")
@Getter
@Setter
@ToString
@RequiredArgsConstructor

public class Order {
    private final Date orderDate = new Date();
    @Id
    @Column(name = "id", nullable = false)
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
    private ArrayList<Execution> executions;
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
        this.quantity = quantity;
        this.price = price;
        this.side = side;
        this.type = type;
        this.dateCreated = dateCreated;
        this.exchange = exchange;
        this.userId = userId;
        this.status = "pending";
    }

    public void addExecutions(Execution execution) {
        if (executions == null) {
            executions = new ArrayList<>();
        }
        executions.add(execution);
    }
}
