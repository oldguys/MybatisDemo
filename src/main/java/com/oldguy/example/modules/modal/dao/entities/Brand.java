package com.oldguy.example.modules.modal.dao.entities;

import com.oldguy.example.modules.common.annotation.Entity;
import com.oldguy.example.modules.common.dao.entities.BaseEntity;
import lombok.Data;

/**
 * @author huangrenhao
 * @date 2019/1/9
 * @Description： 品牌
 */
@Entity
public class Brand extends BaseEntity {

    private String sequence;

    private String name;

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSequence() {
        return sequence;
    }

    public String getName() {
        return name;
    }
}
