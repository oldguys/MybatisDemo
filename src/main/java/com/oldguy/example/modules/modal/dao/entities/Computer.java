package com.oldguy.example.modules.modal.dao.entities;

import com.oldguy.example.modules.common.annotation.Entity;
import com.oldguy.example.modules.common.dao.entities.Equipment;
import lombok.Data;

/**
 * @author huangrenhao
 * @date 2019/1/9
 * @Description： 电脑
 */
@Data
@Entity
public class Computer extends Equipment {

    private String name;

    public void setName(String name) {
        this.name = name;
    }
}
