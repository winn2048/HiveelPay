package com.hiveelpay;

import com.hiveelpay.common.util.MyLog;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class AuthorityInterceptor implements HandlerInterceptor {
    private final static MyLog _log = MyLog.getLog(AuthorityInterceptor.class);
    private static final List<String> whiteList = new ArrayList<>();

    static {
        whiteList.add("login.html");
        whiteList.add("dologin");
        whiteList.add("logout.html");
        whiteList.add("/css/");
        whiteList.add("/datas/");
        whiteList.add("/images/");
        whiteList.add("/js/");
        whiteList.add("/plugins/");
        whiteList.add("/invoice/");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getRequestURI();
        if (whiteList.stream().anyMatch(uri::contains)) {
            return true;
        }
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            try {
                response.sendRedirect(request.getContextPath() + "/login.html");
            } catch (Exception e) {
                _log.error("", e);
                e.printStackTrace();
            }
            _log.warn("blocked:{}", request.getRequestURI());
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
