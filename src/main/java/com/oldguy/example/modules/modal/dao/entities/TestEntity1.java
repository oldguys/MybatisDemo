package com.oldguy.example.modules.modal.dao.entities;

import com.oldguy.example.modules.common.annotation.Entity;
import com.oldguy.example.modules.common.dao.entities.Equipment;

/**
 * @author huangrenhao
 * @date 2019/2/1
 */
@Entity
public class TestEntity1 extends Equipment {

    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
