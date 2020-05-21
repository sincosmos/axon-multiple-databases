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
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


/**
 * Datasource master
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.baeldung.axon.repository"},
        excludeFilters = @ComponentScan.Filter(Slave.class),
        entityManagerFactoryRef = "projectionMasterEntityManager")
public class MasterDataSourceConfiguration {
    @Primary
    @Bean("projectionMaster")
    @ConfigurationProperties("spring.datasource.hikari.projection-master")
    public DataSource master() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "projectionMasterEntityManager")
    public LocalContainerEntityManagerFactoryBean projectionMasterEntityManager(EntityManagerFactoryBuilder builder) {
        return  builder.dataSource(master())
                .persistenceUnit("projectionMaster")
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
