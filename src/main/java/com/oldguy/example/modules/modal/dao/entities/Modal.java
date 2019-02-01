package com.oldguy.example.modules.modal.dao.entities;

import com.oldguy.example.modules.common.annotation.Entity;
import com.oldguy.example.modules.common.dao.entities.BaseEntity;
import lombok.Data;

/**
 * @author huangrenhao
 * @date 2019/1/9
 * @Description： 系列
 */
@Entity
public class Modal extends BaseEntity {

    private String brandSequence;

    private String sequence;

    private String name;

    public void setBrandSequence(String brandSequence) {
        this.brandSequence = brandSequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrandSequence() {
        return brandSequence;
    }

    public String getSequence() {
        return sequence;
    }

    public String getName() {
        return name;
    }
}
