package lk.ijse.dep8.tasks.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@Profile("test")
@PropertySource("classpath:application-dev.properties")
public class TestConfig {

    private final Environment env;

    public TestConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty("javax.persistence.jdbc.driver"));
        dataSource.setUrl(env.getRequiredProperty("javax.persistence.jdbc.url"));
        return dataSource;
    }

}
