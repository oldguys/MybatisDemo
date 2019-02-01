package com.oldguy.example.modules.common.dto.db;

import com.oldguy.example.modules.common.services.TableFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @author huangrenhao
 * @date 2019/2/1
 */
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

    public String getApplicationLocation() {
        return applicationLocation;
    }

    public void setApplicationLocation(String applicationLocation) {
        this.applicationLocation = applicationLocation;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TableFactory getFactory() {
        return factory;
    }

    public void setFactory(TableFactory factory) {
        this.factory = factory;
    }

    public String getTemplateClassPath() {
        return templateClassPath;
    }

    public void setTemplateClassPath(String templateClassPath) {
        this.templateClassPath = templateClassPath;
    }

    public List<String> getPackageNames() {
        return packageNames;
    }

    public void setPackageNames(List<String> packageNames) {
        this.packageNames = packageNames;
    }
}
