package com.oldguy.example.modal.jpa;

import com.oldguy.example.modules.common.dao.entities.BaseEntity;
import com.oldguy.example.modules.common.dao.jpas.BaseEntityMapper;
import com.oldguy.example.modules.modal.dao.entities.Brand;
import com.oldguy.example.modules.modal.dao.entities.Modal;
import com.oldguy.example.modules.modal.dao.jpas.BrandMapper;
import com.oldguy.example.modules.modal.dao.jpas.ModalMapper;
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
public class ModalMapperTest {

    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private ModalMapper modalMapper;

    @Test
    public void testSaveBatch() {

        List<Brand> brandList = brandMapper.findAllByStatus(null);

        List<Modal> list = new ArrayList<>();
        for (Brand obj : brandList) {
            for (int i = 1; i < 6; i++) {
                list.add(newInstance("系列-" + i, "modal-" + i, obj.getSequence()));
            }
        }
        modalMapper.saveBatch(list);


    }

    public Modal newInstance(String name, String sequence, String brandSequence) {
        Modal entity = new Modal();
        entity.setName(name);
        entity.setSequence(sequence);
        entity.setBrandSequence(brandSequence);
        BaseEntityMapper.initSave(entity);

        return entity;
    }
}
