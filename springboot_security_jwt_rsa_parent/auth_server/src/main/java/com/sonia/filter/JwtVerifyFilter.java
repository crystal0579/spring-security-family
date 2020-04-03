package com.sonia.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonia.config.RsaKeyProperties;
import com.sonia.domain.Payload;
import com.sonia.model.SysUser;
import com.sonia.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class JwtVerifyFilter extends BasicAuthenticationFilter {

    private RsaKeyProperties rsaKeyProperties;

    public JwtVerifyFilter(AuthenticationManager authenticationManager, RsaKeyProperties rsaKeyProperties) {
        super(authenticationManager);
        this.rsaKeyProperties = rsaKeyProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean debug = this.logger.isDebugEnabled();
        String header = request.getHeader("Authorization");
        if (header == null || !header.toLowerCase().startsWith("bearer")) {//对标生成token的代码
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            PrintWriter out = response.getWriter();
            Map resultMap = new HashMap();
            resultMap.put("code", HttpServletResponse.SC_FORBIDDEN);
            resultMap.put("msg", "请登录");
            out.write(new ObjectMapper().writeValueAsString(resultMap));

            out.flush();
            out.close();
            chain.doFilter(request, response);
        } else {
            String token = header.replace("Bearer", "");
            Payload<SysUser> payload = JwtUtils.getInfoFromToken(token, rsaKeyProperties.getPublicKey(), SysUser.class);//解读token
            SysUser user = payload.getUserInfo();
            if (user != null){
                UsernamePasswordAuthenticationToken authResult = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authResult);
            }

            chain.doFilter(request, response);
        }
    }
}
