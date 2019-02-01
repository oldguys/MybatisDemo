package com.oldguy.example.modules.common.utils;/**
 * Created by Administrator on 2018/9/20 0020.
 */


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Description:
 * @Author: ren
 * @CreateTime: 2018-09-2018/9/20 0020 22:05
 */
public class Log4jUtils {
    public static Log getInstance(Class clazz) {
        return LogFactory.getLog(clazz);
    }
}
