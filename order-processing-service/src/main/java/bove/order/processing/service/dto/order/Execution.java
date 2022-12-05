package bove.order.processing.service.dto.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@Entity
public class Execution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date timestamp;
    private double price;
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public Execution() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Execution execution = (Execution) o;
        return id == execution.id && Double.compare(execution.price, price) == 0 && quantity == execution.quantity && timestamp.equals(execution.timestamp) && order.equals(execution.order);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

