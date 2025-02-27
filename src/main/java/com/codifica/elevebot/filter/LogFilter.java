package com.codifica.elevebot.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LogFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LogFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        long tempoInicio = System.currentTimeMillis();

        filterChain.doFilter(request, response);

        long duracao = System.currentTimeMillis() - tempoInicio;
        String metodo = request.getMethod();
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        String ip = request.getRemoteAddr();
        int status = response.getStatus();

        logger.info("Requisição [Método: {}, URI: {}, Query: {}, IP: {}] - Resposta [Status: {}] - Duração: {} ms",
                metodo, uri, query, ip, status, duracao);
    }
}