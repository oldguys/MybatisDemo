package com.oldguy.example.modules.modal.controllers;

import com.baomidou.mybatisplus.plugins.Page;
import com.oldguy.example.modules.modal.dao.entities.Computer;
import com.oldguy.example.modules.modal.dao.jpas.ComputerMapper;
import com.oldguy.example.modules.modal.dto.form.ComputerQueryForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author huangrenhao
 * @date 2019/1/9
 */
@RestController
@RequestMapping("Computer")
public class ComputerController {

    @Autowired
    private ComputerMapper computerMapper;

    @GetMapping("all")
    public List<Computer> getList(Integer status) {
        return computerMapper.findAllByStatus(status);
    }


    @GetMapping("page")
    public Page<Computer> page(ComputerQueryForm form) {

        Page<Computer> page = form.trainToPage();
        List<Computer> records = computerMapper.findByPage(page, form);
        page.setRecords(records);

        return page;
    }
}
