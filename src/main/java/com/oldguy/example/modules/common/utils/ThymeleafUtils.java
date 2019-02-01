package com.oldguy.example.modules.common.utils;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * @author ren
 * @date 2019/2/1
 */
public class ThymeleafUtils {

    private static ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    private static TemplateEngine templateEngine = new TemplateEngine();

    static {
        templateResolver.setSuffix(".xml");
        templateResolver.setTemplateMode(TemplateMode.XML);
        templateEngine.setTemplateResolver(templateResolver);
    }

    /**
     *  处理XML 文件
     * @param classPathTemplate 类目录
     * @param context 注入变量
     * @return
     */
    public static String processXML(String classPathTemplate, Context context) {
        return templateEngine.process(classPathTemplate, context);
    }

}
