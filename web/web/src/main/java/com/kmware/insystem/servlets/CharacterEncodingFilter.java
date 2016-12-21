package com.kmware.insystem.servlets;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CharacterEncodingFilter implements Filter{

    private String requestEncoding = null;
    private String responseEncoding = null;


    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        if(this.requestEncoding != null){
            request.setCharacterEncoding(this.requestEncoding);
        }
        if(this.responseEncoding != null){
            response.setCharacterEncoding(this.responseEncoding);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        requestEncoding = arg0.getInitParameter("requestEncoding");
        responseEncoding = arg0.getInitParameter("responseEncoding");
    }

}
