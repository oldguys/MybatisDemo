package com.oldguy.example.common;

import com.oldguy.example.modules.common.dto.db.MyBatisMapperData;
import com.oldguy.example.modules.common.dto.db.SqlTableObject;
import com.oldguy.example.modules.common.services.DbRegister;
import com.oldguy.example.modules.common.services.MyBatisMapperService;
import com.oldguy.example.modules.common.services.TableFactory;
import com.oldguy.example.modules.common.services.TableSchemaService;
import com.oldguy.example.modules.common.services.impls.MySQLTableFactory;
import com.oldguy.example.modules.common.utils.ThymeleafUtils;
import com.oldguy.example.modules.modal.dao.entities.MyComponent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.context.Context;
import org.thymeleaf.util.ClassLoaderUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author huangrenhao
 * @date 2019/2/1
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DbRegisterTest {

    @Value("${mybatis.template-location}")
    private String templateLocation;

    @Value("${mybatis.mapper-locations}")
    private String mapperLocation;

    @Value("${mybatis.application-location}")
    private String applicationLocation;


    @Test
    public void test1() throws IOException {

        System.out.println(applicationLocation);

        DbRegister.mapperGenerateLocationMap.forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });

    }

    @Test
    public void test() throws IOException {

        TableFactory tableFactory = new MySQLTableFactory();
        // 生成Table对象
        SqlTableObject table = TableSchemaService.getSqlTableObject(MyComponent.class, tableFactory);
        // 生成SQL映射
        MyBatisMapperData data = MyBatisMapperService.trainFormTableObject(table);
        // 生成参数
        Context context = MyBatisMapperService.trainToContext(data);
        // 转换xml
        String mapperContext = ThymeleafUtils.processXML(templateLocation, context);
        System.out.println(mapperContext);

        File file = new File("D:\\测试\\" + MyComponent.class.getSimpleName() + ".xml");
        if (!file.exists()) {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(mapperContext);
            fileWriter.close();
        }


    }
}
