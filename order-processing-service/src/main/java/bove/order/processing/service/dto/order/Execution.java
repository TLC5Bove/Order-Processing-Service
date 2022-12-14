package bove.order.processing.service.dto.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Execution {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private Date timestamp;
    private double price;
    private int quantity;
    @JsonIgnore
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
        return Double.compare(execution.price, price) == 0 && quantity == execution.quantity && timestamp.equals(execution.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, price, quantity);
    }

    @Override
    public String toString() {
        return "Execution{" +
                "timestamp=" + timestamp +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}

