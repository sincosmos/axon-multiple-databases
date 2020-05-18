package com.baeldung.axon.coreapi.queries;

public class FindOrderedProductByIdQuery {
    private final String orderId;

    public FindOrderedProductByIdQuery(String orderId) {
        this.orderId = orderId;
    }
    public String getOrderId() {
        return orderId;
    }
}
