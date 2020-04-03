package com.sonia.advice;

        import org.springframework.security.access.AccessDeniedException;
        import org.springframework.web.bind.annotation.ControllerAdvice;
        import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class HandleExceptionAdvice {

    @ExceptionHandler(RuntimeException.class)
    public String handleException(RuntimeException e){
        System.out.println("异常拦截类-------------------");
        if (e instanceof AccessDeniedException){
            return "redirect:/403.jsp";
        }
        return "redirect:/500.jsp";
    }
}
