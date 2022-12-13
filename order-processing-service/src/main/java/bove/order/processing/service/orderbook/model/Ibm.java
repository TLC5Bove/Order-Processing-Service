package bove.order.processing.service.orderbook.model;

import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "ibm")
public class Ibm extends Stock{
}
