package com.oldguy.example.modules.modal.dao.entities;

import com.oldguy.example.modules.common.annotation.Entity;
import com.oldguy.example.modules.common.dao.entities.Equipment;
import lombok.Data;

/**
 * @author huangrenhao
 * @date 2019/1/9
 * @Description： 部件
 */
@Entity
public class MyComponent extends Equipment {

    private String computerSequence;

    private String name;

    public String getComputerSequence() {
        return computerSequence;
    }

    public void setComputerSequence(String computerSequence) {
        this.computerSequence = computerSequence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
