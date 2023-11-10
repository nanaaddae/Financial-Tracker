package configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@org.springframework.context.annotation.Configuration
public class Configuration 
{
    
@Value("${spring.datasource.url}")
	    private String datasourceUrl;

	    @Value("${spring.datasource.driverClassName}")
	    private String dbDriverClassName;

	    @Value("${spring.datasource.username}")
	    private String dbUsername;

	    @Value("${spring.datasource.password}")
	    private String dbPassword;

	    @Bean
	    public DataSource dataSource() {
	        DriverManagerDataSource dataSource = new DriverManagerDataSource();
	        dataSource.setDriverClassName(dbDriverClassName);
	        dataSource.setUrl(datasourceUrl);
	        dataSource.setUsername(dbUsername);
	        dataSource.setPassword(dbPassword);

	        return dataSource;
	    }


}
