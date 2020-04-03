package com.itheima.controller.advice;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    //哪个异常走哪个方式
    @ExceptionHandler(AccessDeniedException.class)
    public String handler403Exception(Exception e){
        System.out.println("这里是AccessDeniedException的处理模式");
        e.printStackTrace();
        return "forward:/403.jsp";
    }

    @ExceptionHandler(RuntimeException.class)
    public String handlerRuntimeException(Exception e){
        System.out.println("这里是RuntimeException的处理模式");
        e.printStackTrace();
        return "redirect:/500.jsp";
    }
}
