package com.oldguy.example.modules.common.services;

import com.oldguy.example.modules.common.annotation.AssociateEntity;
import com.oldguy.example.modules.common.annotation.Entity;
import com.oldguy.example.modules.common.dto.db.SqlTableObject;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
