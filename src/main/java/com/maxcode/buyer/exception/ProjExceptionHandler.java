package com.maxcode.buyer.exception;

/**
 * Created by gubaoer on 17/7/2.
 */

import com.maxcode.buyer.dao.ErrorInfo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ProjExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {

        ModelAndView mav = new ModelAndView();

        mav.addObject("exception", e);

        mav.addObject("url", req.getRequestURL());

        mav.setViewName("error");

        return mav;
    }

    @ExceptionHandler(value = ProjException.class)
    @ResponseBody
    public ErrorInfo<String> jsonErrorHandler(HttpServletRequest req, ProjException e) throws Exception {

        ErrorInfo<String> r = new ErrorInfo<>();

        r.setMessage(e.getMessage());

        r.setCode(ErrorInfo.ERROR);

        r.setData("Some Data");

        r.setUrl(req.getRequestURL().toString());

        return r;
    }

    public static void main(String[] args) {
        long l = countPositiveSolutions(100);
        System.out.println(l);
    }

   
    public static long countSolutions(int n) {
        // 使用long避免溢出
        long result = (long)(n + 1) * (n + 2) / 2;
        return result;
    }

    public static long countPositiveSolutions(int n) {
        if (n < 3) {
            return 0;
        }
        // 使用long避免溢出
        long m = n - 1;
        return (m * (m - 1)) / 2;
    }

}