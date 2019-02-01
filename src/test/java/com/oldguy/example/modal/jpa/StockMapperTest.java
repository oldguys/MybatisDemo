package com.oldguy.example.modal.jpa;

import com.oldguy.example.modules.common.dao.jpas.BaseEntityMapper;
import com.oldguy.example.modules.common.utils.ReflectUtils;
import com.oldguy.example.modules.modal.dao.entities.Computer;
import com.oldguy.example.modules.modal.dao.entities.MyComponent;
import com.oldguy.example.modules.modal.dao.entities.Stock;
import com.oldguy.example.modules.modal.dao.jpas.ComputerMapper;
import com.oldguy.example.modules.modal.dao.jpas.MyComponentMapper;
import com.oldguy.example.modules.modal.dao.jpas.StockMapper;
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
public class StockMapperTest {


    @Autowired
    private ComputerMapper computerMapper;
    @Autowired
    private MyComponentMapper myComponentMapper;
    @Autowired
    private StockMapper stockMapper;

    @Test
    public void testSaveBatch() {

        List<Stock> list = new ArrayList<>();

        List<Computer> computerList = computerMapper.findAllByStatus(1);
        int index = 1;

        Stock template = new Stock();
        for (int i = 0; i < computerList.size(); i++) {

            if ((i + 1) % 5 == 0) {
                index++;
            }
            template.setStoreSequence("store-" + index);
            template.setSequence(computerList.get(i).getSequence());
            template.setType(Computer.class.getSimpleName());

            list.add(newInstance(template));
        }

        List<MyComponent> myComponentList = myComponentMapper.findAllByStatus(1);
        index = 20;
        for (int i = 0; i < myComponentList.size(); i++) {

            if ((i + 1) % 10 == 0) {
                index++;
            }
            template.setStoreSequence("store-" + index);
            template.setSequence(myComponentList.get(i).getSequence());
            template.setType(MyComponent.class.getSimpleName());

            list.add(newInstance(template));
        }

        stockMapper.saveBatch(list);
    }

    public Stock newInstance(Stock template) {
        Stock stock = new Stock();

        ReflectUtils.updateFieldByClass(template, stock);
        BaseEntityMapper.initSave(stock);

        return stock;
    }
}
