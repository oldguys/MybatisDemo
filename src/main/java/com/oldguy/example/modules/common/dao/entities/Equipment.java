package com.oldguy.example.modules.common.dao.entities;

import lombok.Data;

/**
 * @author huangrenhao
 * @date 2019/1/9
 */
public abstract class Equipment extends BaseEntity {

    /**
     *  品牌
     */
    private String brandSequence;

    /**
     *  系列
     */
    private String modalSequence;

    /**
     *  序列号
     */
    private String sequence;

    public void setBrandSequence(String brandSequence) {
        this.brandSequence = brandSequence;
    }

    public void setModalSequence(String modalSequence) {
        this.modalSequence = modalSequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getBrandSequence() {
        return brandSequence;
    }

    public String getModalSequence() {
        return modalSequence;
    }

    public String getSequence() {
        return sequence;
    }
}
