package com.oldguy.example.modal.jpa;

import com.oldguy.example.modules.common.dao.jpas.BaseEntityMapper;
import com.oldguy.example.modules.modal.dao.entities.Store;
import com.oldguy.example.modules.modal.dao.jpas.StoreMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huangrenhao
 * @date 2019/1/10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StoreMapperTest {

    @Autowired
    private StoreMapper storeMapper;

    @Test
    public void testSaveBatch() {

        List<Store> list = new ArrayList<>();

        for (int i = 1; i <51; i++) {
            list.add(newInstance("仓库-" + i, "store-" + i));
        }

        storeMapper.saveBatch(list);
    }

    public Store newInstance(String name, String sequence) {

        Store store = new Store();
        BaseEntityMapper.initSave(store);
        store.setName(name);
        store.setSequence(sequence);
        return store;
    }
}
