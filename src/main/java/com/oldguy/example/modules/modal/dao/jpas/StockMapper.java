package com.oldguy.example.modules.modal.dao.jpas;

import com.baomidou.mybatisplus.plugins.Page;
import com.oldguy.example.modules.common.dao.jpas.BaseEntityMapper;
import com.oldguy.example.modules.modal.dao.entities.Computer;
import com.oldguy.example.modules.modal.dao.entities.Stock;
import com.oldguy.example.modules.modal.dto.form.StockQueryForm;
import com.oldguy.example.modules.modal.dto.json.StockInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author huangrenhao
 * @date 2019/1/9
 */
@Repository
public interface StockMapper extends BaseEntityMapper<Stock> {

    /**
     *  分页获取数据
     * @param page
     * @param form
     * @return
     */
    List<StockInfo> findByPage(Page<StockInfo> page,@Param("form") StockQueryForm form);
}
