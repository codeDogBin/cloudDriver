package com.bin.filer;

import com.bin.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SessionFilter implements Filter {
    //登录的几个访问地址
    public static String[] ignorePath = new String[]{
            "/toLogin",
            "/toRegister",
            "/login.do",
            "/register.do",
            "/index.jsp"};
    //对比地址的方法
    public static boolean isIgnore(String path){
        for (String temp : ignorePath) {
            if(temp.equals(path)) return true;
        }
        return  false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getRequestURI();
        User user = (User)request.getSession().getAttribute("user");
        if(user == null && !isIgnore(path) ){
            response.sendRedirect("/index.jsp");
        }else{
            filterChain.doFilter(request,response);
        }
    }

    @Override
    public void destroy() {

    }
}
