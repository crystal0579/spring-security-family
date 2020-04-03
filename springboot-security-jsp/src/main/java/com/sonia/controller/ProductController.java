package com.sonia.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/product")
public class ProductController {

//    @RequestMapping  //那么这里就是直接访问 "/product" 母目录就行了
//    @ResponseBody
//    public String findAll(){
//        return "success";
//    }

    @Secured("ROLE_PRODUCT")
    @RequestMapping("/findAll")
    public String findAll(){
        return "product-list";
    }
}
