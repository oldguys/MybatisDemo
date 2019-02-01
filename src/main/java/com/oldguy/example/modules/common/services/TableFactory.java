package com.oldguy.example.modules.common.services;/**
 * Created by Administrator on 2018/10/23 0023.
 */


import com.oldguy.example.modules.common.dto.db.SqlTableObject;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: ren
 * @CreateTime: 2018-10-2018/10/23 0023 13:53
 */
public interface TableFactory {

    /**
     *  显示Table集合
     * @return
     */
    String showTableSQL();

    String getDialect();

    Map<Class, String> getColumnType();

    /**
     *  转换成为 DB Schema
     * @param sqlTableObjects
     * @return
     */
    Map<String, String> trainToDBSchema(List<SqlTableObject> sqlTableObjects);
}
