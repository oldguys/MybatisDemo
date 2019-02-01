package com.oldguy.example.modal.jpa;

import com.oldguy.example.modules.common.dao.jpas.BaseEntityMapper;
import com.oldguy.example.modules.common.utils.ReflectUtils;
import com.oldguy.example.modules.modal.dao.entities.Modal;
import com.oldguy.example.modules.modal.dao.entities.MyComponent;
import com.oldguy.example.modules.modal.dao.jpas.ModalMapper;
import com.oldguy.example.modules.modal.dao.jpas.MyComponentMapper;
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
public class MyComponentMapperTest {


    @Autowired
    private MyComponentMapper myComponentMapper;
    @Autowired
    private ModalMapper modalMapper;


    @Test
    public void testSaveBatch() {

        List<MyComponent> list = new ArrayList<>();

        MyComponent template = new MyComponent();

        List<Modal> modalList = modalMapper.findAllByStatus(1);

        int index = 1;
        for (Modal obj : modalList) {
            for (int i = 1; i < 10; i++) {

                template.setName("配件-" + index);
                template.setSequence("sequence-" + index);
                template.setBrandSequence(obj.getBrandSequence());
                template.setModalSequence(obj.getSequence());
                list.add(newInstance(template));
                index++;
            }
        }

        myComponentMapper.saveBatch(list);
    }

    public MyComponent newInstance(MyComponent template) {

        MyComponent target = new MyComponent();

        ReflectUtils.updateFieldByClass(template, target);
        BaseEntityMapper.initSave(target);

        return target;
    }
}
