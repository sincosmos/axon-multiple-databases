package com.baeldung.axon.config;

import com.baeldung.axon.config.annotation.Slave;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


/**
 * datasource
 */
/**
 * Datasource master
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.baeldung.axon.repository"},
        includeFilters = @ComponentScan.Filter(Slave.class),
        entityManagerFactoryRef = "projectionSlaveEntityManager")
public class SlaveDataSourceConfiguration {
    @Bean("projectionSlave")
    @ConfigurationProperties("spring.datasource.hikari.projection-slave")
    public DataSource slave() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "projectionSlaveEntityManager")
    public LocalContainerEntityManagerFactoryBean projectionSlaveEntityManager(EntityManagerFactoryBuilder builder) {
        return  builder.dataSource(slave())
                .persistenceUnit("projectionSlave")
                .properties(jpaProperties())
                .packages("com.baeldung.axon.repository", "com.baeldung.axon.coreapi.queries")
                .build();
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
