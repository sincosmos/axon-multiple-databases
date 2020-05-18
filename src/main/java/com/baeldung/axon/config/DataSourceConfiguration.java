package com.baeldung.axon.config;

import com.zaxxer.hikari.HikariDataSource;
import org.axonframework.common.jdbc.PersistenceExceptionResolver;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.jpa.JpaEventStorageEngine;
import org.axonframework.serialization.Serializer;
import org.axonframework.spring.config.AxonConfiguration;
import org.axonframework.springboot.util.jpa.ContainerManagedEntityManagerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


/**
 * datasource
 */
@Configuration
public class DataSourceConfiguration {
    @Primary
    @Bean("axon")
    @ConfigurationProperties("spring.datasource.hikari.axon-master")
    public DataSource axon() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean("projectionRead")
    @ConfigurationProperties("spring.datasource.hikari.projection-write")
    public DataSource master() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean("projectionWrite")
    @ConfigurationProperties("spring.datasource.hikari.projection-read")
    public DataSource slave() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
}
