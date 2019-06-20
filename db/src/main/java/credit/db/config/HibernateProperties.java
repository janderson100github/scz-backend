package credit.db.config;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@Component
public class HibernateProperties {

    Environment environment;

    public HibernateProperties(Environment environment) {
        this.environment = environment;
    }

    public Map<String, String> getProperties(String prefix) {
        if (!prefix.endsWith(".")) {
            prefix = prefix + ".";
        }

        String[] suffixes = {"hibernate.dialect", "hibernate.hbm2ddl.auto", "hibernate.naming-strategy",
                             "hibernate.connection.release_mode"};

        Properties props = new Properties();
        for (String suffix : suffixes) {
            String propertyKey = suffix;
            String propertyName = prefix + suffix;
            String propertyValue = environment.getProperty(propertyName);
            if (propertyValue != null) {
                props.setProperty(propertyKey, propertyValue);
            }
        }

        props.setProperty("hibernate.show_sql", Optional.ofNullable(environment.getProperty(prefix + "show-sql"))
                .orElse("false"));

        return (Map) props;
    }
}
