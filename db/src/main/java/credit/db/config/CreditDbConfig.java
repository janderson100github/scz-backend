package credit.db.config;

import credit.db.entity.Pool;
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
@EnableJpaRepositories(entityManagerFactoryRef = "creditEntityManagerFactory",
                       transactionManagerRef = "creditTransactionManager",
                       basePackages = {"credit.db.repository"})
public class CreditDbConfig {

    private static final String DATASOURCE_PROPS_PREFIX = "spring.credit.datasource";

    private static final String HIBERNATE_PROPS_PREFIX = "spring.credit.jpa";

    protected static final String ENTITIES_PATH = Pool.class.getPackage()
            .getName();

    private HibernateProperties hibernateProperties;

    public CreditDbConfig(HibernateProperties hibernateProperties) {
        this.hibernateProperties = hibernateProperties;
    }

    @Primary
    @Bean(name = "creditDataSource")
    @ConfigurationProperties(prefix = DATASOURCE_PROPS_PREFIX)
    public DataSource creditDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Primary
    @Bean(name = "creditEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean creditEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                             @Qualifier("creditDataSource")
                                                                                     DataSource creditDataSource) {
        return builder.dataSource(creditDataSource)
                .properties(hibernateProperties.getProperties(HIBERNATE_PROPS_PREFIX))
                .packages(ENTITIES_PATH)
                .persistenceUnit("creditPersistenceUnit")
                .build();
    }

    @Primary
    @Bean(name = "creditTransactionManager")
    public PlatformTransactionManager creditTransactionManager(
            @Qualifier("creditEntityManagerFactory")
                    EntityManagerFactory creditEntityManagerFactory) {
        return new JpaTransactionManager(creditEntityManagerFactory);
    }
}

