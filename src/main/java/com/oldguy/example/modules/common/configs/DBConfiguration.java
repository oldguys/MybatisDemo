package com.oldguy.example.modules.common.configs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author huangrenhao
 * @date 2019/1/9
 */
@Configuration
@MapperScan(basePackages = "com.oldguy.example.modules.modal.dao.jpas")
public class DBConfiguration {

    @Value("${mybatis.type-aliases-package}")
    private String typeAliasesPackage;

    @Autowired
    private DataSource dataSource;
    @Qualifier("dbRegisterConfiguration")
    @Autowired
    private DbRegisterConfiguration dbRegisterConfiguration;

    public void init() throws IOException {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        dbRegisterConfiguration.initDB(jdbcTemplate, typeAliasesPackage);
    }
}
