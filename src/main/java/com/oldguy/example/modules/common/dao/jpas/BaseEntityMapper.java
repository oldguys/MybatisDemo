package com.oldguy.example.modules.common.dao.jpas;

import com.oldguy.example.modules.common.dao.entities.BaseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 	实体映射基类，用于映射BaseEntity的子类
 * @ClassName: BaseEntityMapper 
 * @author huangrenhao
 * @Description: TODO
 * @date 2017年12月4日 上午10:44:55 
 * @version V1.0   
 * @param <T>
 */
public interface BaseEntityMapper<T> extends BaseMapper<T,Long>{

    static void initSave(BaseEntity obj) {
        obj.setCreateTime(new Date());
        obj.setStatus(1);
    }

    /**
     *  获取 List<T> 列表
     *  status :
     *      1 - 有效
     *      0 - 无效
     *      null -> 所有
     * @param status
     * @return
     */
    List<T> findAllByStatus(@Param("status") Integer status);

    /**
     * 	修改 T 状态
     * @param id
     * @param status
     * @return
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);


}
