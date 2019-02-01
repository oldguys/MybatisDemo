package com.oldguy.example.modal.jpa;

import com.oldguy.example.modules.common.dao.jpas.BaseEntityMapper;
import com.oldguy.example.modules.modal.dao.entities.Brand;
import com.oldguy.example.modules.modal.dao.jpas.BrandMapper;
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
public class BrandMapperTest {

    @Autowired
    private BrandMapper brandMapper;

    @Test
    public void testSaveBatch() {

        List<Brand> list = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            list.add(newInstance("品牌-" + i, "brand-" + i));
        }

        brandMapper.saveBatch(list);
    }

    public static Brand newInstance(String name, String sequence) {
        Brand entity = new Brand();
        entity.setName(name);
        entity.setSequence(sequence);
        BaseEntityMapper.initSave(entity);

        return entity;
    }
}
