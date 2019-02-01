package com.oldguy.example.modules.modal.dao.entities;

import com.oldguy.example.modules.common.annotation.Entity;
import com.oldguy.example.modules.common.dao.entities.BaseEntity;
import lombok.Data;

/**
 * @author huangrenhao
 * @date 2019/1/9
 * @Description： 库存
 */
@Entity
public class Stock extends BaseEntity {

    /**
     *  类别
     */
    private String type;

    /**
     *  序列号
     */
    private String sequence;

    /**
     *  仓库序列号
     */
    private String storeSequence;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getStoreSequence() {
        return storeSequence;
    }

    public void setStoreSequence(String storeSequence) {
        this.storeSequence = storeSequence;
    }
}
