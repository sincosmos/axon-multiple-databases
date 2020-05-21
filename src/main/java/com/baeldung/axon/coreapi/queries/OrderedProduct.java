package com.baeldung.axon.coreapi.queries;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="order_product")
@Data
@NoArgsConstructor
public class OrderedProduct {

    @Id
    private String orderId;
    private String product;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public OrderedProduct(String orderId, String product) {
        this.orderId = orderId;
        this.product = product;
        orderStatus = OrderStatus.PLACED;
    }

    public void setOrderConfirmed() {
        this.orderStatus = OrderStatus.CONFIRMED;
    }

    public void setOrderShipped() {
        this.orderStatus = OrderStatus.SHIPPED;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, product, orderStatus);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final OrderedProduct other = (OrderedProduct) obj;
        return Objects.equals(this.orderId, other.orderId)
                && Objects.equals(this.product, other.product)
                && Objects.equals(this.orderStatus, other.orderStatus);
    }

    @Override
    public String toString() {
        return "OrderedProduct{" +
                "orderId='" + orderId + '\'' +
                ", product='" + product + '\'' +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
