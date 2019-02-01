package com.oldguy.example.modules.modal.dto.json;

import com.oldguy.example.modules.modal.dao.entities.Store;
import lombok.Data;

/**
 * @author huangrenhao
 * @date 2019/1/10
 */
@Data
public class StoreInfo extends Store {

    private String sequenceList;
}
