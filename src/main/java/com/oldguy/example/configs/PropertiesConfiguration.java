package com.oldguy.example.configs;

import com.oldguy.example.modules.common.services.DbRegister;
import com.oldguy.example.modules.common.utils.Log4jUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author ren
 * @date 2019/1/25
 */
@Configuration
public class PropertiesConfiguration {

    @Value("${mybatis.mapper-generate-config-location}")
    private String MyBatisMapperGenerateConfigLocation;

    /**
     * 初始化环境常量
     *
     * @throws IOException
     */
    public void init() throws IOException {
        Log4jUtils.getInstance(getClass()).info("初始化环境常量");
        setProperties(MyBatisMapperGenerateConfigLocation, DbRegister.mapperGenerateLocationMap);
    }

    private void setProperties(String configPath, Map<String, String> valueMap) throws IOException {
        Properties properties = new Properties();
        File file = ResourceUtils.getFile("classpath:" + configPath);
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        properties.load(new InputStreamReader(bufferedInputStream, "gbk"));
        fileInputStream.close();

        for (Object key : properties.keySet()) {
            valueMap.put(String.valueOf(key), String.valueOf(properties.get(key)));
        }
    }
}
