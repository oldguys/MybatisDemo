package com.oldguy.example.modules.modal.dto.json;

import com.oldguy.example.modules.modal.dao.entities.Stock;
import lombok.Data;

/**
 * @author huangrenhao
 * @date 2019/1/9
 */
@Data
public class StockInfo extends Stock {

    /**
     *  类别
     */
    private String typeName;

    /**
     *  品牌
     */
    private String brandName;

    /**
     *  系列
     */
    private String modalName;

    /**
     *  主机名称
     */
    private String computerName;

    /**
     *  配件名称
     */
    private String componentName;

    /**
     *  实体ID
     */
    private Long entityId;

    /**
     *  品牌序列号
     */
    private String brandSequence;

    /**
     *  系列序列号
     */
    private String modalSequence;

    /**
     *  仓库名
     */
    private String storeName;


}
