package com.oldguy.example.configs;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author huangrenhao
 * @date 2019/1/10
 */
@Configuration
@AutoConfigureBefore(MybatisAutoConfiguration.class)
public class DemoConfiguration {

    public static final Integer DEFAULT_PAGE_SIZE = 10;

    @Qualifier("propertiesConfiguration")
    @Autowired
    private PropertiesConfiguration propertiesConfiguration;
    @Qualifier("DBConfiguration")
    @Autowired
    private DBConfiguration DBConfiguration;

    @PostConstruct
    public void init() throws IOException {
        // 初始化属性配置
        propertiesConfiguration.init();
        // 初始化 数据库配置
        DBConfiguration.init();

    }
}
