## SpringBoot+MyBatis 动态建表+动态生成基于模板Mapper

> 之前已经实现了 自动建表，但是仅实现了自动建表功能，而Mybatis还有 Mapper.xml 文件。所以本文将 描述如何使用 通用的Mapper模板，动态生成Mapper.xml
> 并且本文会将原本的代码进行规范。
>
> GitHub  [https://github.com/oldguys/ShiroDemo](https://github.com/oldguys/ShiroDemo)

设计思路：
1. 类似于Jpa的包扫描，发现实体，
2. 将获取到的实体使用注解解析，生成SQL 的schema 实现动态建表；
3. 使用 Thymeleaf 模板引擎，生成动态模板，并保存。

基于MyBatis 思想，可以把 Entity 与 Mapper 之间的关系模板化。

![关系图.png](https://upload-images.jianshu.io/upload_images/14387783-b2180a165ec7398c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

实现动态建表的核心其实很简单，类图如下

![核心类图.png](https://upload-images.jianshu.io/upload_images/14387783-f58a00ae0f667271.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

所有类的关系图

![所有类图.png](https://upload-images.jianshu.io/upload_images/14387783-bf53d5602c1fa8ff.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


由于使用Thymeleaf所以需要引入Maven
```
<dependency>
	<groupId>ognl</groupId>
	<artifactId>ognl</artifactId>
	<version>3.2</version>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

<dependency>
	<groupId>javax.persistence</groupId>
	<artifactId>persistence-api</artifactId>
	<version>1.0</version>
</dependency>

```

配置属性类: com.oldguy.example.modules.common.dto.db.DbRegisterProperties
```
@Data
public class DbRegisterProperties {

    /**
     *  数据库表工厂
     */
    private TableFactory factory;

    /**
     *  Mapper模板 类路径
     */
    private String templateClassPath;

    /**
     *  包目录
     */
    private List<String> packageNames;

    /**
     *  数据源
     */
    private JdbcTemplate jdbcTemplate;

    /**
     *  系统全局目录
     */
    private String applicationLocation;
}
```

注册器： com.oldguy.example.modules.common.services.DbRegister
```

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

```

Schema 生成服务类: com.oldguy.example.modules.common.services.TableSchemaService

```

/**
 * @author ren
 * @date 2019/2/1
 */
public class TableSchemaService {

    /**
     *  转换成为数据集合列表
     * @param factory
     * @param classList
     * @return
     */
    public static List<SqlTableObject> getSqlTableObjectList(TableFactory factory, Collection<Class> classList) {
        List<SqlTableObject> sqlTableObjects = new ArrayList<>();
        for (Class clazz : classList) {
            if (clazz.isAnnotationPresent(Entity.class) || clazz.isAnnotationPresent(AssociateEntity.class)) {
                sqlTableObjects.add(TableSchemaService.getSqlTableObject(clazz, factory));
            }
        }
        return sqlTableObjects;
    }

    /**
     * 转换为 Table 对象
     *
     * @param clazz
     * @param factory
     * @return
     */
    public static SqlTableObject getSqlTableObject(Class clazz, TableFactory factory) {

        SqlTableObject obj = new SqlTableObject();
        obj.setTableFactory(factory);
        obj.setTargetClass(clazz);

        String tableName = "";
        String preIndex = "";

        if (clazz.isAnnotationPresent(Entity.class)) {
            Entity annotation = (Entity) clazz.getAnnotation(Entity.class);
            tableName = annotation.name();
            preIndex = annotation.pre();
        } else if (clazz.isAnnotationPresent(AssociateEntity.class)) {
            AssociateEntity annotation = (AssociateEntity) clazz.getAnnotation(AssociateEntity.class);
            tableName = annotation.name();
            preIndex = annotation.pre();
        }

        tableName = StringUtils.isEmpty(tableName) ? preIndex + formatTableName(clazz.getSimpleName()) : tableName;
        obj.setTableName(tableName);

        // 配置字段
        List<Field> fields = new ArrayList<>();
        getAllDeclareFields(clazz, fields);
        setTableColumns(obj, fields);
        return obj;
    }

    /**
     * 设置表格字段
     *
     * @param obj
     * @param fields
     */
    private static void setTableColumns(SqlTableObject obj, List<Field> fields) {

        List<SqlTableObject.Column> columnList = new ArrayList<>();
        for (Field field : fields) {
            if (obj.getTableFactory().getColumnType().containsKey(field.getType())) {

                SqlTableObject.Column column = new SqlTableObject.Column();

                if (field.isAnnotationPresent(Id.class)) {
                    column.setPrimaryKey(true);
                    if (field.isAnnotationPresent(GeneratedValue.class)) {
                        GeneratedValue annotation = field.getAnnotation(GeneratedValue.class);
                        if (annotation.strategy().equals(GenerationType.AUTO)) {
                            column.setAutoIncrement(true);
                        }
                    }
                }

                if (field.isAnnotationPresent(Column.class)) {
                    Column annotation = field.getAnnotation(Column.class);

                    if (!StringUtils.isEmpty(annotation.name())) {
                        column.setName(annotation.name());
                    } else {
                        column.setName(formatTableName(field.getName()));
                    }

                    if (!StringUtils.isEmpty(annotation.columnDefinition())) {
                        column.setType(annotation.columnDefinition());
                    } else {
                        column.setType(obj.getTableFactory().getColumnType().get(field.getType()));
                    }

                    column.setLength(annotation.length());
                    column.setUnique(annotation.unique());
                    column.setNullable(annotation.nullable());
                } else {
                    column.setName(formatTableName(field.getName()));
                    column.setType(obj.getTableFactory().getColumnType().get(field.getType()));
                }
                columnList.add(column);
            }
        }
        obj.setColumns(columnList);
    }


    /**
     * 获取所有的 字段
     *
     * @param clazz
     * @param fields
     */
    public static void getAllDeclareFields(Class clazz, List<Field> fields) {
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        if (!clazz.getSuperclass().equals(Object.class)) {
            getAllDeclareFields(clazz.getSuperclass(), fields);
        }
    }

    /**
     * 驼峰转双峰
     *
     * @param name
     * @return
     */
    public static String formatTableName(String name) {
        StringBuilder formatResult = new StringBuilder();
        char[] upperCaseArrays = name.toUpperCase().toCharArray();
        char[] defaultArrays = name.toCharArray();

        for (int i = 0; i < upperCaseArrays.length; i++) {
            if (i == 0) {
                formatResult.append(String.valueOf(defaultArrays[0]).toLowerCase());
                continue;
            }
            if (defaultArrays[i] == upperCaseArrays[i]) {
                formatResult.append("_" + String.valueOf(defaultArrays[i]).toLowerCase());
            } else {
                formatResult.append(defaultArrays[i]);
            }
        }
        return formatResult.toString();
    }


}

```
Mapper 模板生成器类：com.oldguy.example.modules.common.services.MyBatisMapperService

```

/**
 * @author ren
 * @date 2019/2/1
 */
public class MyBatisMapperService {

    /**
     * 转换成为 Context对象
     *
     * @param tables
     * @return
     */
    public static Map<String, String> trainToMapperContext(List<SqlTableObject> tables, String classpath) {
        Map<String, String> map = new HashMap<>(tables.size());
        for (SqlTableObject table : tables) {
            Context context = trainToContext(trainFormTableObject(table));
            String mapperContext = ThymeleafUtils.processXML(classpath, context);
            map.put(table.getTargetClass().getSimpleName(), mapperContext);
        }
        return map;
    }

    /**
     * @param table
     * @return
     */
    public static MyBatisMapperData trainFormTableObject(SqlTableObject table) {

        MyBatisMapperData object = new MyBatisMapperData();

        // 设置表名
        object.setTableName(table.getTableName());
        object.setEntityName(table.getTargetClass().getSimpleName());

        object.setMapperLocation(setMapperLocation(table.getTargetClass()));

        // 配置列
        List<String> columnNameList = getColumnNameList(table.getColumns());
        object.setPerfixsColumnNames(setPerfixsColumnNames(columnNameList));
        object.setColumnNames(setColumnNames(columnNameList));

        // 获取字段名
        List<String> fieldNameList = new ArrayList<>();
        getFieldNameListByClass(table.getTargetClass(), fieldNameList);

        // 设置
        object.setInsertFields(setInsertFields(fieldNameList));
        object.setInsertBatchFields(setInsertBatchFields(fieldNameList));
        object.setUpdateFields(setUpdateFields(fieldNameList, columnNameList));
        object.setUpdateBatchFields(setUpdateBatchFields(fieldNameList, columnNameList));

        return object;
    }

    public static String setMapperLocation(Class clazz) {

        String className = clazz.getName();
        String location = className.substring(0, className.indexOf("dao") + 3) + ".jpas." + clazz.getSimpleName() + "Mapper";
        return location;
    }

    public static Context trainToContext(MyBatisMapperData data) {
        Context context = new Context();

        Field[] fields = data.getClass().getDeclaredFields();

        Map<String, String> fieldMethodMap = new HashMap<>(fields.length);

        for (Field field : fields) {
            String fieldName = field.getName();
            fieldMethodMap.put(ReflectUtils.tranFieldToGetterMethodName(fieldName), fieldName);
        }

        Method[] methods = data.getClass().getDeclaredMethods();
        for (Method method : methods) {

            String fieldName = fieldMethodMap.get(method.getName());
            if (StringUtils.isEmpty(fieldName)) {
                continue;
            }
            try {
                Object value = method.invoke(data);
                context.setVariable(fieldName, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return context;
    }

    /**
     * 配置批量更新字段
     *
     * @param fields
     * @param columns
     * @return
     */
    private static String setUpdateBatchFields(List<String> fields, List<String> columns) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n ");

        for (int i = 0; i < fields.size(); i++) {

            String field = fields.get(i);
            String column = columns.get(i);

            if (field.equals("id") || field.equals("createTime")) {
                continue;
            }

            builder.append("\t ").append(column).append(" = ").append("#{item.").append(field).append("}");

            if (i < fields.size() - 1) {
                builder.append(",").append("\n");
            } else {
                builder.append("\n").append("\t ");
            }
        }

        return builder.toString();
    }

    /**
     * 设置更新字段
     *
     * @param fields
     * @param columns
     * @return
     */
    private static String setUpdateFields(List<String> fields, List<String> columns) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n ");

        for (int i = 0; i < fields.size(); i++) {

            String field = fields.get(i);
            String column = columns.get(i);

            if (field.equals("id") || field.equals("createTime")) {
                continue;
            }

            builder.append("\t ").append(column).append(" = ").append("#{").append(field).append("}");

            if (i < fields.size() - 1) {
                builder.append(",").append("\n");
            } else {
                builder.append("\n").append("\t ");
            }
        }

        return builder.toString();
    }

    /**
     * 批量插入字段
     * 剔除ID
     *
     * @param list
     * @return
     */
    private static String setInsertBatchFields(List<String> list) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n ");

        for (int i = 0; i < list.size(); i++) {

            String columnName = list.get(i);

            if (columnName.equals("id")) {
                continue;
            }

            builder.append("\t\t #{item.").append(columnName).append("}");

            if (i < list.size() - 1) {
                builder.append(",").append("\n");
            } else {
                builder.append("\n").append("\t ");
            }
        }

        return builder.toString();
    }

    /**
     * 插入字段
     * 剔除 ID
     *
     * @param list
     * @return
     */
    private static String setInsertFields(List<String> list) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n ");

        for (int i = 0; i < list.size(); i++) {

            String columnName = list.get(i);

            if (columnName.equals("id")) {
                continue;
            }

            builder.append("\t #{").append(columnName).append("}");

            if (i < list.size() - 1) {
                builder.append(",").append("\n");
            } else {
                builder.append("\n").append("\t ");
            }
        }

        return builder.toString();
    }

    /**
     * 设置持久化列名
     *
     * @param list
     * @return
     */
    private static String setColumnNames(List<String> list) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n ");

        for (int i = 0; i < list.size(); i++) {

            String columnName = list.get(i);

            if (columnName.equals("id")) {
                continue;
            }

            builder.append("\t ").append(columnName);

            if (i < list.size() - 1) {
                builder.append(",").append("\n");
            } else {
                builder.append("\n").append("\t ");
            }
        }

        return builder.toString();
    }

    /**
     * 配置显示前缀列名
     *
     * @param list
     * @return
     */
    private static String setPerfixsColumnNames(List<String> list) {

        StringBuilder builder = new StringBuilder();
        builder.append("\n ");
        for (int i = 0; i < list.size(); i++) {

            builder.append("\t ").append("${prefix}.").append(list.get(i));

            if (i < list.size() - 1) {
                builder.append(",").append("\n");
            } else {
                builder.append("\n").append("\t ");
            }
        }
        return builder.toString();
    }

    /**
     * 获取列名
     *
     * @param list
     * @return
     */
    private static List<String> getColumnNameList(List<SqlTableObject.Column> list) {
        List<String> columnNameList = new ArrayList<>();

        for (SqlTableObject.Column column : list) {
            columnNameList.add(column.getName());
        }
        return columnNameList;
    }

    /**
     * 获取所有类的字段名
     *
     * @param clazz
     * @param fieldNameList
     */
    private static void getFieldNameListByClass(Class clazz, List<String> fieldNameList) {

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            fieldNameList.add(field.getName());
        }

        if (!clazz.getSuperclass().equals(Object.class)) {
            getFieldNameListByClass(clazz.getSuperclass(), fieldNameList);
        }
    }


}

```

Thymeleaf 模板处理工具类
```

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

```

属性文件配置
```
spring:
  datasource:
    username: root
    password: root
    url: jdbc:mysql://127.0.0.1:3306/mybatis_demo?useUnicode=true&characterEncoding=utf8&useSSL=false&allowMultiQueries=true
    driver-class-name: com.mysql.jdbc.Driver
    type: com.alibaba.druid.pool.DruidDataSource
  jackson:
    date-format: yyyy-MM-dd
    time-zone: GMT+8
  mvc:
    date-format: yyyy-MM-dd
  thymeleaf:
    cache: false
mybatis:
  config-location: classpath:configs/myBatis-config.xml
  type-aliases-package: com.oldguy.example.modules.modal.dao
  mapper-locations: classpath:mappers/*.xml
  template-location: configs/mapper/mybatis-template.xml
  application-location: F://workspace//java//idea//MyBatisDemo
  mapper-generate-config-location: configs/mapper/mybatis-mapper-generate-config.perporties
server:
  port: 8082
```

基于 Thymeleaf 的 Mapper 抽象模板
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper th:attr="namespace=${mapperLocation}">

    <sql id="table_name" th:text="${tableName}"></sql>

    <sql id="all_entity_columns" th:text="${perfixsColumnNames}"></sql>

    <select id="findByPage" th:attr="resultType=${entityName}">
        SELECT
        <include refid="all_entity_columns">
            <property name="prefix" value="a"/>
        </include>
        FROM
        <include refid="table_name"/> a
        <where>
            <if test="form.status != null">
                AND a.`status` = #{form.status}
            </if>
            <if test="form.startTime != null ">
                AND a.create_time &gt; #{form.startTime}
            </if>
            <if test="form.endTime != null ">
                AND a.create_time &lt; #{form.endTime}
            </if>
        </where>
        <choose>
            <when test="form.sort != null and form.sort == 1">
                ORDER BY a.id DESC
            </when>
            <when test="form.sort != null and form.sort == 0">
                ORDER BY a.id ASC
            </when>
            <otherwise>
                ORDER BY a.id DESC
            </otherwise>
        </choose>
    </select>


    <select id="findOne" parameterType="long"  th:attr="resultType=${entityName}">
        SELECT
        <include refid="all_entity_columns">
            <property name="prefix" value="a"/>
        </include>
        FROM
        <include refid="table_name"/>
        a
        WHERE
        id = #{id}
    </select>

    <select id="findAllByStatus" parameterType="int"  th:attr="resultType=${entityName}">
        SELECT
        <include refid="all_entity_columns">
            <property name="prefix" value="a"/>
        </include>
        FROM
        <include refid="table_name"/> a
        <where>
            <choose>
                <when test="status != null">
                    `status` = #{status}
                </when>
            </choose>
        </where>
    </select>

    <update id="update" th:attr="parameterType=${entityName}">
        UPDATE
        <include refid="table_name"/>
        SET
        [[${updateFields}]]
        WHERE
        id = #{id}
    </update>

    <update id="updateBatch" >
        <foreach collection="collection" item="item" separator=";">
            UPDATE
            <include refid="table_name"/>
            SET
            [[${updateBatchFields}]]
            WHERE
            id = #{item.id}
        </foreach>
    </update>

    <update id="updateStatus">
        UPDATE
        <include refid="table_name"/>
        SET
        `status` = #{status}
        WHERE
        id = #{id}
    </update>

    <insert id="save" th:attr="parameterType=${entityName}" keyProperty="id" useGeneratedKeys="true">
        INSERT
        <include refid="table_name"/>
        ([[${columnNames}]])
        VALUES
        ([[${insertFields}]])
    </insert>

    <insert id="saveBatch" th:attr="parameterType=${entityName}" keyProperty="id" useGeneratedKeys="true">
        INSERT
        <include refid="table_name"/>
        ([[${columnNames}]])
        VALUES
        <foreach collection="collections" item="item" separator=",">
        ([[${insertBatchFields}]])
        </foreach>
    </insert>
</mapper>
```

 ## 
最后依然存在一个问题 由于MyBatis 容器先于 自动配置，所以默认使用MyBatis 完全自动装配时，需要在生成Mapper后，重启，不然会报找不到Mapper异常。
想到解决方案 （然后好像没有什么乱用）
@AutoConfigureBefore(MybatisAutoConfiguration.class)