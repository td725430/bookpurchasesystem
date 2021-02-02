package com.tenton.config;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @Date: 2021/1/29
 * @Author: Tenton
 * @Description: 登录页面国际化
 */
public class MyLocaleResolver implements LocaleResolver {
    /**
     * 解析请求
     * @param httpServletRequest
     * @return
     */
    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        //获取请求中的语言参数
        String language = httpServletRequest.getParameter("l");
        //如果没有就使用默认的
        Locale locale = Locale.getDefault();
        //如果请求的链接携带了国际化的参数
        if (!StringUtils.isEmpty(language)){
            String[] split = language.split("_");
            //国家  地区
            locale = new Locale(split[0], split[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}