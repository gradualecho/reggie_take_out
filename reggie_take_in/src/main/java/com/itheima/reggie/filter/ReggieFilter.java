package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.R;
import com.itheima.reggie.common.ReggieThreadLocal;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "Reggie",urlPatterns = "/*")
public class ReggieFilter implements Filter {
    //    路径匹配器,支持通配符
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest servletRequest1 = (HttpServletRequest) servletRequest;
        HttpServletResponse servletResponse1 = (HttpServletResponse) servletResponse;


        String[] strings =
                {"/employee/login","employee/logout","/backend/**","/front/**","/user/sendMsg","/user/login"};
        String requestURI = servletRequest1.getRequestURI();

//        url检查
        if (check(strings,requestURI)){
            filterChain.doFilter(servletRequest1,servletResponse1);
            return;
        }
//        员工端登录状态检查
        if (servletRequest1.getSession().getAttribute("employee") != null) {
            Long aLong = (Long) servletRequest1.getSession().getAttribute("employee");
            ReggieThreadLocal.setCurrentThreadLocal(aLong);
            filterChain.doFilter(servletRequest1, servletResponse1);
            return;
        }
//        未登录状态
        servletResponse1.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

    }

    //    匹配检查
    public boolean check(String[] urls,String uri){
        for(String url : urls){
            if (PATH_MATCHER.match(url, uri)){
                return true;
            }
        }
        return false;
    }
}
