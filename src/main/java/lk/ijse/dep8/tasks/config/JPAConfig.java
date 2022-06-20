package lk.ijse.dep8.tasks.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
@PropertySource("classpath:application-prod.properties")
public class JPAConfig {

    private final Environment env;

    public JPAConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource ds, JpaVendorAdapter jpaVendorAdapter){
        LocalContainerEntityManagerFactoryBean lcemfb = new LocalContainerEntityManagerFactoryBean();
        lcemfb.setDataSource(ds);
        lcemfb.setJpaVendorAdapter(jpaVendorAdapter);
        lcemfb.setPackagesToScan("lk.ijse.dep8.tasks.entity");
        return lcemfb;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter(){
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.MYSQL);
        jpaVendorAdapter.setDatabasePlatform(env.getRequiredProperty("hibernate.dialect"));
        jpaVendorAdapter.setShowSql(env.getProperty("hibernate.show_sql", Boolean.class, false));
        return jpaVendorAdapter;
    }

    @Bean
    public JndiObjectFactoryBean dataSource() {
        JndiObjectFactoryBean jndiDataSource = new JndiObjectFactoryBean();
        jndiDataSource.setJndiName("java:comp/env/jdbc/pool");
        jndiDataSource.setResourceRef(true);
        return jndiDataSource;
    }
}
