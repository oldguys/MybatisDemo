package com.oldguy.example.modules.modal.controllers;

import com.baomidou.mybatisplus.plugins.Page;
import com.oldguy.example.modules.common.dto.BootstrapTablePage;
import com.oldguy.example.modules.modal.dao.entities.Computer;
import com.oldguy.example.modules.modal.dao.jpas.StockMapper;
import com.oldguy.example.modules.modal.dto.form.ComputerQueryForm;
import com.oldguy.example.modules.modal.dto.form.StockQueryForm;
import com.oldguy.example.modules.modal.dto.json.StockInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author huangrenhao
 * @date 2019/1/10
 */
@RestController
@RequestMapping("Stock")
public class StockController {


    @Autowired
    private StockMapper stockMapper;

    @GetMapping("page")
    public BootstrapTablePage page(StockQueryForm form) {

        Page<StockInfo> page = form.trainToPage();
        List<StockInfo> records = stockMapper.findByPage(page, form);
        page.setRecords(records);

        BootstrapTablePage<StockInfo> bootstrapTablePage = new BootstrapTablePage(page);

        return bootstrapTablePage;
    }
}
