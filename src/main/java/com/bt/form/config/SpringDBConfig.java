package com.bt.form.config;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import com.bt.form.service.UserService;

/**
 * The Class SpringDBConfig.
 */

@Configuration
public class SpringDBConfig {

	@Autowired
	DataSource dataSource;
	
	@Resource
	private UserService userService;
	
	@Resource
	private Environment env;
	
	@Autowired
	Cipher decipher;

	@Autowired
	SecretKey secretKey;
	
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private final Logger logger = LoggerFactory.getLogger(SpringDBConfig.class);
	/**
	 * Gets the named parameter jdbc template.
	 *
	 * @return the named parameter jdbc template creating NamedParameterJdbcTemplate
	 *         for database operations
	 */
	@Bean
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}

	/**
	 * Data source initializer.
	 *
	 * @param dataSource the data source
	 * @return the data source initializer
	 * This Bean initialize the table with sample records loaded from scripts create-db.sql and insert-data.sql
	 */
	@Bean
	public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
		ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
		resourceDatabasePopulator.addScript(new ClassPathResource("/db/sql/create-db.sql"));
		resourceDatabasePopulator.addScript(new ClassPathResource("/db/sql/insert-data.sql"));
		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSource);
		dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
		logger.debug("initialized the table techmdb.userinfo with sample records loaded from scripts create-db.sql and insert-data.sql");
		return dataSourceInitializer;
	}

	/**
	 * Gets the data source.
	 *
	 * @return the data source We are using mysql server 8.0 database for
	 *         connectivity below are database connectivity details
	 * 
	 *         Url: jdbc:mysql://localhost:3306/techmdb
	 *         userName: root1
	 *         Password: admin1
	 * @throws NoSuchAlgorithmException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidKeyException 
	 * @throws NoSuchPaddingException 
	 * @throws IOException 
	 * 
	 */
	@Bean
	public DataSource getDataSource() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, IOException {
		String password = "";
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName(env.getProperty("database.driverClassName"));
		ds.setUrl(env.getProperty("database.url"));
		ds.setUsername(env.getProperty("database.username"));
		if (StringUtils.isNotBlank(env.getProperty("use.encrypt.pwd"))
				&& env.getProperty("use.encrypt.pwd").equals("true")) {
			password = userService.decrypt(env.getProperty("database.password.encrypt"));
		} else {
			password = env.getProperty("database.password");
		}
		ds.setPassword(password);
		return ds;
	}

	@PostConstruct
	public void startDBManager() {
		 // We are not using any implementation of this
	}

}