package com.yzt.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    //ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //添加shiro的内置过滤器
        /*
            anon：无需认证可以访问
            authc：必须认证了才能访问
            user：必须拥有记住我功能才能用
            perms：拥有对某个资源的权限才能访问
            role：拥有某个角色权限才能访问
         */
        Map<String, String> filterMap = new LinkedHashMap<>();
        //授权
        filterMap.put("/system/*", "perms[system:manager]");
        filterMap.put("/supplier/*", "perms[purchase:supplier]");
        filterMap.put("/purchase/*", "perms[purchase:order]");
        filterMap.put("/sale/*", "perms[sale:order]");
        filterMap.put("/customer/*", "perms[sale:customer]");
        filterMap.put("/product/*", "perms[product:message]");
        filterMap.put("/analysis/toSaleAnalysis/*","perms[analysis:sale]");
        filterMap.put("/analysis/toProfitAnalysis/*", "perms[analysis:profit]");
        filterMap.put("/stock/toStocktaking/*", "perms[stock:stocktaking]");
        filterMap.put("/stock/stockManage/*", "perms[stock:stockManage]");

        filterMap.put("/index", "authc");
        filterMap.put("/login", "anon");
        filterMap.put("/toLogin", "anon");
        filterMap.put("/static/**", "anon");
        filterMap.put("/**", "user");
        filterMap.put("/logout", "logout");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        //设置登录的请求
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        return shiroFilterFactoryBean;
    }

    //DefaultWebSecurityManager
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    //创建 realm对象，需要自定义类
    @Bean(name = "userRealm")
    public UserRealm userRealm() {
        return new UserRealm();
    }

    @Bean
    public ShiroDialect getShiroDialect() {
        return new ShiroDialect();
    }

}
