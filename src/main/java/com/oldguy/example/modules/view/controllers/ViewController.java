package com.oldguy.example.modules.view.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author huangrenhao
 * @date 2019/1/10
 */
@RequestMapping("view")
@Controller
public class ViewController {

    @RequestMapping("Computer/page")
    public String computer(){
        return "Computer";
    }

    @RequestMapping("MyComponent/page")
    public String myComponent(){
        return "MyComponent";
    }

    @RequestMapping("Stock/page")
    public String stock(){
        return "Stock";
    }

    @RequestMapping("Store/page")
    public String store(){
        return "Store";
    }

    @RequestMapping("Modal/page")
    public String Modal(){
        return "Modal";
    }

    @RequestMapping("Brand/page")
    public String brand(){
        return "Brand";
    }
}
