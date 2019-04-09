package com.oldguy.example.modules.common.services;

import com.oldguy.example.modules.common.annotation.AssociateEntity;
import com.oldguy.example.modules.common.dto.db.MyBatisMapperData;
import com.oldguy.example.modules.common.dto.db.SqlTableObject;
import com.oldguy.example.modules.common.utils.ReflectUtils;
import com.oldguy.example.modules.common.utils.ThymeleafUtils;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;

import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            if(table.getTargetClass().isAnnotationPresent(AssociateEntity.class)){
                continue;
            }

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
