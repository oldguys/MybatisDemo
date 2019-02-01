package com.oldguy.example.modules.modal.controllers;

import com.oldguy.example.modules.modal.dao.entities.Modal;
import com.oldguy.example.modules.modal.dao.jpas.ModalMapper;
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
@RequestMapping("Modal")
public class ModalController {

    @Autowired
    private ModalMapper modalMapper;

    @GetMapping("all")
    public List<Modal> getList(Integer status) {
        return modalMapper.findAllByStatus(status);
    }

}
