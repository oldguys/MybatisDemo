package com.oldguy.example.modules.modal.controllers;

import com.oldguy.example.modules.modal.dao.entities.Brand;
import com.oldguy.example.modules.modal.dao.jpas.BrandMapper;
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
@RequestMapping("Brand")
public class BrandController {

    @Autowired
    private BrandMapper brandMapper;

    @GetMapping("all")
    public List<Brand> getList(Integer status) {
        return brandMapper.findAllByStatus(status);
    }

}
