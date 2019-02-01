package com.oldguy.example.modules.modal.dao.jpas;

import com.baomidou.mybatisplus.plugins.Page;
import com.oldguy.example.modules.common.dao.jpas.BaseEntityMapper;
import com.oldguy.example.modules.modal.dao.entities.Stock;
import com.oldguy.example.modules.modal.dao.entities.Store;
import com.oldguy.example.modules.modal.dto.form.StoreQueryForm;
import com.oldguy.example.modules.modal.dto.json.StoreInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author huangrenhao
 * @date 2019/1/9
 */
@Repository
public interface StoreMapper extends BaseEntityMapper<Store> {

    /**
     *  分页获取仓库
     * @param page
     * @param form
     * @return
     */
    List<StoreInfo> findByPage(Page<StoreInfo> page,@Param("form") StoreQueryForm form);
}
