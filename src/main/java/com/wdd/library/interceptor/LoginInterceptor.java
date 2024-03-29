package com.wdd.library.interceptor;

import com.wdd.library.util.Const;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;


public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        Set<String> uri = new HashSet<String>();

        uri.add("/toLogin.htm");
        uri.add("/checkReader.do");
        uri.add("/toRegister.htm");
        uri.add("/submitAddReader.do");
        uri.add("/doLogin.do");



        String servletPath = request.getServletPath();
        if(uri.contains(servletPath)){
            return true;
        }



        HttpSession session = request.getSession();
        Object admin = session.getAttribute(Const.ADMIN);
        Object reader = session.getAttribute(Const.READER);
        if(admin!=null || reader!=null){
            return true;
        }else{
            response.sendRedirect(request.getContextPath()+"/toLogin.htm");
            return false;
        }



    }
}
