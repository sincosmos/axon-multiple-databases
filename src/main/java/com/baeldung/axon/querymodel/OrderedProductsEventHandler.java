package com.baeldung.axon.querymodel;

import java.util.*;

import com.baeldung.axon.coreapi.queries.FindOrderedProductByIdQuery;
import com.baeldung.axon.repository.OrderMasterRepository;
import com.baeldung.axon.repository.OrderSlaveRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baeldung.axon.coreapi.events.OrderConfirmedEvent;
import com.baeldung.axon.coreapi.events.OrderPlacedEvent;
import com.baeldung.axon.coreapi.events.OrderShippedEvent;
import com.baeldung.axon.coreapi.queries.FindAllOrderedProductsQuery;
import com.baeldung.axon.coreapi.queries.OrderedProduct;

@Service
//@ProcessingGroup("ordered-products")
public class OrderedProductsEventHandler {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private OrderSlaveRepository orderSlaveRepository;

    @EventHandler
    public void on(OrderPlacedEvent event) {
        String orderId = event.getOrderId();
        orderMasterRepository.save(new OrderedProduct(orderId, event.getProduct()));
    }

    @EventHandler
    public void on(OrderConfirmedEvent event) {
        orderMasterRepository.findById(event.getOrderId()).ifPresent(x-> {
            x.setOrderConfirmed();
            orderMasterRepository.save(x);
        });

    }

    @EventHandler
    public void on(OrderShippedEvent event) {
        orderMasterRepository.findById(event.getOrderId()).ifPresent(x-> {
            x.setOrderShipped();
            orderMasterRepository.save(x);
        });
    }

    @QueryHandler
    public List<OrderedProduct> handle(FindAllOrderedProductsQuery query) {
        return orderMasterRepository.findAll();
    }

    @QueryHandler
    public OrderedProduct handle(FindOrderedProductByIdQuery query) {
        return orderMasterRepository.findById(query.getOrderId()).orElse(null);
    }
}