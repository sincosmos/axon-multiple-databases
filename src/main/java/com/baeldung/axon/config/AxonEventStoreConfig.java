package com.baeldung.axon.config;

import com.zaxxer.hikari.HikariDataSource;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
public class AxonEventStoreConfig {
    @Bean("axonMaster")
    @ConfigurationProperties("spring.datasource.hikari.axon-master")
    public DataSource axon() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name="axonEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("axonMaster") DataSource axonMaster) {
        return builder
                .dataSource(axonMaster)
                .persistenceUnit("axonMaster")
                .properties(jpaProperties())
                .packages("org.axonframework.eventhandling.tokenstore",
                        "org.axonframework.modelling.saga.repository.jpa",
                        "org.axonframework.eventsourcing.eventstore.jpa")
                .build();
    }

    /**
     * For axon framework
     * @param entityManagerFactory
     * @return
     */
    @Bean
    public EntityManagerProvider entityManagerProvider(@Qualifier("axonEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return () -> entityManagerFactory.getObject().createEntityManager();
    }

    private Map<String, Object> jpaProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
        props.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.show_sql", "true");
        return props;
    }
}
