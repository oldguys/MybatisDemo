package com.oldguy.example.modules.modal.controllers;

import com.baomidou.mybatisplus.plugins.Page;
import com.oldguy.example.modules.common.dto.BootstrapTablePage;
import com.oldguy.example.modules.modal.dao.jpas.StoreMapper;
import com.oldguy.example.modules.modal.dto.form.StockQueryForm;
import com.oldguy.example.modules.modal.dto.form.StoreQueryForm;
import com.oldguy.example.modules.modal.dto.json.StockInfo;
import com.oldguy.example.modules.modal.dto.json.StoreInfo;
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
@RequestMapping("Store")
public class StoreController {

    @Autowired
    private StoreMapper storeMapper;

    @GetMapping("page")
    public BootstrapTablePage page(StoreQueryForm form) {

        Page<StoreInfo> page = form.trainToPage();
        List<StoreInfo> records = storeMapper.findByPage(page, form);
        page.setRecords(records);

        return new BootstrapTablePage(page);
    }
}
