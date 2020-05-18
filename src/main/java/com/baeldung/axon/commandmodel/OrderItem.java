package com.baeldung.axon.commandmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.EntityId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @EntityId
    private String productId;

    private int quantity;
}


