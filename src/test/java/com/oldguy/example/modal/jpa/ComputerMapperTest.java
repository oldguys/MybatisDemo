package com.oldguy.example.modal.jpa;

import com.oldguy.example.modules.common.dao.jpas.BaseEntityMapper;
import com.oldguy.example.modules.modal.dao.entities.Computer;
import com.oldguy.example.modules.modal.dao.jpas.ComputerMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huangrenhao
 * @date 2019/1/9
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ComputerMapperTest {


    @Autowired
    private ComputerMapper computerMapper;

    @Test
    public void testFindAllByStatus(){
        List<Computer> list = computerMapper.findAllByStatus(null);
        list.forEach(System.out::println);
    }

    @Test
    public void testSaveBatch() {

        List<Computer> list = new ArrayList<>();

        int nameIndex = 1;
        int brandIndex = 1;
        int modalIndex = 1;

        for (int i = 1; i < 51; i++) {
            if (i % 10 == 0) {
                modalIndex++;
            }
            if (i % 20 == 0) {
                brandIndex++;
            }
            if(i % 5 == 0){
                nameIndex ++;
            }
            list.add(newInstance("name-" + nameIndex, "brand-" + brandIndex, "modal-" + modalIndex,"sequence-"+i ));
        }

        computerMapper.saveBatch(list);
    }

    public Computer newInstance(String name, String brand, String modal, String sequence) {

        Computer obj = new Computer();
        obj.setBrandSequence(brand);
        obj.setModalSequence(modal);
        obj.setSequence(sequence);
        obj.setName(name);
        BaseEntityMapper.initSave(obj);

        return obj;
    }
}
