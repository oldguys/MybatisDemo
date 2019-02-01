package com.oldguy.example.modal.jpa;

import com.oldguy.example.modules.modal.dao.entities.TestEntity1;
import com.oldguy.example.modules.modal.dao.jpas.TestEntity1Mapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author huangrenhao
 * @date 2019/2/1
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestEntity1MapperTest {

    @Autowired
    private TestEntity1Mapper testEntity1Mapper;

    public void test(){

    }

    @Test
    public void testSave(){

        TestEntity1 entity1 = new TestEntity1();
        entity1.setModalSequence("setModalSequence");
        entity1.setSequence("setSequence");
        entity1.setBrandSequence("setBrandSequence");
        entity1.setVersion("vs");
        entity1.setStatus(1);

        testEntity1Mapper.save(entity1);
    }
}
