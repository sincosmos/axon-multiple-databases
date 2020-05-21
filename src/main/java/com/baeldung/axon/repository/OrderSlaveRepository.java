package com.baeldung.axon.repository;

import com.baeldung.axon.config.annotation.Slave;
import com.baeldung.axon.coreapi.queries.OrderedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Slave
public interface OrderSlaveRepository extends JpaRepository<OrderedProduct, String> {
}
