package com.oldguy.example.modules.common.dto.db;

/**
 * @author ren
 * @date 2019/2/1
 */
public class MyBatisMapperData {

    /**
     *  接口路径
     */
    private String mapperLocation;

    /**
     *  实体名称
     */
    private String entityName;

    /**
     *  字段名称
     */
    private String fieldNames;

    /**
     *  表名称
     */
    private String tableName;

    /**
     *  列名称
     */
    private String columnNames;

    /**
     *  前缀列名
     */
    private String perfixsColumnNames;

    /**
     *  批量更新字段名
     */
    private String updateBatchFields;

    /**
     *  更新字段名
     */
    private String updateFields;

    /**
     *  插入字段名
     */
    private String insertFields;

    /**
     *  批量插入字段名
     */
    private String insertBatchFields;

    public String getMapperLocation() {
        return mapperLocation;
    }

    public void setMapperLocation(String mapperLocation) {
        this.mapperLocation = mapperLocation;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(String fieldNames) {
        this.fieldNames = fieldNames;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String columnNames) {
        this.columnNames = columnNames;
    }

    public String getPerfixsColumnNames() {
        return perfixsColumnNames;
    }

    public void setPerfixsColumnNames(String perfixsColumnNames) {
        this.perfixsColumnNames = perfixsColumnNames;
    }

    public String getUpdateBatchFields() {
        return updateBatchFields;
    }

    public void setUpdateBatchFields(String updateBatchFields) {
        this.updateBatchFields = updateBatchFields;
    }

    public String getUpdateFields() {
        return updateFields;
    }

    public void setUpdateFields(String updateFields) {
        this.updateFields = updateFields;
    }

    public String getInsertFields() {
        return insertFields;
    }

    public void setInsertFields(String insertFields) {
        this.insertFields = insertFields;
    }

    public String getInsertBatchFields() {
        return insertBatchFields;
    }

    public void setInsertBatchFields(String insertBatchFields) {
        this.insertBatchFields = insertBatchFields;
    }
}
