package com.ihobb.gm.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DynamicTenantAwareRoutingDataSource extends AbstractRoutingDataSource {

//    private final Map<String, HikariDataSource> tenants;

    public DynamicTenantAwareRoutingDataSource() {
        // default constructor, use default data source
        this("admin");
    }

    public DynamicTenantAwareRoutingDataSource(String databaseName) {
        final HikariDataSource dataSource = buildDataSource(databaseName);
        TenantDataSourceContextHolder.setDataSourceContext(dataSource);
    }

    public HikariDataSource buildDataSource(String dataBaseName) {

        Properties datasourceProperties = new Properties();
        datasourceProperties.put("url", "jdbc:postgresql://127.0.0.1:5432" + dataBaseName);
        datasourceProperties.put("user", "postgres");
        datasourceProperties.put("password", "ihobb");
        datasourceProperties.put("driverClassName", "org.postgresql.Driver");

        Properties hikariConfigProperties = new Properties();

        hikariConfigProperties.put("poolName","ProjectsDBPool");
        hikariConfigProperties.put("jdbcUrl", "jdbc:postgresql://127.0.0.1:5432/" + dataBaseName);
        hikariConfigProperties.put("driverClassName","org.postgresql.Driver");

//        hikariConfigProperties.put("maximumPoolSize", 5);
//        hikariConfigProperties.put("idleTimeout",     50000);
        hikariConfigProperties.put("dataSourceProperties", datasourceProperties);

        HikariConfig hc = new HikariConfig(hikariConfigProperties);
        return new HikariDataSource(hc);
    }

    @Override
    public void afterPropertiesSet() {
        // todo sth needs to do
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantDataSourceContextHolder.getDataSourceContext();
    }

    @Override
    protected DataSource determineTargetDataSource() {
        return (DataSource) determineCurrentLookupKey();
    }

    public void initDatasource() {
        Map<Object, Object> dataSourceMap = new HashMap<>();
        DataSource adminDataSource = buildDataSource("admin");
        dataSourceMap.put("admin", adminDataSource);
        this.setTargetDataSources(dataSourceMap);
        this.setDefaultTargetDataSource(adminDataSource);
    }
}
