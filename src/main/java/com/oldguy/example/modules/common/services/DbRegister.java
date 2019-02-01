package com.oldguy.example.modules.common.services;


import com.oldguy.example.modules.common.dto.db.DbRegisterProperties;
import com.oldguy.example.modules.common.dto.db.SqlTableObject;
import com.oldguy.example.modules.common.services.impls.MySQLTableFactory;
import com.oldguy.example.modules.common.utils.ClassUtils;
import com.oldguy.example.modules.common.utils.Log4jUtils;
import com.oldguy.example.modules.common.utils.ThymeleafUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @Description:
 * @Author: ren
 * @CreateTime: 2018-10-2018/10/16 0016 10:33
 */
public class DbRegister {

    /**
     * mapper生成期映射路局
     */
    public final static Map<String, String> mapperGenerateLocationMap = new HashMap<>();

    /**
     * 注册
     *
     * @param properties
     */
    public void register(DbRegisterProperties properties) {

        Map<String, String> classPackageMap = new HashMap<>(16);
        // 获取扫描的类
        List<Class> classList = new ArrayList<>();
        for (String packageName : properties.getPackageNames()) {

            List<Class<?>> itemList = ClassUtils.getClasses(packageName);
            itemList.forEach(obj -> {
                classPackageMap.put(obj.getSimpleName(), packageName);
            });
            classList.addAll(itemList);
        }

        // 获取数据对象
        List<SqlTableObject> list = TableSchemaService.getSqlTableObjectList(properties.getFactory(), classList);
        // 转换成为Schema
        Map<String, String> schemaMap = properties.getFactory().trainToDBSchema(list);
        // 获取已存在的 Table 表
        Set<String> tableList = getExistTables(properties.getJdbcTemplate(), properties.getFactory());
        // 初始化数据库
        initTableSchema(properties.getJdbcTemplate(), tableList, schemaMap);

        // 转换成为context
        Map<String, String> mapperContextMap = MyBatisMapperService.trainToMapperContext(list, properties.getTemplateClassPath());
        initMyBatisMapperXML(properties, mapperContextMap, classPackageMap);

    }

    /**
     * 生成 Mapper
     *
     * @param properties
     * @param mapperContextMap
     */
    private void initMyBatisMapperXML(DbRegisterProperties properties, Map<String, String> mapperContextMap, Map<String, String> classPackageMap) {

        mapperContextMap.forEach((k, v) -> {

            String mapperContext = mapperContextMap.get(k);
            if (StringUtils.isEmpty(mapperContext)) {
                Log4jUtils.getInstance(getClass()).info("没有找到 class: [ " + k + " ] 的映射文件!");
                return;
            }

            String packageName = classPackageMap.get(k);
            if (StringUtils.isEmpty(packageName)) {
                Log4jUtils.getInstance(getClass()).info("没有找到 class: [ " + k + " ] 的包路径!");
                return;
            }
            String location = DbRegister.mapperGenerateLocationMap.get(packageName);
            if (StringUtils.isEmpty(location)) {
                Log4jUtils.getInstance(getClass()).info("没有找到 class: [ " + k + " ] 的配置路径!");
                return;
            }

            String absolutePath = properties.getApplicationLocation() + location + k + ".xml";
            // 创建文件
            createMapperFile(absolutePath, mapperContext);


        });

    }

    private void createMapperFile(String absolutePath, String mapperContext) {

        File file = new File(absolutePath);
        if (!file.exists()) {
            Log4jUtils.getInstance(getClass()).info("创建文件:" + absolutePath);
            try {
                file.createNewFile();
                // 编写文件
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(mapperContext);
                fileWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log4jUtils.getInstance(getClass()).info("文件已存在:" + absolutePath);
        }

    }

    /**
     * 初始化数据库
     *
     * @param jdbcTemplate
     * @param existTables
     * @param schemaMap
     */
    public static void initTableSchema(JdbcTemplate jdbcTemplate, Set<String> existTables, Map<String, String> schemaMap) {

        for (String key : schemaMap.keySet()) {
            if (!existTables.contains(key)) {
                Log4jUtils.getInstance(DbRegister.class).info("未找到表[" + key + "],进行创建.");
                String sql = schemaMap.get(key);
                if (sql.trim().length() > 0) {
                    jdbcTemplate.execute(sql);
                    Log4jUtils.getInstance(DbRegister.class).info("\n\n" + sql);
                }
            } else {
                Log4jUtils.getInstance(DbRegister.class).info("表[" + key + "] 已存在");
            }
        }
    }

    /**
     * 获取已存在的Table集合
     *
     * @param jdbcTemplate
     * @param tableFactory
     * @return
     */
    public static Set<String> getExistTables(JdbcTemplate jdbcTemplate, TableFactory tableFactory) {
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(tableFactory.showTableSQL());
        Set<String> tableNameSet = new HashSet<>();
        for (Map<String, Object> item : mapList) {
            for (String key : item.keySet()) {
                tableNameSet.add((String) item.get(key));
            }
        }
        return tableNameSet;
    }
}
