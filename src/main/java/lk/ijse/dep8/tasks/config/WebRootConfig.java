package lk.ijse.dep8.tasks.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(HibernateConfig.class)
public class WebRootConfig {
}
