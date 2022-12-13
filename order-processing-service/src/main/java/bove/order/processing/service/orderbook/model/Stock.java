package bove.order.processing.service.orderbook.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
public abstract class Stock {

    @Id
    protected String id;

    @Field(type = FieldType.Text, name = "product")
    protected String product;

    @Field(type = FieldType.Integer, name = "quantity")
    protected int quantity;

    @Field(type = FieldType.Double, name = "price")
    protected Double price;

    @Field(type = FieldType.Text, name = "side")
    protected String side;

    @Field(type = FieldType.Text, name = "orderId")
    protected String orderID;

    @Field(type = FieldType.Text, name = "orderType")
    protected String orderType;

    @Field(type = FieldType.Text, name = "exchange")
    protected String exchange;

    @Field(type = FieldType.Integer, name = "cumulatitiveQuantity")
    protected int cumulatitiveQuantity;

    protected double cumulatitivePrice;
}
