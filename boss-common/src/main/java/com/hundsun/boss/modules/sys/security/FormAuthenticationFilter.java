package com.hundsun.boss.modules.sys.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Service;

import com.hundsun.boss.common.utils.RSAUtil;

/**
 * 表单验证（包含验证码）过滤类
 */
@Service
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

    public static final String DEFAULT_CAPTCHA_PARAM = "validateCode";

    private String captchaParam = DEFAULT_CAPTCHA_PARAM;

    public String getCaptchaParam() {
        return captchaParam;
    }

    protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, getCaptchaParam());
    }

    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String rsaSessionKey = request.getParameter("rsaSessionKey");
        String agent = ((HttpServletRequest) request).getHeader("user-agent");
        String username = getUsername(request);
        String password = getPassword(request);
        // 如果是从pc端访问
        if (!agent.contains("Android") && !agent.contains("iPhone")) {
            username = RSAUtil.decodeRSA(rsaSessionKey, getUsername(request));
            password = RSAUtil.decodeRSA(rsaSessionKey, getPassword(request));
        }
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        String captcha = getCaptcha(request);
        return new UsernamePasswordToken(username, password.toCharArray(), rememberMe, host, captcha);
    }
}