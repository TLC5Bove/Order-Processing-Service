package bove.order.processing.service.order;

import org.springframework.stereotype.Component;

@Component
public class Order {
    private Long id;
    private String product;
    private Integer quantity;
    private Double price;
    private String side;
    private String type;

    public Order(){

    }
    public Order(String product, Integer quantity, Double price, String side, String type) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.side = side;
        this.type = type;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", product='" + product + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", Side='" + side + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
