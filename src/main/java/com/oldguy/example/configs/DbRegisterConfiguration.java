package com.oldguy.example.configs;

import com.oldguy.example.modules.common.dto.db.DbRegisterProperties;
import com.oldguy.example.modules.common.services.DbRegister;
import com.oldguy.example.modules.common.services.TableSchemaService;
import com.oldguy.example.modules.common.services.impls.MySQLTableFactory;
import com.oldguy.example.modules.common.utils.Log4jUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.*;

/**
 * @Description:
 * @Author: ren
 * @CreateTime: 2018-10-2018/10/15 0015 17:45
 */
@Configuration
public class DbRegisterConfiguration {

    @Value("${mybatis.application-location}")
    private String applicationLocation;
    @Value("${mybatis.template-location}")
    private String templateClassPath;

    /**
     * 初始化数据库
     */
    public void initDB(JdbcTemplate jdbcTemplate, String typeAliasesPackage) {

        DbRegisterProperties properties = new DbRegisterProperties();
        properties.setPackageNames(splitPackagesPath(typeAliasesPackage));
        properties.setFactory(new MySQLTableFactory());
        properties.setApplicationLocation(applicationLocation);
        properties.setJdbcTemplate(jdbcTemplate);
        properties.setTemplateClassPath(templateClassPath);

        DbRegister dbRegister = new DbRegister();

        // 注册
        dbRegister.register(properties);
    }

    private List<String> splitPackagesPath(String typeAliasesPackage) {
        List<String> paths = new ArrayList<>();
        String[] packagePaths = typeAliasesPackage.split(";");
        for (String path : packagePaths) {
            if (!StringUtils.isEmpty(path)) {
                paths.add(path);
            }
        }
        return paths;
    }


}
