package com.oldguy.example.common;

import org.junit.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * @author huangrenhao
 * @date 2019/2/1
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class MapperProducerTest {

    //    @Value("${mybatis.template-location}")
    private String templateLocation = "configs/mapper/mybatis-template.xml";

    private String absoPath = "E:\\资料备份\\后端\\demo\\MyBatisDemo\\src\\main\\resources\\configs\\mapper\\mybatis-template.xml";

    @Test
    public void test() {

//        FileTemplateResolver templateResolver = new FileTemplateResolver();


        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".xml");
        templateResolver.setTemplateMode(TemplateMode.XML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("mapperLocation","com.oldguy.example.modules.common.dao.jpas.BaseEntityMapper");
        context.setVariable("tableName","brand");
        context.setVariable("entityName","Brand");


        StringBuilder builder = new StringBuilder();
        builder .append("\n ").append("\t ").append("${prefix}.").append("id").append(" ,").append("\n")
                .append("\t ").append("${prefix}.").append("create_time").append(" ,").append("\n")
                .append("\t ").append("${prefix}.").append("status").append("\n").append("\t ");


        context.setVariable("columnNames",builder.toString());

        String message = templateEngine.process(templateLocation, context);
//        String message = templateEngine.process(absoPath, context);

        System.out.println(message);

        System.out.println(templateLocation);

    }

}
