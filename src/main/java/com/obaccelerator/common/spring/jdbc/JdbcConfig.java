package com.obaccelerator.common.spring.jdbc;

import com.obaccelerator.common.spring.vault.VaultObaConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


/**
 * This JDBC config is almost empty, because a PlatformTransactionManager and a NamedParameterJdbcTemplate
 * are already auto-configured
 * <p>
 * See JdbcTemplateAutoConfiguration for JDBC template
 * See DataSourceTransactionManagerAutoConfiguration for the transaction manager
 */
@Configuration
public class JdbcConfig {

    private static final String MYSQL_URI = "jdbc:mysql://localhost:3306";
    private VaultObaConfiguration vaultOBAConfiguration;

    public JdbcConfig(VaultObaConfiguration vaultOBAConfiguration) {
        this.vaultOBAConfiguration = vaultOBAConfiguration;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(MYSQL_URI + "/" + vaultOBAConfiguration.getDbName());
        dataSource.setUsername(vaultOBAConfiguration.getMysqlUser());
        dataSource.setPassword(vaultOBAConfiguration.getMysqlPassword());
        return dataSource;
    }


}
