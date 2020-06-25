package clane.car;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import javax.sql.DataSource;
import java.util.Properties;
import org.springframework.context.annotation.Primary;

/**
 * This configuration class configures the persistence layer of our example
 * application and enables annotation driven transaction management.
 * <p>
 * This configuration is put to a single class because this way we can write
 * integration tests for our persistence layer by using the configuration used
 * by the application.
 *
 * In other words, we can ensure that the persistence layer of the application
 * works as expected.
 *
 * @author Jejelowo B. Festus
 */
@Configuration
@ComponentScan("clane.car.service")
@EnableJpaRepositories(basePackages = {"clane.car.repository"})
@PropertySource(value = {"file:config/application.properties"})
public class Db {

    private static final String[] ENTITY_PACKAGES = {"clane.car.model"};
    private static final String PROPERTY_DB_DRIVER_CLASS = "app.db.class";
    private static final String PROPERTY_DB_PASSWORD = "app.db.pass";
    private static final String PROPERTY_DB_NAME = "app.db.name";
    private static final String PROPERTY_DB_ADDR = "app.db.addr";
    private static final String PROPERTY_DB_USER = "app.db.user";
    private static final String HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String HIBERNATE_FORMAT_SQL = "hibernate.format_sql";
    private static final String HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";
    private static final String HIBERNATE_NAMING_STRATEGY = "hibernate.ejb.naming_strategy";
    private static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";

    @Autowired
    private Environment env;

    /**
     * Creates and configures the HikariCP data source bean.
     *
     * @param env The run time environment of our application.
     * @return
     */
    @Bean(destroyMethod = "close")
    DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(env.getRequiredProperty(PROPERTY_DB_DRIVER_CLASS));
        config.setMaximumPoolSize(Math.max(Runtime.getRuntime().availableProcessors(), 4));
        config.addDataSourceProperty("serverName", env.getRequiredProperty(PROPERTY_DB_ADDR));
        config.addDataSourceProperty("databaseName", env.getRequiredProperty(PROPERTY_DB_NAME));
        config.addDataSourceProperty("user", env.getRequiredProperty(PROPERTY_DB_USER));
        config.addDataSourceProperty("password", env.getRequiredProperty(PROPERTY_DB_PASSWORD));
        return new HikariDataSource(config);
    }

    /**
     * Creates the bean that creates the JPA entity manager factory.
     *
     * Configures the used database dialect. This allows Hibernate to create SQL
     * that is optimized for the used database.
     *
     * Specifies the action that is invoked to the database when the Hibernate
     * SessionFactory is created or closed.
     *
     * Configures the naming strategy that is used when Hibernate creates new
     * database objects and schema elements.
     *
     * @param dataSource The data source that provides the database connections.
     * @param env The runtime environment of our application.
     * @return {@code LocalContainerEntityManagerFactoryBean}
     */
    @Primary
    @Bean(name = "entityManagerFactory")
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
            Environment env) {
        Properties properties = new Properties();
        properties.put(HIBERNATE_DIALECT, env.getRequiredProperty(HIBERNATE_DIALECT));
        properties.put(HIBERNATE_HBM2DDL_AUTO, env.getProperty(HIBERNATE_HBM2DDL_AUTO));
        properties.put(HIBERNATE_NAMING_STRATEGY,
                env.getRequiredProperty(HIBERNATE_NAMING_STRATEGY));
        properties.put(HIBERNATE_SHOW_SQL, env.getRequiredProperty(HIBERNATE_SHOW_SQL));
        properties.put(HIBERNATE_FORMAT_SQL, env.getRequiredProperty(HIBERNATE_FORMAT_SQL));
        properties.put("hibernate.jdbc.batch_size", 250);
        properties.put("spring.jpa.hibernate.temp.use_jdbc_metadata_defaults", "false");
        properties.put("spring.jpa.hibernate.ddl-auto", "none");
        properties.put("hibernate.order_inserts", true);
        properties.put("hibernate.order_updates", true);

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setJpaProperties(properties);
        emf.setDataSource(dataSource);
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emf.setPackagesToScan(ENTITY_PACKAGES);
        emf.setPersistenceUnitName("CARPU"); // <- giving 'default' as name
        emf.afterPropertiesSet();
        return emf;
    }

}
