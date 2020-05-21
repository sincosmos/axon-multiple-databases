package com.baeldung.axon.repository;

import com.baeldung.axon.config.annotation.Master;
import com.baeldung.axon.coreapi.queries.OrderedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Master
public interface OrderMasterRepository extends JpaRepository<OrderedProduct, String> {
}
