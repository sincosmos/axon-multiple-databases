package com.baeldung.axon.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "axonEntityManagerFactory",
        basePackages = "org.axonframework.eventsourcing.eventstore.jpa")
public class AxonEventStoreConfig {
    @Primary
    @Bean(name="axonMaster")
    @ConfigurationProperties("spring.datasource.hikari.axon-master")
    public DataSource axonMaster() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }


    @Primary
    @Bean(name="axonEntityManagerFactory")
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("axonMaster") DataSource axonMaster) {
        return builder
                .dataSource(axonMaster)
                .packages("org.axonframework.eventsourcing.eventstore.jpa")
                .persistenceUnit("axonMaster")
                .build();
    }

    @Primary
    @Bean(name = "axonTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("axonEntityManagerFactory") EntityManagerFactory axonEntityManagerFactory) {
        return new JpaTransactionManager(axonEntityManagerFactory);
    }
}
