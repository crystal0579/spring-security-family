//package com.itheima.controller.advice;
//
////import java.nio.file.AccessDeniedException;不要导错，是下面这个
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerExceptionResolver;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Component
//public class HandlerControllerException implements HandlerExceptionResolver {
//
//    /**
//     * 从它的入参和出参可以看出，它是最外层的拦截（Http端）
//     * @param httpServletRequest
//     * @param httpServletResponse
//     * @param o 出现异常的对象
//     * @param e 出现异常的信息
//     * @return
//     */
//    @Override
//    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
//        ModelAndView mv = new ModelAndView();
//        //将异常信息放入request域中，不过基本没啥意思
//        mv.addObject("errorMsg", e.getMessage());
//
//        //指定不同的异常处理
//        if (e instanceof AccessDeniedException){
//            mv.setViewName("forward:/403.jsp"); //必须写上 forward 或 redirect(地址栏会变动)
//            //因为 在 spring-mvc中配置了 页面的跳转是通过 /pages 文件夹，并且后缀自动加上 jsp
//        }else{
//            mv.setViewName("redirect:/500.jsp");
//        }
//
//        e.printStackTrace();
//        return mv;
//    }
//}
