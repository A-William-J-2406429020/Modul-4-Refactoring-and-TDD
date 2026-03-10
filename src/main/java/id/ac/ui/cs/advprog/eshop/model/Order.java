package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
public class Order {
    String id;
    List<Product> products;
    Long orderTime;
    String author;
    String status;

    public Order(String id, List<Product> products, Long orderTime, String author) {
        this.id = id;
        this.orderTime = orderTime;
        this.author = author;

        if (products.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            this.products = products;
        }

        this.status = "WAITING_PAYMENT";
    }

    public Order(String id, List<Product> products, Long orderTime, String author, String status) {
        this(id, products, orderTime, author);

        String[] statusList = {"WAITING_PAYMENT", "FAILED", "SUCCESS", "CANCELLED"};
        if (Arrays.asList(statusList).contains(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setStatus(String status) {
        String[] statusList = {"WAITING_PAYMENT", "FAILED", "SUCCESS", "CANCELLED"};
        if (Arrays.asList(statusList).contains(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }
}