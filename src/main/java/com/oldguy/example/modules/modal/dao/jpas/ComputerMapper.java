package com.oldguy.example.modules.modal.dao.jpas;

import com.baomidou.mybatisplus.plugins.Page;
import com.oldguy.example.modules.common.dao.jpas.BaseEntityMapper;
import com.oldguy.example.modules.modal.dao.entities.Computer;
import com.oldguy.example.modules.modal.dto.form.ComputerQueryForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author huangrenhao
 * @date 2019/1/9
 */
@Repository
public interface ComputerMapper extends BaseEntityMapper<Computer> {

    /**
     *  分页获取
     * @param page
     * @param form
     * @return
     */
    List<Computer> findByPage(Page<Computer> page,@Param("form") ComputerQueryForm form);
}
