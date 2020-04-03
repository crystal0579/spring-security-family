package com.sonia.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonia.config.RsaKeyProperties;
import com.sonia.model.SysRole;
import com.sonia.model.SysUser;
import com.sonia.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {


    private AuthenticationManager authenticationManager;


    private RsaKeyProperties rsaKeyProperties;


    //filter 肯定是要在路径的配置类里面注入进来的，所以这些属性由外界提供更动态
    public JwtLoginFilter(AuthenticationManager authenticationManager, RsaKeyProperties rsaKeyProperties) {
        this.authenticationManager = authenticationManager;
        this.rsaKeyProperties = rsaKeyProperties;
    }

    /**
     *其实可以不用重写 UsernamePasswordAuthenticationFilter.attemptAuthentication()
     *无非是为了写   认证不通过的数据返回信息罢了
     */
    @Override//重写 来源于UsernamePasswordAuthenticationFilter
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            SysUser user = new ObjectMapper().readValue(request.getInputStream(), SysUser.class);//user 是从 request中过来的，并非数据库， ObjectMapper是jackson工具的优美
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()); //这是本地spring security token
//            this.setDetails(request, authRequest);
            return authenticationManager.authenticate(authRequest);//比对数据库中的数据
        } catch (IOException e) {


            try {//不再返回 jsp 页面，而是数据告知
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                PrintWriter out = response.getWriter();
                Map resultMap = new HashMap();
                resultMap.put("code", HttpServletResponse.SC_UNAUTHORIZED);
                resultMap.put("msg", "用户名或密码错误");
                out.write(new ObjectMapper().writeValueAsString(resultMap));


                out.flush();
                out.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            throw new RuntimeException();
        }


    }


    @Override//重写  来源于AbstractAuthenticationProcessingFilter
    //认证成功后 生成 JWT token
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SysUser user = new SysUser();
        //就set了name 和 roles
        user.setUsername(authResult.getName());
        user.setRoles((List<SysRole>) authResult.getAuthorities());
        String token = JwtUtils.generateTokenExpireInMinutes(user, rsaKeyProperties.getPrivateKey(), 24*60);
        response.addHeader("Authorization", "Bearer" + token);//一个标志而已，也可以写别的，原来是 basic，为了区别原来,解读token的地方也要以此为标


//认证通过的返回信息
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        Map resultMap = new HashMap();
        resultMap.put("code", HttpServletResponse.SC_OK);
        resultMap.put("msg", "认证通过");
        out.write(new ObjectMapper().writeValueAsString(resultMap));


        out.flush();
        out.close();


    }
}