package com.tenton.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Date: 2021/1/29
 * @Author: Tenton
 * @Description: 路径拦截
 */
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //为视图起别名
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
        registry.addViewController("/regist.html").setViewName("regist");
    }

    /**
     * 自定义的国际化组件就生效了
     * @return
     */
    @Bean
    public MyLocaleResolver localeResolver(){
        return new MyLocaleResolver();
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
                //拦截所有
                .addPathPatterns("/**")
                //放开不需要拦截的静态文件和路径
                .excludePathPatterns("/","/index.html","/user/regist","/user/sendEmail",
                        "/regist.html","/login","/css/**",
                        "/img/**","/js/**");
    }
}