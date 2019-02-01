package com.oldguy.example.modules.common.dto.db;


import com.oldguy.example.modules.common.services.TableFactory;

import java.util.Collections;
import java.util.List;

/**
 * @Description: 抽象数据表
 * @Author: ren
 * @CreateTime: 2018-10-2018/10/16 0016 14:10
 */
public class SqlTableObject {

    /**
     *  工厂
     */
    private TableFactory tableFactory;

    /**
     *  实体类
     */
    private Class targetClass;

    /**
     *  表名
     */
    private String tableName;

    /**
     *  列
     */
    private List<Column> columns = Collections.emptyList();

    public TableFactory getTableFactory() {
        return tableFactory;
    }

    public void setTableFactory(TableFactory tableFactory) {
        this.tableFactory = tableFactory;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public String getTableName() {
        return tableName;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public static class Column{

        private boolean autoIncrement = false;

        private boolean unique = false;

        private boolean primaryKey = false;

        private boolean nullable = true;

        private String name;

        private String type;

        private Integer length;

        public void setAutoIncrement(boolean autoIncrement) {
            this.autoIncrement = autoIncrement;
        }

        public void setUnique(boolean unique) {
            this.unique = unique;
        }

        public void setNullable(boolean nullable) {
            this.nullable = nullable;
        }

        public void setPrimaryKey(boolean primaryKey) {
            this.primaryKey = primaryKey;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setLength(Integer length) {
            this.length = length;
        }

        public boolean isUnique() {
            return unique;
        }

        public boolean isPrimaryKey() {
            return primaryKey;
        }

        public boolean isNullable() {
            return nullable;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public Integer getLength() {
            return length;
        }

        public boolean isAutoIncrement() {
            return autoIncrement;
        }
    }
}
